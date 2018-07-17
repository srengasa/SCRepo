package com.syf.gemfire.client.repository;

import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.syf.caching.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("select * from /customer where id=$1")
	Customer getCustomerByAccountNumber(long accountNumber);
}
