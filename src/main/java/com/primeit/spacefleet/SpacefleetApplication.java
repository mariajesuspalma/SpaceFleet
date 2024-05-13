package com.primeit.spacefleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpacefleetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacefleetApplication.class, args);
	}

}
