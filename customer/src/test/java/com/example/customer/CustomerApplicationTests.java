package com.example.customer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.base.customer.exception.BadIdCardFormatException;
import com.example.base.customer.exception.DuplicateCustomerIdCardException;
import com.example.base.customer.exception.UnknownCustomerException;
import com.example.base.customer.model.Customer;
import com.example.base.customer.repository.CustomerRepository;
import com.example.base.customer.service.CustomerService;
import com.example.base.mobilesubscriber.exception.BadFormatMobileException;
import com.example.base.mobilesubscriber.exception.BadServiceTypeException;
import com.example.base.mobilesubscriber.exception.DuplicateMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownSubscriberException;
import com.example.base.mobilesubscriber.model.MobileSubscriber;
import com.example.base.mobilesubscriber.repository.MobileSubscriberRepository;
import com.example.base.mobilesubscriber.service.MobileSubscriberService;
import com.example.base.mobilesubscriber.service.MobileSubscriberService.PaymentType;

/**
 * This class is here to test the validation logic we have in the service layers
 * @author User
 *
 */
class CustomerApplicationTests {

	@Test
	void testCreateCustomerAlreadyExists() {
		// setup the test
		CustomerService cs = new CustomerService();
		CustomerRepository cr = mock(CustomerRepository.class);
		cs.setCustomerRepository(cr);
		Customer c = buildCustomer(1, 2, "0657124L", "Paul", "Brown", "Triq il-Bajja, Marsaskala");
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(c);
		when(cr.findByIdCard("0657124L")).thenReturn(customers);

		try {
			// make the call to the code under test
			cs.createCustomer(c);

			Assertions.fail("Should have failed with exception");
		}
		catch(DuplicateCustomerIdCardException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with DuplicateCustomerIdCardException");
		}
	}

	@Test
	void testCreateCustomerValidId() {
		// setup the test
		CustomerService cs = new CustomerService();
		CustomerRepository cr = mock(CustomerRepository.class);
		cs.setCustomerRepository(cr);
		Customer c = buildCustomer(1, 2, "0657124L", "Paul", "Brown", "Triq il-Bajja, Marsaskala");
		List<Customer> customers = new ArrayList<Customer>();
		when(cr.findByIdCard("0657124L")).thenReturn(customers);

		// make the call to the code under test
		cs.createCustomer(c);
		
		// pass
	}

	@Test
	void testCreateCustomerInvalidId() {
		// setup the test
		CustomerService cs = new CustomerService();
		Customer c = buildCustomer(1, 2, "06512127124L", "Paul", "Brown", "Triq il-Bajja, Marsaskala");
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(c);

		try {
			// make the call to the code under test
			cs.createCustomer(c);

			Assertions.fail("Should have failed with exception");
		}
		catch(BadIdCardFormatException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with BadIdCardFormatException");
		}
	}

	@Test
	void testDeleteCustomerNonExistingId() {
		// setup the test
		CustomerService cs = new CustomerService();
		CustomerRepository cr = mock(CustomerRepository.class);
		cs.setCustomerRepository(cr);
		Customer c = buildCustomer(1, 2, "0657124L", "Paul", "Brown", "Triq il-Bajja, Marsaskala");
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(c);
		when(cr.findByIdCard("0657124L")).thenReturn(customers);

		try {
			// make the call to the code under test
			cs.deleteCustomer(0L);

			Assertions.fail("Should have failed with exception");
		}
		catch(UnknownCustomerException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with UnknownCustomerException");
		}
	}

	@Test
	void testUpdateCustomerNonExistingId() {
		// setup the test
		CustomerService cs = new CustomerService();
		CustomerRepository cr = mock(CustomerRepository.class);
		cs.setCustomerRepository(cr);
		Customer c = buildCustomer(1, 2, "0657124L", "Paul", "Brown", "Triq il-Bajja, Marsaskala");
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(c);
		when(cr.findByIdCard("0657124L")).thenReturn(customers);

		try {
			// make the call to the code under test
			cs.updateCustomer(0L, c);

			Assertions.fail("Should have failed with exception");
		}
		catch(UnknownCustomerException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with UnknownCustomerException");
		}
	}

	@Test
	void testCreateMobileSubscriberAlreadyExists() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679573765", 1L, 1L, PaymentType.PREPAID.toString(), 1L);
		List<MobileSubscriber> subs = new ArrayList<MobileSubscriber>();
		subs.add(s);

