package com.salon.controller;

/**
 * 
 */

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.salon.SalonApplication;
import com.salon.dao.CustomerRepository;
import com.salon.model.Customer;
import com.salon.model.Customer.MemberType;


/**
 * @author Anu Jammula
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalonApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class CustomerControllerTest {

    @Autowired
    CustomerRepository repo;

    Customer a, b , c, d, e, f, g, h;

    @Value("${local.server.port:8080}")
    int port;

    @Before
    public void setUp() {
    	repo.deleteAll();

    	a = new Customer("aaa");
        b = new Customer("bbb", true, MemberType.PREMIUM);
        c = new Customer("CCC", true, MemberType.GOLD);
        d = new Customer("ddd", true, MemberType.SILVER);
        
        //negative test cases
        e = new Customer(null);//Bad Request
        f = new Customer("", true, MemberType.SILVER);//Bad request
        g = new Customer("GGG", true, null);//Data Conflict
        h = new Customer("hhh", false, MemberType.GOLD);//Data Conflict

        RestAssured.port = port;
    }

    @Test
    public void canAddCustA() {
    	addCust(a);
    }
    
    @Test
    public void canAddCustB() {
    	addCust(b);
    }
    
    @Test
    public void canAddCustC() {
    	addCust(c);
    }
    
    @Test
    public void canAddCustD() {
    	addCust(d);
    }
    
    private void addCust( Customer customer ) {
    	Gson gson = new Gson();

    	given().
			contentType("application/json").and().
			body(gson.toJson(customer)).
		when().
	            post("/customers" ).
	    then().
	            statusCode(HttpStatus.SC_OK);
    	fetchCust(customer);
    }
    
    private void fetchCust( Customer customer ) {
    	if( customer.getMemberType()!=null){ 
	    	when().
			        get("/customers/{name}", customer.getName()).
			then().
			        statusCode(HttpStatus.SC_OK).and().
			        body("name", is(customer.getName())).and().
			        body("memberType", hasToString(customer.getMemberType().toString() ) );
    	}else{
    		when().
			        get("/customers/{name}", customer.getName()).
			then().
			        statusCode(HttpStatus.SC_OK).and().
			        body("name", is(customer.getName())).and();
    	}
    }
    
    @Test
    public void canFetchAll() {
    	addCust(a);
    	addCust(b);
    	addCust(c);
    	addCust(d);
        when().
                get("/customers").
        then().
                statusCode(HttpStatus.SC_OK).and().
                body("name", hasItems("aaa", "bbb", "CCC", "ddd"));
    }

    @Test
    public void canDeleteCustD() {
    	canFetchAll();

        when().
                delete("/customers/{name}", d.getName()).
        then().
                statusCode(HttpStatus.SC_OK);
        
        when().
		        get("/customers/{name}", d.getName()).
		then().
		        statusCode(HttpStatus.SC_NOT_FOUND);
        when().
		        get("/customers").
		then().
		        statusCode(HttpStatus.SC_OK).and().
		        body("name", hasItems("aaa", "bbb", "CCC"));
    }
    

    
    @Test
    public void canUpdateCustA_addMemebrship() {
    	addCust(a);
    	a.setMember(true);
    	a.setMemberType(MemberType.GOLD);
    	Gson gson = new Gson();
    	String name = a.getName();

    	given().
    		contentType("application/json").and().
    		body(gson.toJson(a)).
    	when().
                put("/customers/{name}", name).
        then().
                statusCode(HttpStatus.SC_OK);
    	
    	when().
        		get("/customers/{name}", name).
        then().
        		statusCode(HttpStatus.SC_OK).and().
        		body("member", is(true)).
        and().
        		body( "memberType", hasToString((MemberType.GOLD).toString()));
    }
    
    @Test
    public void canUpdateCustB_changeMemebrshipToSilver() {
    	addCust(b);
    	b.setMemberType(MemberType.SILVER);
    	Gson gson = new Gson();
    	String name = b.getName();

    	given().
    		contentType("application/json").and().
    		body(gson.toJson(b)).
    	when().
                put("/customers/{name}", name).
        then().
                statusCode(HttpStatus.SC_OK);
    	
    	when().
        		get("/customers/{name}", name).
        then().
        		statusCode(HttpStatus.SC_OK).and().
        		body("member", is(true)).
        and().
        		body( "memberType", hasToString((MemberType.SILVER).toString()));
    }
    
    @Test
    public void shouldFailEWithBadRequest() {
    	
    	Gson gson = new Gson();
    	given().
    		contentType("application/json").and().
    		body(gson.toJson(e)).
    	when().
                post("/customers").
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    public void shouldFailAddingCustFWithBadRequest() {
    	
    	Gson gson = new Gson();
    	given().
    		contentType("application/json").and().
    		body(gson.toJson(f)).
    	when().
                post("/customers").
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    @Test
    public void shouldFailAddingCustGWithDataConflict() {
    	
    	Gson gson = new Gson();
    	given().
    		contentType("application/json").and().
    		body(gson.toJson(g)).
    	when().
                post("/customers").
        then().
                statusCode(HttpStatus.SC_CONFLICT);
    }
    
    @Test
    public void shouldFailAddingCustHWithDataConflict() {
    	
    	Gson gson = new Gson();
    	given().
    		contentType("application/json").and().
    		body(gson.toJson(h)).
    	when().
                post("/customers").
        then().
                statusCode(HttpStatus.SC_CONFLICT);
    }
    
}
