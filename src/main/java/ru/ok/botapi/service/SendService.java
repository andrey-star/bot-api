package ru.ok.botapi.service;

import org.springframework.web.client.RestClientException;
import ru.ok.botapi.comment.ModerationComment;

public interface SendService {
	
	void sendComment(ModerationComment comment, String adminUrl) throws RestClientException;
	
}
