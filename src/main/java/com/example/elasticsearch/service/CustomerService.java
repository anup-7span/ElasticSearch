package com.example.elasticsearch.service;

import com.example.elasticsearch.model.CustomerModel;
import com.example.elasticsearch.entity.Customer;

import java.io.IOException;
import java.util.List;

public interface CustomerService
{
	void create(Customer customer);

	Iterable<Customer> getCustomer();

	String update(Customer customer);

	 String deleteById(Integer id);

	List<Customer> fetchCustomSearchCustomer(
			Integer min_age, Integer max_age, Double min_amount, Double max_amount, Boolean productIsAvailable) throws IOException;

	CustomerModel getCustomerAggregation() throws IOException;
}
