/**
 * 
 */
package com.salon.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.salon.exceptions.BadRequestException;
import com.salon.exceptions.DataConflictException;
import com.salon.exceptions.NotFoundException;
import com.salon.model.Customer;
import com.salon.service.CustomerService;

/**
 * @author Anu Jammula
 *
 */
@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController
{
    @Autowired
    private CustomerService custService;
 
    @RequestMapping(value="/customers", method = RequestMethod.GET)
    public List<Customer> getAllCustomers()
    {
        Iterable<Customer> all = custService.findAll();
        if(all == null)
    		throw new NotFoundException("Customers NOT FOUND");
        return Lists.newArrayList( all );
    }
 
    @RequestMapping(value="/customers/{name}", method = RequestMethod.GET)
    public Customer getCustomer( @PathVariable("name") String name )
    {
    	Customer customer = custService.findById( name );
    	if(customer == null)
    		throw new NotFoundException("Customer with name " + name + " NOT FOUND");
        return customer;
    }
 
    @RequestMapping(value="/customers", method=RequestMethod.POST)
    public Customer addCustomer( @RequestBody Customer customer )
    {
    	if ( StringUtils.isEmpty(customer) || StringUtils.isEmpty(customer.getName() ) )
    		throw new BadRequestException( "Customer's name cannot be NULL/EMPTY" );
    	
    	return saveCustomer(customer);
    }
    
    @RequestMapping(value="/customers/{name}", method=RequestMethod.PUT)
    public Customer updateCustomer( @PathVariable("name") String name, @RequestBody Customer customer )
    {
    	Customer existingCust = custService.findById( name );
    	if ( StringUtils.isEmpty(existingCust) )
    		throw new NotFoundException( "Customer with name " + name + " NOT FOUND" );
    	
    	if ( StringUtils.isEmpty(customer) || StringUtils.isEmpty(customer.getName() ) )
    		throw new BadRequestException( "Customer's name cannot be NULL/EMPTY" );
    	
    	if( !existingCust.getName().equals(customer.getName()) ){
			throw new BadRequestException( "Path parameter, name [" + name + "] MUST match the JSON input's name [" + customer.getName() + "]" );
		}
    	
    	return saveCustomer(customer);
    }

	private Customer saveCustomer(Customer customer) {
    	if(customer.isMember() && StringUtils.isEmpty(customer.getMemberType()))
    		throw new DataConflictException( "Data integrity violation. memberType was Empty. It Must be one of these ['PREMIUM', 'GOLD', 'SILVER']." );
    	
    	if(!customer.isMember() && !StringUtils.isEmpty(customer.getMemberType()))
    		throw new DataConflictException( "Data integrity violation. member was false. It Must be 'true' when memberType is NOT EMPTY." );
    	
        return custService.save( customer );
	}

    @RequestMapping(value="/customers/{name}", method=RequestMethod.DELETE)
    public void deleteCustomer( @PathVariable("name") String name )
    {
    	Customer existingCust = custService.findById( name );
    	if ( StringUtils.isEmpty(existingCust) )
    		throw new NotFoundException( "Customer with name " + name + " NOT FOUND" );
    	custService.delete( name );
    }

}