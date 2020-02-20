package ru.ok.botapi.service;

import ru.ok.botapi.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
	
	List<Subscription> findAll();
	
	Subscription findById(int id);
	
	List<Subscription> findByPostId(long postId);
	
	void save(Subscription subscription);
	
	void deleteById(int id);
	
}
