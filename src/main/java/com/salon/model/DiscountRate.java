/**
 * 
 */
package com.salon.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.salon.model.Customer.MemberType;

/**
 * @author Anu Jammula
 *
 * 
 * Suggestion for PO : how about calling premium membership a "platinum" membership to keep the precious metal theme?
 */
@Component
public final class DiscountRate {
	
	//As the discount rates may change in the future, it makes sense to make them configurable via application.properties.
	//However the properties are optional until we need need to deviate from the below default values.
	@Value( "${app.serviceDiscountPremium:0.2}" ) private double serviceDiscountPremium;
	@Value( "${app.serviceDiscountGold:0.15}" ) private double serviceDiscountGold;
	@Value( "${app.serviceDiscountSilver:0.1}" ) private double serviceDiscountSilver;
	@Value( "${app.productDiscountPremium:0.1}" ) private double productDiscountPremium;;
	@Value( "${app.productDiscountGold:0.1}" ) private double productDiscountGold;
	@Value( "${app.productDiscountSilver:0.1}" ) private double productDiscountSilver;
	
	public double getServiceDiscountRate(MemberType memberType){
		if(memberType == null)
			return 0;
		switch(memberType){
			case PREMIUM :
				return serviceDiscountPremium;
			case GOLD :
				return serviceDiscountGold;
			case SILVER :
				return serviceDiscountSilver;
			default :
				return 0;
		}
	}
	
	public double getProductDiscountRate(MemberType memberType){
		if(memberType == null)
			return 0;
		switch(memberType){
			case PREMIUM :
				return productDiscountPremium;
			case GOLD :
				return productDiscountGold;
			case SILVER :
				return productDiscountSilver;
			default :
				return 0;
		}
	}
	
}
