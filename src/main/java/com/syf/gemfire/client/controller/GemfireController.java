package com.syf.gemfire.client.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.syf.caching.domain.Customer;
import com.syf.gemfire.client.helper.GemfireService;
import com.syf.gemfire.client.repository.CustomerRepository;

@RestController
public class GemfireController {
	
	private static final Logger logger = Logger.getLogger(GemfireController.class);

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private GemfireService service;
	
	@PostMapping("/addCustomer")
	public String addCustomer(@RequestBody Customer customer) {
		customerRepo.save(customer);
		return "success";
	}
	
	@GetMapping("/getAllCustomer")
	public Iterable<Customer> getCustomers() {
		return customerRepo.findAll();
	}
	
	@PostMapping("/getCustmerByAcctNo")
	public Customer getCustomerByAccountNumber(@RequestBody Customer customer) {
		return customerRepo.getCustomerByAccountNumber(customer.getAccountNumber());
	}
	
	@PostMapping("/hysterixTest")
	public String testHysterix(@RequestBody Customer customer) {
		return service.saveCustomer(customer);
	}
	
	@PostMapping("junitTest")
	public String testJunit(@RequestBody Customer customer) {
		logger.info("testJunit tenter");
		service.createCustomer(customer);
		logger.info("testJunit exit");
		return "happy";
	}
	
}
