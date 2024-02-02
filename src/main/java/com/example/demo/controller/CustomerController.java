package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Customer;
import com.example.demo.model.Login;
import com.example.demo.service.CustomerService;

@Controller
public class CustomerController {
	@Autowired
	CustomerService cs;

	@RequestMapping("/index")
	public String home() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String checkLogin(@ModelAttribute("lg") Login lg, HttpSession session, ModelMap model) {
	    String loginId = lg.getLoginid();
	    String password = lg.getPassword();

	    // Perform authentication and obtain the access token
	    String requestBody = String.format("{\"login_id\": \"%s\", \"password\": \"%s\"}", loginId, password);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        ResponseEntity<String> authResponseEntity = restTemplate.postForEntity(
	                "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp",
	                requestEntity,
	                String.class
	        );

	        // Extract the access token from the authentication response
	        String accessToken = extractAccessToken(authResponseEntity.getBody());

	        // Set accessToken in the session after successful authentication
	        session.setAttribute("accessToken", accessToken);

	        // Redirect to the CustomerList method to fetch and display data
	        return "redirect:/CustomerList";
	    } catch (HttpClientErrorException e) {
	        // Handle authentication failure
	        model.addAttribute("error", "Invalid credentials");
	        return "loginerror";
	    }
	}

	@RequestMapping("/CustomerList")
	public String CustomerList(ModelMap model, HttpSession session) {
	    // Retrieve the access token from the session
	    String accessToken = (String) session.getAttribute("accessToken");

	    if (accessToken == null) {
	        // Handle the case when the access token is not available
	        return "redirect:/login"; 
	    }

	    // Prepare headers for the request with the access token
	    HttpHeaders dataHeaders = new HttpHeaders();
	    dataHeaders.setContentType(MediaType.APPLICATION_JSON);
	    dataHeaders.set("Authorization", "Bearer " + accessToken);

	    // Create the HttpEntity with headers for the data request
	    HttpEntity<Void> dataRequestEntity = new HttpEntity<>(dataHeaders);
	    	
	    RestTemplate restTemplate = new RestTemplate();
		// Send the GET request to fetch records using the access token
	    ResponseEntity<Customer[]> dataResponseEntity = restTemplate.exchange(
	            "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
	            HttpMethod.GET,
	            dataRequestEntity,
	            Customer[].class
	    );

	      System.out.println("Data Response Code: " + dataResponseEntity.getStatusCode());
	    System.out.println("Data Response Body: " + Arrays.toString(dataResponseEntity.getBody()));
	    List<Customer> customerList = Arrays.asList(dataResponseEntity.getBody());
	    List<Customer> list= cs.display();
		model.addAttribute("list",list);
	  
	    return "CustomerList";
	}
	@RequestMapping("/syncData")
	public String syncData(ModelMap model, HttpSession session) {
	    // Retrieve the access token from the session
	    String accessToken = (String) session.getAttribute("accessToken");

	    if (accessToken == null) {
	        // Handle the case when the access token is not available
	        return "redirect:/login"; 
	    }

	    // Prepare headers for the request with the access token
	    HttpHeaders dataHeaders = new HttpHeaders();
	    dataHeaders.setContentType(MediaType.APPLICATION_JSON);
	    dataHeaders.set("Authorization", "Bearer " + accessToken);

	    // Create the HttpEntity with headers for the data request
	    HttpEntity<Void> dataRequestEntity = new HttpEntity<>(dataHeaders);

	    RestTemplate restTemplate = new RestTemplate();

	    try {
	        // Send the GET request to fetch records using the access token
	        ResponseEntity<Customer[]> dataResponseEntity = restTemplate.exchange(
	                "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
	                HttpMethod.GET,
	                dataRequestEntity,
	                Customer[].class
	        );

	        // Process the data response as needed
	        System.out.println("Data Response Code: " + dataResponseEntity.getStatusCode());
	        System.out.println("Data Response Body: " + Arrays.toString(dataResponseEntity.getBody()));

	        // Save the fetched data to your local database
	        List<Customer> customerList = Arrays.asList(dataResponseEntity.getBody());
	        cs.saveCustomers(customerList);

	        // Add the fetched data to the model
	        List<Customer> list = cs.display();
	        model.addAttribute("list", list);

	        return  "redirect:/Record_Sync";
	    } catch (HttpClientErrorException e) {
	        // Log the error or handle it appropriately
	        System.err.println("HTTP Client Error: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
	        return "redirect:/Error_Sync"; // Redirect to an error page or handle it appropriately
	    } catch (Exception e) {
	        // Log the error or handle it appropriately
	        e.printStackTrace();
	        return "redirect:/Error_Sync"; // Redirect to an error page or handle it appropriately
	    }
	}


	
	
	
	@RequestMapping("/loginerror")
	public String loginerror() {
		return "loginerror";
	}
	
	@RequestMapping("/addcustomer")
	public String addcustomer() {
		return "addcustomer";
	}
	
	@RequestMapping("/edit")
	public String edit() {
		return "edit";
	}
	@RequestMapping("/cutomeradded")
	public String cutomeradded() {
		return "cutomeradded";
	}

	private String extractAccessToken(String authResponseBody) {
	    // Extract the access token from the authentication response body
	    // Assuming the response body is a JSON string, you may use a JSON parsing library
	     String tokenPrefix = "\"access_token\":\"";
	    int startIndex = authResponseBody.indexOf(tokenPrefix) + tokenPrefix.length();
	    int endIndex = authResponseBody.indexOf("\"", startIndex);
	    return authResponseBody.substring(startIndex, endIndex);
	}

	
	
	

	
	
	@PostMapping("/add")
	public String register(@ModelAttribute("c") Customer c) {
		int a=cs.add(c);
		if(a==1) {
			return "redirect:/cutomeradded";
		}else {
			return "CustomerList";
		}
		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		cs.delete(id);
		return "redirect:/record_delete_popup";
	}
	
	@RequestMapping("/record_delete_popup")
	public String record_delete_popup() {
		return "record_delete_popup";
	}
	
	@GetMapping("/update/{id}")
	public String getOneData(@PathVariable int id,ModelMap m) {
		Customer u=cs.getOneData(id);

		System.out.println("Update"+u);
		m.addAttribute("customer",u);
		return "edit";
		
	}
	
	@PostMapping("update/edit")
	public String update(@ModelAttribute("us") Customer us) {

		cs.update(us);
		return "redirect:/record_update_popup";
	}
	

	@RequestMapping("/Error_Sync")
	public String Error_Sync() {
		return "Error_Sync";
	}
	@RequestMapping("/Record_Sync")
	public String Record_Sync() {
		return "Record_Sync";
	}
	@RequestMapping("/record_update_popup")
	public String record_update_popup() {
		return "record_update_popup";
	}
    @GetMapping("/search")
    public String searchCustomers(@RequestParam(name = "keyword") String keyword,
                                  @RequestParam(name = "criteria") String criteria,
                                  ModelMap model) {
        // Perform the search based on the keyword and criteria
        List<Customer> searchResults = cs.searchCustomers(keyword, criteria);

        // Add the search results to the model
        model.addAttribute("list", searchResults);

        // Return the view to display the search results
        return "CustomerList"; 
    }
}
