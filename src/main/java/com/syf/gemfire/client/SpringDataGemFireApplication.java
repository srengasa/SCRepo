package com.syf.gemfire.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@EnableGemfireRepositories(basePackages={"com.syf.gemfire.client", "com.syf.caching"})
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages={"com.syf.gemfire.client", "com.syf.caching"})
public class SpringDataGemFireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataGemFireApplication.class, args);
	}
}
