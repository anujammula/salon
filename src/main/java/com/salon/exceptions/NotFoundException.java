/**
 * 
 */

package com.salon.exceptions;

/**
 * @author Anu Jammula
 *
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2842156269198755467L;
	public NotFoundException (String msg){
		super(msg);
	}
}
