/**
 * 
 */
package com.salon.dao;

import org.springframework.data.repository.CrudRepository;

import com.salon.model.Visit;
import com.salon.model.Visit.VisitId;

/**
 * @author Anu Jammula
 *
 */

public interface VisitRepository extends CrudRepository<Visit, VisitId> {
	
    Iterable<Visit> findByIdName(String name);
    
}

