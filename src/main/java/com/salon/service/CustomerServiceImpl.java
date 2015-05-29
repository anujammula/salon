/**
 * 
 */
package com.salon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.dao.CustomerRepository;
import com.salon.model.Customer;

/**
 * @author Anu Jammula
 *
 */

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired private CustomerRepository repo;

	@Override
	public Customer findById(String name) {
		return repo.findOne(name);
	}

	@Override
	public Customer save(Customer customer) {
		return repo.save(customer);
	}

	@Override
	public void delete(String name) {
		repo.delete(name);		
	}

	@Override
	public Iterable<Customer> findAll() {
		return repo.findAll();
	}

}
