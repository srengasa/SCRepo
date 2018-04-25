package com.syf.caching.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;

@Region("customer")
public class Customer {

	@Id
	private long accountNumber;
	private String firstName;
	private String lastName;
	
	@PersistenceConstructor
	public Customer() {}
	
	public Customer(long acNo, String fn, String ln) {
		this.accountNumber = acNo;
		this.firstName = fn;
		this.lastName = ln;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
