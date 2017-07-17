package com.myproj.securedcard.model;

public class ESFAcctMgmtRequest {

	//private Header header;
	private Customer customer;
	//private Account account;
	private User user;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
