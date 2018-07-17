package com.syf.gemfire.client.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.syf.caching.domain.Customer;
import com.syf.gemfire.client.repository.CustomerRepository;

@Service
public class GemfireService {

	@Autowired
	private CustomerRepository custRepo;
	
	@HystrixCommand(fallbackMethod="saveCustomerFallback")
	public String saveCustomer(Customer customer) {
		System.out.println("inside original method");
		Customer c = custRepo.save(customer);
		return "Success";
	}
	
	public String saveCustomerFallback(Customer customer) {
		System.out.println("inside the fallback method*****");
		return "Hey the cache server is down";
	}

	public String createCustomer(Customer customer) {
		System.out.println("inside the createCustomer method");
		custRepo.save(customer);
		System.out.println("Exit the createCustomer method");
		return null;
	}
}
