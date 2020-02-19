package ru.ok.botapi.service;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import ru.ok.botapi.comment.ModerationComment;

public class SendServiceImpl implements SendService {
	
	@Override
	public void sendComment(ModerationComment comment, String adminUrl) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ModerationComment> request = new HttpEntity<>(comment);
		restTemplate.postForLocation(adminUrl, request, ModerationComment.class);
	}
}
