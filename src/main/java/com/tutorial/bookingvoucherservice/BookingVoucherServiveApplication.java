package com.tutorial.bookingvoucherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BookingVoucherServiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingVoucherServiveApplication.class, args);
	}

}
