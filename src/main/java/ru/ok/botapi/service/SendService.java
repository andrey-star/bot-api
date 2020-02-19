package ru.ok.botapi.service;

import ru.ok.botapi.comment.ModerationComment;

public interface SendService {
	
	void sendComment(ModerationComment comment, String adminUrl);
	
}
