package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Customer;
import com.example.demo.model.Login;

public interface CustomerService {
	
	Login login(String loginid, String password);

	int add(Customer c);

	List<Customer> display();

	void delete(int id);

	Customer getOneData(int id);

	void update(Customer us);

	List<Customer> searchCustomers(String keyword, String criteria);


	List<Customer> getCustomerList(String accessToken);

	void saveCustomers(List<Customer> customerList);

	List<Customer> performDataSynchronization();


}
