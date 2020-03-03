package ru.ok.botapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ok.botapi.comment.ModerationComment;

@Service
public class SendServiceImpl implements SendService {
	
	static final Logger logger = LogManager.getLogger(SendServiceImpl.class.getName());
	
	@Override
	public void sendComment(ModerationComment comment, String adminUrl) throws RestClientException {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ModerationComment> request = new HttpEntity<>(comment);
		restTemplate.postForLocation(adminUrl, request, ModerationComment.class);
		logger.info("Comment submitted: " + comment);
	}
}