		when(sr.findByMsisdn("35679573765")).thenReturn(subs);

		
		try {
			// make the call to the code under test
			ss.createMobileSubscriber(s);

			Assertions.fail("Should have failed with exception");
		}
		catch(DuplicateMobileNumberException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with DuplicateMobileNumberException");
		}
	}

	@Test
	void testCreateMobileSubscriberInvalidMsisdn() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679111573756", 1L, 1L, PaymentType.PREPAID.toString(), 1L);

		try {
			// make the call to the code under test
			ss.createMobileSubscriber(s);

			Assertions.fail("Should have failed with exception");
		}
		catch(BadFormatMobileException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with BadFormatMobileException");
		}
	}

	@Test
	void testCreateMobileSubscriberInvalidServiceType() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679573765", 1L, 1L, "Bad Service Type", 1L);

		try {
			// make the call to the code under test
			ss.createMobileSubscriber(s);

			Assertions.fail("Should have failed with exception");
		}
		catch(BadServiceTypeException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with BadServiceTypeException");
		}
	}

	@Test
	void testFindMobileSubscriberByMsisdn() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679573765", 1L, 1L, PaymentType.PREPAID.toString(), 1L);
		List<MobileSubscriber> subs = new ArrayList<MobileSubscriber>();
		subs.add(s);

		when(sr.findByMsisdn("35679573765")).thenReturn(subs);

		try {
			// make the call to the code under test
			Optional<MobileSubscriber> sub;
			sub = ss.findByMsisdn("35679573111756");
			Assertions.assertTrue(sub.isEmpty(), "Should not have found Msisdn");
			sub = ss.findByMsisdn("35679573765");
			Assertions.assertTrue(sub.isPresent(), "Should have found Msisdn");
		}
		catch(DuplicateMobileNumberException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with DuplicateMobileNumberException");
		}
	}

	@Test
	void testAssignMobileSubscriberUserAndOwner() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679573765", 1L, 1L, PaymentType.PREPAID.toString(), 1L);

		when(sr.findById(1L)).thenReturn(Optional.empty());

		try {
			// make the call to the code under test
			ss.assignMobileSubscriberUserAndOwner(1L, s);
			
			Assertions.fail("Should have failed with UnknownSubscriberException");
		}
		catch(UnknownSubscriberException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with UnknownSubscriberException");
		}
	}

	@Test
	void testChangeMobileSubscriberPaymentPlanFromTo() {
		// setup the test
		MobileSubscriberService ss = new MobileSubscriberService();
		MobileSubscriberRepository sr = mock(MobileSubscriberRepository.class);
		ss.setMobileSubscriberRepository(sr);
		MobileSubscriber s = buildSubscriber(1, 2, "35679573765", 1L, 1L, PaymentType.PREPAID.toString(), 1L);
		List<MobileSubscriber> subs = new ArrayList<MobileSubscriber>();
		subs.add(s);

		// note the number is different
		when(sr.findByMsisdn("35679573765")).thenReturn(subs);

		try {
			// make the call to the code under test
			ss.changeMobileSubscriberPaymentPlanFromTo("3567911573756", PaymentType.POSTPAID, PaymentType.PREPAID);
			
			Assertions.fail("Should have failed with UnknownMobileNumberException");
		}
		catch(UnknownMobileNumberException e) {
			// pass
		}
		catch(Exception e) {
			Assertions.fail("Should have failed with UnknownMobileNumberException");
		}
	}

	private Customer buildCustomer(int id, long version, String idCard, String name, String surname, String address) {
		Customer c = new Customer();
		c.setId(Long.valueOf(id));
		c.setVersion(version);
		c.setIdCard(idCard);
		c.setName(name);
		c.setSurname(surname);
		c.setAddress(address);
		return c;
	}

	private MobileSubscriber buildSubscriber(int id, long version, String msisdn, Long customerIdOwner, Long customerIdUser, String serviceType, Long serviceStartDate) {
		MobileSubscriber subs = new MobileSubscriber();
		subs.setId(Long.valueOf(id));
		subs.setVersion(version);
		subs.setMsisdn(msisdn);
		subs.setCustomerIdOwner(customerIdOwner);
		subs.setCustomerIdUser(customerIdUser);
		subs.setServiceType(serviceType);
		subs.setServiceStartDate(serviceStartDate);
		return subs;
	}

}
