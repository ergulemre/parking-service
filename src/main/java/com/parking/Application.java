package com.parking;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.parking.config.RunnableObject;
import com.parking.entity.Garage;
import com.parking.repository.GarageRepository;

@SpringBootApplication
public class Application {
	
	private final Integer lotSizeOfGarage = 10;

	public static void main(String[] args) {
		Executor executor = Executors.newSingleThreadExecutor();
		RunnableObject runnable = new RunnableObject("mythread");
		executor.execute(runnable);
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(GarageRepository repository) {
	    return (args) -> {
	    	for(int i=1; i<lotSizeOfGarage+1; i++) {
	    		repository.save(new Garage(Long.valueOf(i), null,true));
	    	}
	    };
	}
}
