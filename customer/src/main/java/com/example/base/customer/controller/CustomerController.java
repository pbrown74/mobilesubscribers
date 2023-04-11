package com.example.base.customer.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.base.customer.model.Customer;
import com.example.base.customer.service.CustomerService;

/**
 * This class is just delegating to the service layer, its here to define the mappings for REST endpoints only
 * @author User
 *
 */

@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
    private CustomerService custService;

	// add a customer to the database
	@RequestMapping(value="/customers", method=RequestMethod.POST)
	public Customer createCustomer(@RequestBody Customer customer) {
	    return custService.createCustomer(customer);
	}

	// change a customers details
	@RequestMapping(value="/customers/{custId}", method=RequestMethod.PUT)
	public Customer updateCustomer(@PathVariable(value = "custId") Long id, @RequestBody Customer custDetails) {
	    return custService.updateCustomer(id, custDetails);
	}

	// delete a customer from the database
	@RequestMapping(value="/customers/{custId}", method=RequestMethod.DELETE)
	public void deleteCustomer(@PathVariable(value = "custId") Long id) {
		custService.deleteCustomer(id);
	}

	@RequestMapping(value="/customers", method=RequestMethod.GET)
	public List<Customer> getCustomers() {
	    return custService.getCustomers();
	}

	// return all mobile numbers details that match a criteria
	@RequestMapping(value="/customers/find/{idCard}", method=RequestMethod.GET)
	public List<Customer> findCustomerByIdCard(@PathVariable(value = "idCard") String idCard) {
		return custService.findCustomersByIdCard(idCard);
	}

}
