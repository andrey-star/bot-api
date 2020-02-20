package ru.ok.botapi.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ok.botapi.comment.ModerationComment;
import ru.ok.botapi.entity.Subscription;
import ru.ok.botapi.remote.Pair;
import ru.ok.botapi.remote.RMIComment;
import ru.ok.botapi.remote.RMICommentInterface;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@EnableScheduling
public class BusinessServiceImpl implements BusinessService {
    @Resource
    private SubscriptionService subscriptionService;

    @Resource
    private SendService sendService;

    private final static String serverURI = "//localhost/journal";
    Queue<RMIComment> queue = new ArrayDeque<>();
    private static RMICommentInterface lookUp;

    static {
        try {
            lookUp = (RMICommentInterface) Naming.lookup(serverURI);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            //TODO: readable exception messages
            e.printStackTrace();
        }
    }
    @Override
    @Scheduled(fixedRate = 10000)
    public void getNewComments() {
        List<Subscription> all = subscriptionService.findAll();
        List<Pair> forRequest = new ArrayList<>();
        for (Subscription sub : all) {
            forRequest.add(new Pair(sub.getPostId(), sub.getLastCommentId()));
        }
        try {
            List<RMIComment> response = lookUp.getComments(forRequest);
            //addAll operation is optional, might cause some troubles later
            queue.addAll(response);
            System.out.println("Comments pulled: " + response);
        } catch (RemoteException e) {
            //TODO: readable exception message
            e.printStackTrace();
        }
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void sendNewComment() {
        List<Subscription> all = subscriptionService.findAll();
        //very inefficient, but ok for a skeleton
        if (!queue.isEmpty()) {
            RMIComment comment = queue.poll();
            String url = getUrl(all,  comment);
            if (url != null) {
                ModerationComment toSend = new ModerationComment(comment.getPostId(), comment.getId(), comment.getText());
                sendService.sendComment(toSend, url);
                System.out.println("Comment sent: " + toSend);
            }
        }
    }

    String getUrl(List<Subscription> all, RMIComment comment) {
        for (Subscription sub : all) {
            if (sub.getPostId() == comment.getPostId()) {
                return sub.getUrl();
            }
        }
        return null;
    }




}
