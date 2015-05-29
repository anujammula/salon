# beauty-salon
Beauty Salon


A discount system for a beauty saloon, which provides services and sells beauty products. It offers 3 types of memberships: Premium, Gold and Silver. Premium, gold and silver members receive a discount of 20%, 15%, and 10%, respectively, for all services provided. Customers without membership receive no discount. All members receives a flat 10% discount on products purchased (this might change in future). Your system shall consist of three classes: Customer, Discount and Visit, as shown in the class diagram. It shall compute the total bill if a customer purchases $x of products and $y of services, for a visit. Also write a test program to exercise all the classes.

## Assumptions: 
There was a MAJOR business rule decision that needed to be made : Assuming that the membership options can change anytime...
* Option A: The discounts will be calculated based on the CURRENT MEMBERSHIP at the time the final bill was being calculated.
* Option B: The discounts will be calculated based on the MEMBERSHIP at the time visit was recorded in the system. If the MEMBERSHIP option changes later on, a Visit's final bill will not change.
* Option C: Some combination of Option A and Option B. For example, the final bill will be calculated at end of every billing cycle, and the discounts are calculated based on the MEMBERSHIP at the end of billing cycle.

I chose option A for this program.

