/**
 * 
 */
package com.salon.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.salon.dao.VisitRepository;
import com.salon.model.DiscountRate;
import com.salon.model.Visit;
import com.salon.model.Visit.VisitId;

/**
 * @author Anu Jammula
 *
 */

@Service
public class VisitServiceImpl implements VisitService {

	@Autowired
	private VisitRepository repo;
	
	@Autowired
	private DiscountRate discountRate;

	@Override
	public Visit save(Visit visit) {
		return repo.save(visit);
	}

	@Override
	public void delete(Visit visit) {
		repo.delete(visit);
	}

	@Override
	public Iterable<Visit> findAll() {
		
		return setDiscountRates( repo.findAll() );
	}

	@Override
	public Iterable<Visit> findByName(String name) {
		return setDiscountRates ( repo.findByIdName(name) );
	}

	@Override
	public Visit findByNameDate(String name, DateTime date) {
		return setDiscountRates( repo.findOne(new VisitId(name, date)) );
	}
	
	private Visit setDiscountRates(Visit v){
		if(!StringUtils.isEmpty(v)){
			v.setFinalBill( v.getServiceExpense() * 
					discountRate.getServiceDiscountRate(v.getCustomer().getMemberType()) 
					+ v.getProductExpense() * 
					discountRate.getProductDiscountRate(v.getCustomer().getMemberType()));
		}
		
		return v;
	}
	
	private Iterable<Visit> setDiscountRates(Iterable<Visit> visits){
		for (Visit v : visits){
			setDiscountRates(v);
		}
		
		return (Iterable<Visit>) visits;
	}
	
}
