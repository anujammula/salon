/**
 * 
 */
package com.salon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * @author Anu Jammula
 *
 */
@Component
@Entity
public class Visit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2765427970070120984L;
	
	@EmbeddedId
    private VisitId id;

	@Column
	private double serviceExpense;
	
	@Column
	private double productExpense;
	
	@Transient
	private double finalBill;

	@ManyToOne (fetch = FetchType.EAGER) // FetchType.EAGER is good here; very rarely we want to see a Visit Info without knowing which Customer it belongs to
	@MapsId ("name")
	@JoinColumn(name = "name", nullable=false )
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Customer customer;
	
	public Visit (){}
	
	public Visit(String name, DateTime date){
		this.id = new VisitId(name, date);
	}
	
	public Visit(String name, DateTime date, double serviceExpense, double productExpense){
		this.id = new VisitId(name, date);
		this.serviceExpense = serviceExpense;
		this.productExpense = productExpense;
	}
	
	public VisitId getId() {
		return id;
	}

	public void setId(VisitId id) {
		this.id = id;
	}
	
	public double getServiceExpense() {
		return serviceExpense;
	}

	public void setServiceExpense(double serviceExpense) {
		this.serviceExpense = serviceExpense;
	}

	public double getProductExpense() {
		return productExpense;
	}

	public void setProductExpense(double productExpense) {
		this.productExpense = productExpense;
	}
	
	/*
	 * Returns total of ALL expenses BEFORE discounts are applied
	 */
	public double getTotalExpense() {
		return serviceExpense + productExpense;
	}
	
	public double getFinalBill() {
		return finalBill;
	}
	
	public void setFinalBill( double finalBill ) {
		this.finalBill = finalBill;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
/*	public String getName() {
		return id.getName();
	}*/
	
	@Override
	public String toString() {
		//I believe is it good idea to use getters here instead of the member variables, so that we can take advantage of special logic in getters,  if there is any
		return String.format( "Visit[vsitId=%s, customer='%s', serviceExpense='%f', productExpense='%f', totalExpense='%f']", getId(), customer, getServiceExpense(), getProductExpense(), getTotalExpense() );
	}
	
	public boolean equals(Object other) {
	    if(other == null)
	        return false;

	    if(!(other instanceof Visit))
	        return false;

	    if(getId() == null)
	        return false;

	    Visit otherVisit = (Visit) other;
	    if(!getId().equals(otherVisit.getId()))
	        return false;

	   return true;
	}

	public int hashcode() {
	    int hash = 3;

	    hash = 23 * hash + ((getId() != null) ? getId().hashCode() : 0);

	    return hash;
	}
    
	@Embeddable
    public static class VisitId implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8249912918949403148L;
		private String name;
		
		@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
		private DateTime date;

		protected VisitId() {
		}
		
		public VisitId(String name, DateTime date) {
			this.name = name;
			this.date = date;
		}

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public DateTime getDate() {
			return date;
		}
		
		public void setDate(DateTime date) {
			this.date = date;
		}
		
		@Override
		public String toString() {
			return String.format( "VisitId[name='%s', date='%s']", getName(), getDate() );
		}
	    
		public boolean equals(Object other) {
		    if(other == null)
		        return false;

		    if(!(other instanceof VisitId))
		        return false;

		    if(getName() == null || getDate() == null)
		        return false;

		    VisitId otherVisitId = (VisitId) other;
		    if(!getName().equals(otherVisitId.getName()))
		        return false;
		    
		    if(getDate().getMillis() != otherVisitId.getDate().getMillis())
		        return false;

		   return true;
		}

		public int hashcode() {
		    int hash = 3;

		    hash = 23 * hash + ((getName() != null) ? getName().hashCode() : 0);
		    hash = 23 * hash + ((getDate() != null) ? getDate().hashCode() : 0);

		    return hash;
		}

	}
	
}
