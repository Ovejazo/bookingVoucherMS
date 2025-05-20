package com.tutorial.frecuencyclentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FrecuencyClentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrecuencyClentsServiceApplication.class, args);
	}

}
