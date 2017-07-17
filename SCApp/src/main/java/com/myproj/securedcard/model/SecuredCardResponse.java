package com.myproj.securedcard.model;

public class SecuredCardResponse {

	private Status status;
	private Customer customer;
	//private List<Account> accounts;
	private User user;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
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
