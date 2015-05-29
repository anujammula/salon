package com.salon.controller;

/**
 * 
 */

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import com.salon.SalonApplication;
import com.salon.dao.CustomerRepository;
import com.salon.dao.VisitRepository;
import com.salon.json.DateTimeTypeConverter;
import com.salon.model.Customer;
import com.salon.model.Customer.MemberType;
import com.salon.model.DiscountRate;
import com.salon.model.Visit;

/**
 * @author Anu Jammula
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalonApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class VisitControllerTest {

	@Autowired
	VisitRepository visitRepo;
	@Autowired
	CustomerRepository custRepo;
	@Autowired
	DiscountRate discountRate;

	Customer a, b, c, d, e, f;

	Visit a1, a2, b1, b2, c1, c2, d1, d2, e1, e2, f1;

	Gson gson;

	@Value("${local.server.port:8080}")
	int port;

	@Before
	public void setUp() {
		RestAssured.config = RestAssuredConfig.newConfig().jsonConfig(jsonConfig().numberReturnType(NumberReturnType.BIG_DECIMAL));
				
		gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).setPrettyPrinting().create();

		a = new Customer("aaa");
		b = new Customer("bbb", true, MemberType.PREMIUM);
		c = new Customer("CCC", true, MemberType.GOLD);
		d = new Customer("ddd", true, MemberType.SILVER);
		e = new Customer("eee", true, MemberType.GOLD);
		f = new Customer("fff", true, MemberType.PREMIUM);

		visitRepo.deleteAll();
		custRepo.deleteAll();

		DateTime visitTime = new DateTime();

		a1 = new Visit("aaa", visitTime, 10.00, 200.00);
		a1.setCustomer(a);

		// Adding a minute time between consecutive visits, so that the VisitId (name, date) will be unique
		visitTime = new DateTime(visitTime.plusMinutes(1));
		a2 = new Visit("aaa", visitTime, 150.00, 20.00);
		a2.setCustomer(a);

		visitTime = new DateTime(visitTime.plusMinutes(1));
		b1 = new Visit("bbb", visitTime, 100.00, 200.00);
		b1.setCustomer(b);

		visitTime = new DateTime(visitTime.plusMinutes(1));
		b2 = new Visit("bbb", visitTime, 300.00, 400.00);
		b2.setCustomer(b);

		visitTime = new DateTime(visitTime.plusMinutes(1));
		c1 = new Visit("CCC", visitTime, 100.00, 200.00);
		c1.setCustomer(c);
		
		visitTime = new DateTime(visitTime.plusMinutes(1));
		c2 = new Visit("CCC", visitTime, 300.00, 400.00);
		c2.setCustomer(c);

		visitTime = new DateTime(visitTime.plusMinutes(1));
		d1 = new Visit("ddd", visitTime, 100.00, 200.00);
		d1.setCustomer(d);
		
		visitTime = new DateTime(visitTime.plusMinutes(1));
		d2 = new Visit("ddd", visitTime, 300.00, 400.00);
		d2.setCustomer(d);

		visitTime = new DateTime(visitTime.plusMinutes(1));
		e1 = new Visit("eee", visitTime);
		e1.setCustomer(e);
		
		visitTime = new DateTime(visitTime.plusMinutes(1));
		e2 = new Visit("eee", visitTime);
		e2.setCustomer(e);

		// negative Test cases
		f1 = new Visit("fff", null, 300.00, 400.00);
		f1.setCustomer(f);

		RestAssured.port = port;
	}

	@Test
	public void canAddVisitsForCustA() {
		custRepo.save(a);
		addVisits(a1);
		addVisits(a2);
		fetchVisit(a1);
		fetchVisit(a2);
		fetchAllVisitsForCust(a, a1, a2);
	}

	@Test
	public void canAddVisitsForCustB() {
		custRepo.save(b);
		addVisits(b1);
		addVisits(b2);
		fetchVisit(b1);
		fetchVisit(b2);
		fetchAllVisitsForCust(b, b1, b2);
	}

	@Test
	public void canAddVisitsForCustC() {
		custRepo.save(c);
		addVisits(c1);
		addVisits(c2);
		fetchVisit(c1);
		fetchVisit(c2);
		fetchAllVisitsForCust(c, c1, c2);
	}

	@Test
	public void canAddVisitsForCustD() {
		custRepo.save(d);
		addVisits(d1);
		addVisits(d2);
		fetchVisit(d1);
		fetchVisit(d2);
		fetchAllVisitsForCust(d, d1, d2);
	}

	@Test
	public void canAddVisitsForCustE() {
		custRepo.save(e);
		addVisits(e1);
		addVisits(e2);
		fetchVisit(e1);
		fetchVisit(e2);
		fetchAllVisitsForCust(e, e1, e2);
	}

	@Test
	public void failWithBadRequest_addVisitsForCust() {// JSON input is empty
		addVisit(null, HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void failWithBadRequest_addVisitsForCustF() {// date field is NULL
		addVisit(f1, HttpStatus.SC_BAD_REQUEST);
	}

	private void addVisits(Visit visit) {
		addVisit(visit, HttpStatus.SC_OK);
	}

	private void addVisit(Visit visit, int expectedHttpStatus) {
		given().contentType("application/json").and().body(gson.toJson(visit)).when().post("/visits").then().statusCode(expectedHttpStatus);
	}

	private void fetchVisit(Visit visit) {
		when().
			get("/visits/{name}/{date}", visit.getId().getName(), visit.getId().getDate().toString(VisitController.DATE_FORMAT)).
		then().statusCode(HttpStatus.SC_OK).and().
			body("id.name", is(visit.getId().getName())).and().
			body("serviceExpense", closeTo(new BigDecimal(visit.getServiceExpense()), new BigDecimal(0.0))).and().
			body("productExpense", closeTo(new BigDecimal(visit.getProductExpense()), new BigDecimal(0.0))).and().
			body("totalExpense", closeTo(new BigDecimal(visit.getTotalExpense()), new BigDecimal(0.0))).and().
			body("finalBill", closeTo(new BigDecimal(getFinalBill(visit)), new BigDecimal(0.0))).and();
	}

	private void fetchAllVisitsForCust(Customer customer, Visit expectingV1, Visit expectingV2) {
		String name = customer.getName();

		when().
			get("/visits/{name}", name).
		then().
			statusCode(HttpStatus.SC_OK).and().
		body("id.name", hasItems(expectingV1.getId().getName(), expectingV2.getId().getName())).and().
		body("serviceExpense", contains(
				closeTo(new BigDecimal(expectingV1.getServiceExpense()), new BigDecimal(0.0))
				, closeTo(new BigDecimal(expectingV2.getServiceExpense()), new BigDecimal(0.0))
				)).and().
		body("productExpense", contains(
				closeTo(new BigDecimal(expectingV1.getProductExpense()), new BigDecimal(0.0))
				, closeTo(new BigDecimal(expectingV2.getProductExpense()), new BigDecimal(0.0))
				)).and().
		body("totalExpense", contains(
				closeTo(new BigDecimal(expectingV1.getTotalExpense()), new BigDecimal(0.0))
				, closeTo(new BigDecimal(expectingV2.getTotalExpense()), new BigDecimal(0.0))
				)).and().
		body("finalBill", contains(
				closeTo(new BigDecimal(getFinalBill(expectingV1)), new BigDecimal(0.0))
				, closeTo(new BigDecimal(getFinalBill(expectingV2)), new BigDecimal(0.0))
				)).and();
	}

	private double getFinalBill(Visit v) {
		if(!StringUtils.isEmpty(v)){
			return v.getServiceExpense() * 
					discountRate.getServiceDiscountRate(v.getCustomer().getMemberType()) 
					+ v.getProductExpense() * 
					discountRate.getProductDiscountRate(v.getCustomer().getMemberType());
		}
		else return 0.0;
	}

	@Test
	public void canFetchAll() {
		custRepo.save(Arrays.asList(a, b, c, d, e, f));
		addVisits(a1);
		addVisits(a2);
		addVisits(b1);
		addVisits(b2);
		addVisits(c1);
		addVisits(c2);
		addVisits(d1);
		addVisits(d2);
		addVisits(e1);
		addVisits(e2);
		
		when().
			get("/visits").
		then().
			statusCode(HttpStatus.SC_OK).and().
			body("id.name", hasItems("aaa", "bbb", "aaa", "bbb", "CCC", "CCC", "ddd", "ddd", "eee", "eee"));
	}

	@Test
	public void canDeleteCustDVisit1() {
		custRepo.save(d);
		addVisits(d1);
		fetchVisit(d1);
		String name = d1.getId().getName();
		String date = d1.getId().getDate().toString(VisitController.DATE_FORMAT);

		when().delete("/visits/{name}/{date}", name, date).then().statusCode(HttpStatus.SC_OK);

		when().get("/visits/{name}/{date}", name, date).then().statusCode(HttpStatus.SC_NOT_FOUND);

		// Customer should be left intact
		when().
			get("/customers/{name}", name).
		then().
			statusCode(HttpStatus.SC_OK).and().
			body("name", is(name)).and().
			body("member", is(true)).and().
			body("memberType", hasToString((MemberType.SILVER).toString()));
	}

	@Test
	public void canUpdateCustAVisit1_UpdateServiceExpense() {
		custRepo.save(a);
		addVisits(a1);
		addVisits(a2);
		a1.setServiceExpense(900);
		String name = a1.getId().getName();
		String date = a1.getId().getDate().toString(VisitController.DATE_FORMAT);

		given().
			contentType("application/json").and().
			body(gson.toJson(a1)).
		when().
			put("/visits/{name}/{date}", name, date).
		then().statusCode(HttpStatus.SC_OK);

		fetchVisit(a1);
	}

}
