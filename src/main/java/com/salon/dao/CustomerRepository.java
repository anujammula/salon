/**
 * 
 */
package com.salon.dao;

import org.springframework.data.repository.CrudRepository;

import com.salon.model.Customer;

/**
 * @author Anu Jammula
 *
 */

public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findByName(String name);
    
}
