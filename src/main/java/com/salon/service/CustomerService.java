/**
 * 
 */
package com.salon.service;

import com.salon.model.Customer;

/**
 * @author Anu Jammula
 *
 */

public interface CustomerService {
	
	Customer save(Customer customer);
    void delete(String name);
    Iterable<Customer> findAll();
    Customer findById(String id);
    
}

