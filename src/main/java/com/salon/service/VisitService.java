/**
 * 
 */
package com.salon.service;

import org.joda.time.DateTime;

import com.salon.model.Visit;

/**
 * @author Anu Jammula
 *
 */

public interface VisitService {
    Visit save(Visit visit);
    void delete(Visit visit);
    Iterable<Visit> findAll();
    Visit findByNameDate(String name, DateTime date);
    Iterable<Visit> findByName(String name);
}
