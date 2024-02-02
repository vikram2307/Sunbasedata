package com.example.demo.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Customer;
import com.example.demo.model.Login;
import com.example.demo.repo.CustomerRepo;
import com.example.demo.repo.LoginRepo;
import com.example.demo.service.CustomerService;

@Service
public class CustomerDao implements CustomerService {

    @Autowired
    LoginRepo lr;

    @Autowired
    CustomerRepo cr;

    @PersistenceContext
    private EntityManager entityManager;
    

    @Override
    public Login login(String loginid, String password) {
        System.out.println(loginid + " " + password);
        Login detail = lr.getByLoginid(loginid);
        System.out.println(detail);
        if (detail == null || !detail.getPassword().equals(password))
            return null;
        else
            return detail;
    }

    @Override
    public int add(Customer c) {
        cr.save(c);
        return 1;
    }

    @Override
    public List<Customer> display() {
        return cr.findAll();
    }

    @Override
    public void delete(int id) {
        Customer cu = cr.getById(id);
        cr.delete(cu);
    }

    @Override
    public Customer getOneData(int id) {
        return cr.getById(id);
    }

    @Override
    public void update(Customer us) {
        Customer u = new Customer();
        u.setId(us.getId());
        u.setAddress(us.getAddress());
        u.setCity(us.getCity());
        u.setEmail(us.getEmail());
        u.setFirst_name(us.getFirst_name());
        u.setLast_name(us.getLast_name());
        u.setPhone(us.getPhone());
        u.setState(us.getState());
        u.setStreet(us.getStreet());
        cr.save(u);
    }

    @Override
    public List<Customer> searchCustomers(String keyword, String criteria) {
    	if (keyword.isEmpty() ||keyword.equals("All")) {
            return display(); // Return the full list when the search keyword is empty
        }
    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
    	Root<Customer> root = query.from(Customer.class);

    	Predicate predicate;

    	switch (criteria) {
    	    case "first_name":
    	        predicate = cb.like(cb.lower(root.get("first_name")), "%" + keyword.toLowerCase() + "%");
    	        break;
    	    case "last_name":
    	        predicate = cb.like(cb.lower(root.get("last_name")), "%" + keyword.toLowerCase() + "%");
    	        break;
    	    case "city":
    	        predicate = cb.like(cb.lower(root.get("city")), "%" + keyword.toLowerCase() + "%");
    	        break;
    	    case "email":
    	        predicate = cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%");
    	        break;
    	    case "phone":
    	        predicate = cb.like(root.get("phone"), "%" + keyword + "%");
    	        break;
    	    default:
    	        return null;
    	}

    	query.where(predicate);

    	return entityManager.createQuery(query).getResultList();

    }
    
    @Value("${api.base-url}")
    private String apiUrl;  // Inject the base URL from application.properties or application.yml

    @Value("${api.get-customer-list-path}")
    private String getCustomerListPath;  // Inject the specific path from application.properties or application.yml

    @Override
    public List<Customer> getCustomerList(String accessToken) {
        // Set up headers with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Set up the RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to fetch customer list
        ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(
                apiUrl + getCustomerListPath,
                HttpMethod.GET,
                null,
                Customer[].class
        );

        // Extract the customer array from the response
        Customer[] customersArray = responseEntity.getBody();

        // Convert the array to a List
        List<Customer> customerList = Arrays.asList(customersArray);

        return customerList;
    }

    @Override
    public void saveCustomers(List<Customer> customerList) {
        for (Customer customer : customerList) {
            // Check if the customer already exists in the database based on UUID
            Customer existingCustomer = cr.findByUuid(customer.getUuid());

            if (existingCustomer != null) {
                // If the customer exists, update the existing record
                existingCustomer.setFirst_name(customer.getFirst_name());
                existingCustomer.setLast_name(customer.getLast_name());
                existingCustomer.setPhone(customer.getPhone());
                existingCustomer.setState(customer.getState());
                existingCustomer.setCity(customer.getCity());
                existingCustomer.setStreet(customer.getStreet());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setAddress(customer.getAddress());
                // Save the updated customer
                cr.save(existingCustomer);
            } else {
                // If the customer does not exist, save a new record
                cr.save(customer);
            }
        }
    }

	@Override
	public List<Customer> performDataSynchronization() {
		// TODO Auto-generated method stub
		return null;
	}



   }
