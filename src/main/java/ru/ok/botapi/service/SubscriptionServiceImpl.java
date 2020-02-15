package ru.ok.botapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ok.botapi.dao.SubscriptionRepository;
import ru.ok.botapi.entity.Subscription;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Resource
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	@Override
	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}
	
	@Override
	public Subscription findById(int id) {
		Optional<Subscription> result = subscriptionRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		throw new RuntimeException("Did not find post id - " + id);
	}
	
	@Override
	public void save(Subscription subscription) {
		subscriptionRepository.save(subscription);
	}
	
	@Override
	public void deleteById(int id) {
		subscriptionRepository.deleteById(id);
	}
}
