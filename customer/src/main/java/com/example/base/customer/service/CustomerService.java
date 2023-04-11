package com.example.base.customer.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base.customer.exception.BadIdCardFormatException;
import com.example.base.customer.exception.DuplicateCustomerIdCardException;
import com.example.base.customer.exception.UnknownCustomerException;
import com.example.base.customer.model.Customer;
import com.example.base.customer.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository custRepository;
    private final static List<String> IDCARD_TYPES = Arrays.asList("M","G","A","P","L","H","B","Z");
    private Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Transactional
    public Customer createCustomer(Customer customer) {
		String idCard = customer.getIdCard();
		validateIdCard(idCard);
		List<Customer> cust = findCustomersByIdCard(idCard);
		if(!cust.isEmpty()) {
	    	logger.error("createCustomer called for existing customer idCard");
			throw new DuplicateCustomerIdCardException();
		}
		if(logger.isDebugEnabled()) {
	    	logger.debug("customer did not exist already ,will save customer");
		}
        return custRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return custRepository.findAll();
    }

    public List<Customer> findCustomersByIdCard(String idCard) {
        return custRepository.findByIdCard(idCard);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return custRepository.findById(id);
    }

    @Transactional
    public void deleteCustomer(Long custId) {
    	logger.info("deleteCustomer called for custId: " + Long.toString(custId));
		Optional<Customer> cust = findCustomerById(custId);
		if(cust.isEmpty()) {
	    	logger.error("deleteCustomer called for non-existing customer");
			throw new UnknownCustomerException();
		}
		if(logger.isDebugEnabled()) {
			logger.debug("deleteCustomer called for existing customer, will delete customer from database");
		}
    	custRepository.deleteById(custId);
    }

    @Transactional
	public Customer updateCustomer(Long custId, Customer custDetails) {
		Optional<Customer> c = findCustomerById(custId);
		if(c.isEmpty()) {
	    	logger.error("updateCustomer called for non-existing customer");
			throw new UnknownCustomerException();
		}
    	Customer cust = custRepository.findById(custId).get();
    	cust.setAddress(custDetails.getAddress());
    	cust.setId(custDetails.getId());
    	cust.setIdCard(custDetails.getIdCard());
    	cust.setName(custDetails.getName());
    	cust.setSurname(custDetails.getSurname());
		if(logger.isDebugEnabled()) {
			logger.debug("updateCustomer called , will proceed to save customer with Id: " + Long.toString(custId));
		}
        return custRepository.save(cust);                                
    }

	// this is for unit tests
	public void setCustomerRepository(CustomerRepository repo) {
		this.custRepository = repo;
	}

	private void validateIdCard(String idCard) {
		if(!idCard.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][A-Z]")) {
	    	logger.error("Badly formatted ID Card number: " + idCard + ", must have 7 digits leading");
			throw new BadIdCardFormatException();
		}
		String lastCh = idCard.substring(idCard.length()-1);
		if(!IDCARD_TYPES.contains(lastCh)) {
	    	logger.error("Badly formatted ID Card number: " + idCard + ", last character is not valid");
			throw new BadIdCardFormatException();
		}
	}

}
