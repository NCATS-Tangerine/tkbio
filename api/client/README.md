# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import bio.knowledge.client.*;
import bio.knowledge.client.auth.*;
import bio.knowledge.client.model.*;
import bio.knowledge.client.api.AggregatorApi;

import java.io.File;
import java.util.*;

public class AggregatorApiExample {

    public static void main(String[] args) {
        
        AggregatorApi apiInstance = new AggregatorApi();
        String sessionId = "sessionId_example"; // String | identifier to be used for tagging session data 
        try {
            List<KnowledgeBeacon> result = apiInstance.getBeacons(sessionId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AggregatorApi#getBeacons");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://kba.ncats.io*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AggregatorApi* | [**getBeacons**](docs/AggregatorApi.md#getBeacons) | **GET** /beacons | 
*AggregatorApi* | [**getErrors**](docs/AggregatorApi.md#getErrors) | **GET** /errorlog | 
*ConceptsApi* | [**getConceptDetails**](docs/ConceptsApi.md#getConceptDetails) | **GET** /concepts/{conceptId} | 
*ConceptsApi* | [**getConcepts**](docs/ConceptsApi.md#getConcepts) | **GET** /concepts | 
*EvidenceApi* | [**getEvidence**](docs/EvidenceApi.md#getEvidence) | **GET** /evidence/{statementId} | 
*StatementsApi* | [**getStatements**](docs/StatementsApi.md#getStatements) | **GET** /statements | 
*SummaryApi* | [**linkedTypes**](docs/SummaryApi.md#linkedTypes) | **GET** /types | 


## Documentation for Models

 - [Annotation](docs/Annotation.md)
 - [Concept](docs/Concept.md)
 - [ConceptDetail](docs/ConceptDetail.md)
 - [Detail](docs/Detail.md)
 - [KnowledgeBeacon](docs/KnowledgeBeacon.md)
 - [LogEntry](docs/LogEntry.md)
 - [Statement](docs/Statement.md)
 - [StatementsObject](docs/StatementsObject.md)
 - [StatementsPredicate](docs/StatementsPredicate.md)
 - [Subject](docs/Subject.md)
 - [Summary](docs/Summary.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

richard@starinformatics.com

