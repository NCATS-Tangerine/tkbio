# Swagger generated server #

Spring Boot Server 

## Overview ##

This subproject contains the TKBio REST Knowledge Source Application Programming Interface (KSAPI). The KSAPI is formally specified as an OpenAPI ("Swagger") specification, as archived in the 'specifications' subfolder. 

The [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project is used to generate Java client and server libraries/applications to use the API.  The underlying library integrating swagger to SpringBoot is [springfox](https://github.com/springfox/springfox).

## Building a Web Server using the API ##

The 'swagger.sh' script at the root of this subproject wraps a swagger-codegen command to (re-)generate a Java Spring Boot server code tree under 'src' (with Maven dependencies documented in the pom.xml file). The first argument given to the script should be a target KSAPI swagger json file inside the specification subfolder.   

The resulting code base may be directly built with Maven into an executable Java JAR file:

	mvn package

where the resulting JAR is placed into the 'target' subdirectory.

Alternately, the project may be converted to a Gradle project by simply running the 'gradle init' function inside the root 'api' subfolder.

## Running the Web Server ##

Start your server as an simple java application, something like

	java -jar target/swagger-spring-1.0.0.jar 

(for the Maven generated version) or

	java -jar build/libs/swagger-spring-1.0.0.jar 

(for the Gradle generated version).

You can view the api documentation in swagger-ui by pointing to
  
	http://localhost:8080/api

Change default port value in application.properties
