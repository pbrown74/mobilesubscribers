

README.txt


************************************************************************************************************************************

1) To install the docker images which you find in the Github alongside the code:

These images were built and tested on a Win64 notebook running Windows 10 Pro (virtualisation enabled in BIOS).

docker load < mysql-image.tar
docker load < mobile-subscribers-image.tar

docker run --name mysqldb --network mobile-subscribers-mysql-net -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=customer-schema -d mysql
docker run --network mobile-subscribers-mysql-net --name mobile-subscribers-mysql-container -p 8080:8080 mobile-subscribers

I used the latest versions of all software. I used Docker Desktop to manage the containers more easily. I had to pause the MySQL
service running locally to be able to use the 3306 port from the MySQL container. I have set up two Docker containers and put them
in a network so they can communicate.


************************************************************************************************************************************

2) Once the containers are running, to browse the services via Swagger:

http://localhost:8080/swagger-ui/index.html

When you are initially asked to authenticate the username is 'User' and the password will be logged by SpringBOOT in the console that is running
the mobile-susbcribers container. For example, you will see something like this:

  Using generated security password: c9ff531a-443d-46ee-933e-6272f38859ad

Once you enter the password, and pass through the basic authentication, subsequent REST API calls are not authenticated again.

These commands are useful to find the logs in the docker container:

  docker ps                   -- this will give you the Container ID of the Spring BOOT server (mobile-subscribers), eg. 09611f75f157
  docker logs 09611f75f157    -- this will show you the contents of the log file
  


************************************************************************************************************************************

3) REST end points:

a) getCustomers - fetch all the customers in the database
http://localhost:8080/swagger-ui/index.html#/customer-controller/getCustomersUsingGET

b) createCustomer - add a customer into the database
 http://localhost:8080/swagger-ui/index.html#/customer-controller/createCustomerUsingPOST

c) updateCustomer - update an existing customer in the database
http://localhost:8080/swagger-ui/index.html#/customer-controller/updateCustomerUsingPUT

d) deleteCustomer - remove a customer from the database
http://localhost:8080/swagger-ui/index.html#/customer-controller/deleteCustomerUsingDELETE

e) findCustomerByIdCard - find a customer using idCard parameter
http://localhost:8080/swagger-ui/index.html#/customer-controller/findCustomerByIdCardUsingGET

f) getMobileSubscribers - get all mobile subscribers
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/getMobileSubscribersUsingGET

g) createMobileSubscriber - add in a mobile subscriber to the database
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/createMobileSubscriberUsingPOST

h) assignMobileSubscriberUserAndOwner - assign a user and owner to an existing subscriber
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/assignMobileSubscriberUserAndOwnerUsingPUT

i) changeMobileSubscriberPaymentPlanToPostpaid - change a subscribers plan to MOBILE_POSTPAID
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/changeMobileSubscriberPaymentPlanToPostpaidUsingPUT

j) changeMobileSubscriberPaymentPlanToPostpaid - change a subscribers plan to MOBILE_PREPAID
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/changeMobileSubscriberPaymentPlanToPrepaidUsingPUT

k) deleteMobileSubscribersByMobileNumber - remove a subscriber by mobile number
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/deleteMobileSubscribersByMobileNumberUsingDELETE

l) findMobileSubscribersByMobileNumber - lookup a subscriber by mobile number
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/findMobileSubscribersByMobileNumberUsingGET

m) findMobileSubscribersByServiceStartDate - lookup subscribers by service start date (a unix epoch number)
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/findMobileSubscribersByServiceStartDateUsingGET

n) findMobileSubscribersByServiceType - lookup subscribers by service type
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/findMobileSubscribersByServiceTypeUsingGET

o) findMobileSubscribersByCustomerIdUser - find a subscriber by customer Id
http://localhost:8080/swagger-ui/index.html#/mobile-subscriber-controller/findMobileSubscribersByCustomerIdUserUsingGET


If an endpoint requires a request body it will look like this JSON (PUT/POST methods only):

{
  "address": "332 Triq Varjta",
  "id": 0,
  "idCard": "0625412L",
  "name": "Paul",
  "surname": "Brown"
}


************************************************************************************************************************************

4) Design

a) Error handling:
  Exceptions in the backend are logged and translated into JSON responsee like this

{
  "apiVersion": "1.0",
  "error": {
    "code": "400",
    "message": "UnknownMobileNumberException",
    "errors": [
      {
        "domain": "mobilesubscriber-exceptions",
        "reason": "Mobile number unknown in database.",
        "message": "UnknownMobileNumberException",
        "sendReport": "?id=12bb9015-5019-4f15-bce8-3ce2c4d510ee"
      }
    ]
  }
}

b) Layers:
  Standard SpringBoot layers are done: Controller, Service, Repository and POJO
  
  The main classes of interest are:
     CustomerController
       -> CustomerService
          -> CustomerRepository
              -> Customer
          
     MobileSubscriberService
        (same layers)
 
     Controller layer has just the URL mappings.
     Service layer is where the validation is done.
     Repository is provided by Spring and persists to MySQL over Hibernate.
 
     Spring is used to weave in exception handling via the Advice classes (eg. CustomerControllerAdvice), this is how we translate
     backend exception to JSON.
 
c) Transaction Handling:
  Transactions are demarcated on the Service layer using @Transaction

d) Threading:
  The services are stateless so there is no need for synchronisation blocks, however concurrent access to data is handled using optimistic locking
   in order to prevent race conditions. This is done using @version annotation on the POJOs (Customer and MobileSubscriber). Therefore i added
   a 'version' field to the table structure described in the requirements to facilitate this type of locking.

e) Logging:
  ERROR and DEBUG logging is done in the service layer where most of the code is. DEBUG logging has to be turned on explicitly, this is to avoid
  logging lots of stuff by default, which can slow things down.

f) Unit tests:
  There are a number of unit tests in the CustomerApplicationTests class. These have been done using Mockito which
  is bundled in SpringBoot.

g) Database tables:

  These are created on the fly if they do not exist - this is handled in JPA.



************************************************************************************************************************************

5) Extra code of note:

I had to put in a few plumbing classes to leverage Spring:

   StartupApplication
   SwaggerConfig
   SecurityConfig 

If you want to run the code in Eclipse, use the Maven goal (the -D param allows breakpoint debugging):
  spring-boot:run -Dspring-boot.run.fork=false



************************************************************************************************************************************


6) What is missing due to running out of time:

OAUTH security on endpoints:
I did not use OAUTH, i could not get it working in time. I tried Auth0 but found it too time consuming so i went with
Basic authentication.

Active-Active handling:
If you start multiple containers then database replication needs to be setup in the backend. A master<->master relation between
the first mysql container and the second one, would be needed to allow client code to scale by hitting any backend.

Checking that the test coverage is >80%:
There is probably a tool that can be integrated into the build that will report on exactly how much coverage (%) we have. I did
not try that. There are unit tests covering what i consider to be the most important code. The delegating layers do not have any
logic that needs testing.


************************************************************************************************************************************


Thanks for reading!

  Paul Brown, April 2023



 

