package ru.ok.botapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ok.botapi.entity.Subscription;
import ru.ok.botapi.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/bot-api")
public class SubscriptionRestController {

	private SubscriptionService subscriptionService;
	
	@Autowired
	public SubscriptionRestController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	@GetMapping("/subscriptions")
	public List<Subscription> findAll() {
		return subscriptionService.findAll();
	}
	
	@GetMapping("/subscriptions/{subscriptionId}")
	public Subscription findById(@PathVariable int subscriptionId) {
		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throw new RuntimeException("Subscription id not found - " + subscriptionId);
		}
		return subscription;
	}
	
	@PostMapping("/subscriptions")
	public Subscription addSubscription(@RequestBody Subscription subscription) {
		subscription.setId(0);
		subscriptionService.save(subscription);
		return subscription;
	}
	
	@PutMapping("/subscriptions")
	public Subscription updateSubscription(@RequestBody Subscription subscription) {
		subscriptionService.save(subscription);
		return subscription;
	}
	
	@DeleteMapping("/subscriptions/{subscriptionId}")
	public String deleteSubscription(@PathVariable int subscriptionId) {
		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throw new RuntimeException("Subscription id not found - " + subscriptionId);
		}
		subscriptionService.deleteById(subscriptionId);
		return "Deleted subscription id - " + subscriptionId;
	}
}
