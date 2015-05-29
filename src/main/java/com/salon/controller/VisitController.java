/**
 * 
 */
package com.salon.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.salon.exceptions.BadRequestException;
import com.salon.exceptions.NotFoundException;
import com.salon.model.Customer;
import com.salon.model.Visit;
import com.salon.service.CustomerService;
import com.salon.service.VisitService;

/**
 * @author Anu Jammula
 *
 */
@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VisitController
{
	
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss:SSSZ";
	
    @Autowired
    private VisitService visitService;
    @Autowired
    private CustomerService custService;
	
    @RequestMapping(value="/visits", method = RequestMethod.GET)
    public List<Visit> getAllVisits()
    {
        Iterable<Visit> all = visitService.findAll();
        if(StringUtils.isEmpty(all))
        	throw new NotFoundException( "Visits NOT FOUND" );
        return Lists.newArrayList( all );
    }
 
    @RequestMapping(value="/visits/{name}", method = RequestMethod.GET)
    public List<Visit> getVisits( @PathVariable("name") String name )
    {
    	Iterable<Visit> all = visitService.findByName( name );
    	if(StringUtils.isEmpty(all))
        	throw new NotFoundException( "Visits with name " + name + " NOT FOUND" );
        return Lists.newArrayList( all );
    }
    
    @RequestMapping(value="/visits/{name}/{date}", method = RequestMethod.GET)
    public Visit getVisit( @PathVariable("name") String name, @PathVariable("date") @DateTimeFormat(pattern=DATE_FORMAT) DateTime date )
    {
        Visit visit = visitService.findByNameDate( name, date );
        
        if(StringUtils.isEmpty(visit))
    		throw new NotFoundException( "Visit with name " + name + " and Date " + date + " NOT FOUND" );

    	return visit;
    }
    
    @RequestMapping(value="/visits", method=RequestMethod.POST)
    public Visit addVisit( @RequestBody Visit visit )
    {
		if ( isEmpty(visit) ){
			throw new BadRequestException( "Visit itself, Visit.name, and Visit.date cannot be NULL/EMPTY" );
		}
		
    	return saveVisit(visit);
    }
    
    @RequestMapping(value="/visits/{name}/{date}", method=RequestMethod.PUT)
    public Visit updateVisit( @PathVariable("name") String name, @PathVariable("date") @DateTimeFormat(pattern=DATE_FORMAT) DateTime date, @RequestBody Visit visit )
    {
    	Visit existingVisit = visitService.findByNameDate( name, date );

    	if ( StringUtils.isEmpty(existingVisit) )
    		throw new NotFoundException( "Visit with name " + name + " and Date " + date + " NOT FOUND" );
    	
		if ( isEmpty(visit) )
			throw new BadRequestException( "Visit itself, Visit.name, and Visit.date cannot be NULL/EMPTY" );
		
    	if( !existingVisit.getId().equals(visit.getId()) )
			throw new BadRequestException( "Path parameters, name and date [" + name + " " + date + "] MUST match the JSON input's name and date [" + visit.getId().getName() + " " + visit.getId().getDate() + "]" );
    	
    	return saveVisit(visit);
    }

	private Visit saveVisit(Visit visit){
		Customer customer = custService.findById(visit.getId().getName());
		if ( StringUtils.isEmpty(customer) ){
			throw new NotFoundException( "Customer with name " + visit.getId().getName() + " NOT FOUND" );
		}

    	visit.setCustomer(customer);
    	
    	return visitService.save( visit );
	}

	private boolean isEmpty(Visit visit) {
		return StringUtils.isEmpty(visit) || StringUtils.isEmpty(visit.getId()) || StringUtils.isEmpty(visit.getId().getName()) || StringUtils.isEmpty(visit.getId().getDate());
	}
    
    @RequestMapping(value="/visits/{name}/{date}", method=RequestMethod.DELETE)
    public void deleteVisit( @PathVariable("name") String name, @PathVariable("date") @DateTimeFormat(pattern=DATE_FORMAT) DateTime date )
    {
    	Visit existingVisit = visitService.findByNameDate( name, date );
    	
    	if ( StringUtils.isEmpty(existingVisit) )
    		throw new NotFoundException( "Visit with name " + name + " and Date " + date + " NOT FOUND" );
    	
    	visitService.delete( existingVisit );
    }
    
}
