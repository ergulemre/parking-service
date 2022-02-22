package com.parking.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.repository.GarageRepository;
import com.parking.service.GarageService;

@RestController
public class ParkingController {
	
	@Autowired
	public GarageService garageService;
	
	@Autowired
	public GarageRepository vehicleRepo;
	
	@Autowired
	private Executor executor;

	@PostMapping("/park")
	public String greeting(HttpEntity<String> httpEntity) throws Exception {
	    String parkingItem = httpEntity.getBody();
		CompletableFuture<String> result = garageService.park(parkingItem);
		return result.get();
	}
	
	@GetMapping("/status")
	public String garageStatus(HttpEntity<String> httpEntity) throws Exception {
		
		CompletableFuture<String> vehicleSlots = garageService.garageStatus();
		
		return vehicleSlots.get();
	}
	
	@DeleteMapping("/delete")
	public String deleteItem(HttpEntity<String> httpEntity) throws Exception {
		String item = httpEntity.getBody();
		garageService.leave(item);
		return null;
	}
}
