## Starting...

- `git clone https://github.com/HenryLucena/order-manager.git`
- `cd order-manager`

Now you can execute the commands below

## Clean and compile

- `mvn clean`<br>

- `mvn compile`<br>
  
  ## Running the application and the RESTFul API

- `mvn exec:java -Dexec.mainClass="nome.completo.Classe" -Dexec.args="arg1 arg2"`<br>
  execute the method _main_ from indicated class in configuration from _plugin_ relevant
  in the file pom.xml. it depends from `mvn compile`.
  
- `java -jar target/exemplo-unico.jar`<br>
 
 ## Database Postgres
 
 This project is created using a PostgresSQL database. For the correct running you need to have a Postgres database named order-manager-db <br>
 in your local database or change the name for connection in application.properties
  
  ## Documentation
  
  All endpoints used in this application can be found at http://localhost:8080/swagger-ui.html after the application start
  
  ## Dependecies
  
  In this project, the main dependecies are:
  
  - Spring Web
  - Spring JPA
  - Spring email
  - Postgres
  - Lombok
