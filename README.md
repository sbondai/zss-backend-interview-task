# ZSS - Backend Take Home Test using Spring Boot
### Assumptions ###
 - The purchase of a book using the given transaction endpoint assumes the happy path.
 - The task was completed using a *TDD approach* , src/test/java contains relevant test cases that explain the code base.
 - JUnit 4 as unit test engine.
 - H2 embedded database that can be reconfigured to use any db in the application.yml file
 
  ### How to run the Application ###
 - Build and run as Maven application in IDE or mvn spring-boot:run on commandline with Maven installed.
 - Configure JUnit 4 in project path and excute all test cases. 

  ### End points ###
 -  #### Book ####
     - POST *api/v1/books*  - Create book
     - GET  *api/v1/books*  - Get all books
   
 -  #### Category ####
     - POST */api/v1/categories*  - Create Category
     - GET  */api/v1/categories/{category}*  - Get books from specific category

 
 ### Other ###
 - Given more time I would have modelled the response from the transaction endpoint and updated the status of book purchased. 
 
