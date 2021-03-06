# beauty-salon
Beauty Salon


A discount system for a beauty saloon, which provides services and sells beauty products. It offers 3 types of memberships: Premium, Gold and Silver. Premium, gold and silver members receive a discount of 20%, 15%, and 10%, respectively, for all services provided. Customers without membership receive no discount. All members receives a flat 10% discount on products purchased (this might change in future). Your system shall consist of three classes: Customer, Discount and Visit, as shown in the class diagram. It shall compute the total bill if a customer purchases $x of products and $y of services, for a visit. Also write a test program to exercise all the classes.

## Assumptions/Busines rules followed: 
1. Customer entity's primary Key is 'name'
2. A Customer entity may have 0 or more Visit entities associated with it.
3. Visit entity's primary key is Composite [name (foreign key from customer Entity) + date].
4. Joda API's DateTime object was chosen over java.util.Date as it is less error prone

There was a MAJOR business rule decision that needed to be made : Assuming that the membership options can change anytime...
* Option A: The discounts will be calculated based on the CURRENT MEMBERSHIP at the time a visit Entity is retrieved.
* Option B: The discounts will be calculated based on the MEMBERSHIP at the time visit was recorded in the system. If the MEMBERSHIP option changes later on, a Visit's final bill will not change.
* Option C: Some combination of Option A and Option B. For example, the final bill will be calculated at end of every billing cycle, and the discounts are calculated based on the MEMBERSHIP at the end of billing cycle.

I chose option A for this program.

## Tools used:
1. Spring Boot Framework
2. Hibernate
3. Embedded HSQL database

## How to run this program
1. Clone/Download the project into your workspace
2. Import it as an existing Maven project into Eclipse
3. Right click on the project --> Run As --> Java Application.
4. Eclipse will search for the "main" class and show a diagog box with "SalonApplication - com.salon"
5. Select that and clisk OK.
6. Embedded Tomcat server will start on port 8080 using embedded HSQL database.
7. Using your favorite HTTP REST client you can access following REST end points.
    *   GET http://localhost:8080/customers --> returns all Customers
    *   GET http://localhost:8080/customers/{name} --> returns a single Customer matching the name
    *   POST http://localhost:8080/customers --> Adds a Customer (JSON input)
      {
"name": "aaa",
"member": "true",
"memberType": "PREMIUM"
}
    *   PUT http://localhost:8080/customers/{name} --> Updates a Customer matching the name (JSON input)
    *   DELETE http://localhost:8080/customers/{name} --> Deletes a Customer matching the name

    *   GET http://localhost:8080/visits/ --> returns all Visits for all Customers
    *   GET http://localhost:8080/visits/{name} --> returns all Visits for a single Customers matching the name
    *   GET http://localhost:8080/visits/{name}/{date} --> returns a single Visit for a single Customer matching the name and date of visit
    *   POST http://localhost:8080/visits --> Adds a Visit (JSON input)
{
  "id": {
    "name": "aaa",
    "date": "2015-05-29T05:35:13.887-05:00"
  },
  "serviceExpense": 10.0,
  "productExpense": 200.0,
  "finalBill": 0.0
}
    *   PUT http://localhost:8080/visits/{name}/{date} --> Updates a single Visit for a single Customer matching the name and date of visit (JSON input)
    *   DELETE http://localhost:8080/visits/{name}/{date} --> Deletes a single Visit for a single Customer matching the name and date of visit
    
8. Unit/Integration test cases can be executed by Right cliking on src/test/java folder in Eclipse --> Run As --> JUnit Test

