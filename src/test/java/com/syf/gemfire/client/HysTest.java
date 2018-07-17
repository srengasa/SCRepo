package com.syf.gemfire.client;

import java.util.HashMap;

import org.springframework.web.client.RestTemplate;

import com.syf.gemfire.client.model.Customer;

public class HysTest {

	public static void main(String[] args) {
		System.out.println("Start");
		Customer c = new Customer();
		c.setAccountNumber(4123l);
		c.setFirstName("hi");
		String str = new RestTemplate().postForObject("http://localhost:9002/hysterixTest", c, String.class);
		System.out.println("Response**********" + str);
		
	}

}
