/**
 * 
 */
package com.salon.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author Anu Jammula
 *
 */
@Entity
public class Customer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8301762252812962818L;

	@Id
	@Column (unique = true, length = 500) //Default length 255 may not be sufficient
	private String name; // A specific Id field was not provided in the proposed class diagram. So assuming that the name will be the Unique Id....although it is not a best practice to rely on a property/properties of the Entity for uniqueness.
	
	@Column
	private boolean member;
	
	@Column	(name = "MEMBER_TYPE")
	private MemberType memberType; //I believe making the memeberType into enum is a better, cleaner, less error prone implementation
	
	@OneToMany (mappedBy="customer", orphanRemoval=true, fetch = FetchType.LAZY, cascade={ CascadeType.MERGE,
            CascadeType.REMOVE, CascadeType.REFRESH })//{CascadeType.PERSIST, CascadeType.REMOVE})// FetchType.LAZY is good here; We retrieve Customer Info more often than their Visits.
	private transient Set<Visit> visits = new HashSet<Visit>();

	public Customer (){}
	
	public Customer (String name){
		this.name = name;
	}
	
	public Customer (String name, boolean member, MemberType memeberType){
		this.name = name;
		this.member = member;
		this.memberType = memeberType;
	}
	
	public String getName() {
		return name;
	}

	public boolean isMember() {
		return member;
	}
	
	public void setMember(boolean member) {
		this.member = member;
/*		if( !member ) {//Confirm with PO/DBA : Can a Customer have Membership information (may be old membership info) but currently NOT A MEMEBER?
			memberType = null;
		}*/
	}
	
	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType( MemberType memberType ) {
		this.memberType = memberType;
	}
	
	public Set<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}
	
	public void addVisit(Visit visit) {
		visit.setCustomer(this);
		visits.add(visit);
	}
	
	@Override
	public String toString() {
		//I believe is it good idea to use getters here instead of the member variables, so that we can take advantage of special logic in getters,  if there is any
		return String.format( "Customer[name='%s', member='%s', memberType='%s']", getName(), isMember(), getMemberType() );
	}
	
	public boolean equals(Object other) {
	    if(other == null)
	        return false;

	    if(!(other instanceof Customer))
	        return false;

	    if(getName() == null)
	        return false;

	    Customer otherCust = (Customer) other;
	    if(!getName().equals(otherCust.getName()))
	        return false;

	   return true;
	}

	public int hashcode() {
	    int hash = 3;

	    hash = 23 * hash + ((getName() != null) ? getName().hashCode() : 0);

	    return hash;
	}

	public enum MemberType{ PREMIUM, GOLD, SILVER };
}
