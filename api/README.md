# Swagger generated server

Spring Boot Server 

## Overview 

This subproject contains the TKBio REST Knowledge Source Application Programming Interface (KSAPI). The KSAPI is formally specified as an OpenAPI ("Swagger") specification, as archived in the 'specifications' subfolder. 

The [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project is used to generate Java client and server libraries/applications to use the API. 

The 'swagger.sh' script at the root of this subproject may be used to (re-)generate a Java Spring Boot server code tree under 'src' (with Maven dependencies documented in the pom.xml file).  After running this script, the project may be further converted to a Gradle project by simply running the 'gradle init' function inside the root 'api' subfolder.

The underlying library integrating swagger to SpringBoot is [springfox](https://github.com/springfox/springfox)  

Build the server into an executable Java JAR file by using Maven:

	mvn package

The resulting JAR is placed into the 'target' subdirectory.

Start your server as an simple java application, something like

	java -jar target/swagger-spring-1.0.0.jar 

You can view the api documentation in swagger-ui by pointing to
  
	http://localhost:8080/api



Change default port value in application.properties