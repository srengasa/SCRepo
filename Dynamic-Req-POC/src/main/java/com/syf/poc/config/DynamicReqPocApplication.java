package com.syf.poc.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.syf.poc")
public class DynamicReqPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicReqPocApplication.class, args);
	}
}
