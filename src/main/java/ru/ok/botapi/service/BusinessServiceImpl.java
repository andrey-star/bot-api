package ru.ok.botapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ok.botapi.comment.ModerationComment;
import ru.ok.botapi.entity.Subscription;
import ru.ok.botapi.remote.RMIComment;
import ru.ok.botapi.remote.RMICommentInterface;
import ru.ok.botapi.util.Pair;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
@EnableScheduling
public class BusinessServiceImpl implements BusinessService {
	
	static final Logger logger = LogManager.getLogger(BusinessServiceImpl.class.getName());
	
	private final static String serverURI = "//localhost/journal";
	
	private static final int MAX_THREADS = 20;
	private static final int MIN_THREADS = 5;
	private static final int UPPER_THRESHOLD = 25;
	private static final int LOWER_THRESHOLD = 5;
	private static final int THREAD_QUOTIENT = 2;
	private static int threadCount = MIN_THREADS;
	
	private static RMICommentInterface lookUp;
	
	static {
		try {
			lookUp = (RMICommentInterface) Naming.lookup(serverURI);
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private ArrayBlockingQueue<RMIComment> queue = new ArrayBlockingQueue<>(100);
	
	private ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
	@Resource
	private SubscriptionService subscriptionService;
	@Resource
	private SendService sendService;
	
	@Override
	@Async
	@Scheduled(fixedDelay = 5000)
	public void getNewComments() {
		List<Subscription> all = subscriptionService.findAll();
		List<Pair> forRequest = new ArrayList<>();
		for (Subscription sub : all) {
			forRequest.add(new Pair(sub.getPostId(), sub.getLastCommentId()));
		}
		try {
			List<RMIComment> response = lookUp.getComments(forRequest);
			logger.info("Comments pulled: " + response);
			for (RMIComment rmiComment : response) {
				queue.put(rmiComment);
			}
		} catch (RemoteException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	@Async
	@Scheduled(fixedDelay = 1000)
	public void sendNewComment() {
		while (!queue.isEmpty()) {
			if (queue.size() / threadCount > UPPER_THRESHOLD && threadCount < MAX_THREADS) {
				increasePoolSize();
			} else if (queue.size() / threadCount < LOWER_THRESHOLD && threadCount > MIN_THREADS) {
				decreasePoolSize();
			}
			RMIComment comment = queue.remove();
			List<Subscription> byPostId = subscriptionService.findByPostId(comment.getPostId());
			if (byPostId.isEmpty()) {
				logger.warn("Couldn't find subscription to postId: " + comment.getPostId());
				continue;
			}
			Subscription subscription = byPostId.get(0);
			String url = subscription.getUrl();
			ModerationComment toSend = new ModerationComment(comment.getPostId(), comment.getId(), comment.getText());
			Runnable sendComment = () -> {
				try {
					sendService.sendComment(toSend, url);
					subscription.setLastCommentId(comment.getId());
					subscriptionService.save(subscription);
				} catch (Exception e) {
					logger.error("Failed to submit comment", e);
				}
			};
			executorService.execute(sendComment);
		}
	}
	
	private void shutdownPool() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void increasePoolSize() {
		shutdownPool();
		executorService = Executors.newFixedThreadPool(threadCount * THREAD_QUOTIENT);
	}
	
	private void decreasePoolSize() {
		shutdownPool();
		executorService = Executors.newFixedThreadPool(threadCount / THREAD_QUOTIENT);
	}
	
}
