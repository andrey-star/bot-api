package ru.ok.botapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ok.botapi.entity.Subscription;
import ru.ok.botapi.service.SubscriptionService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/bot-api")
public class SubscriptionRestController {

	private final String BASE_URI = "/subscriptions";
	
	@Resource
	private SubscriptionService subscriptionService;
	
	@Autowired
	public SubscriptionRestController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	@GetMapping(BASE_URI)
	public List<Subscription> findAll() {
		return subscriptionService.findAll();
	}
	
	@GetMapping(BASE_URI + "/{subscriptionId}")
	public Subscription findById(@PathVariable int subscriptionId) {
		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throw new RuntimeException("Subscription id not found - " + subscriptionId);
		}
		return subscription;
	}
	
	@PostMapping(BASE_URI)
	public Subscription addSubscription(@RequestBody Subscription subscription) {
		subscription.setId(0);
		subscriptionService.save(subscription);
		return subscription;
	}
	
	@PutMapping(BASE_URI)
	public Subscription updateSubscription(@RequestBody Subscription subscription) {
		subscriptionService.save(subscription);
		return subscription;
	}
	
	@DeleteMapping(BASE_URI + "/{subscriptionId}")
	public String deleteSubscription(@PathVariable int subscriptionId) {
		Subscription subscription = subscriptionService.findById(subscriptionId);
		if (subscription == null) {
			throw new RuntimeException("Subscription id not found - " + subscriptionId);
		}
		subscriptionService.deleteById(subscriptionId);
		return "Deleted subscription id - " + subscriptionId;
	}
}
