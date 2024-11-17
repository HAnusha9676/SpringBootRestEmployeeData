package com.sathya.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootRestfinalllApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestfinalllApplication.class, args);
	}

}
