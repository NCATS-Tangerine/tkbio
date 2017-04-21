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
import bio.knowledge.client.api.ConceptsApi;

import java.io.File;
import java.util.*;

public class ConceptsApiExample {

    public static void main(String[] args) {
        
        ConceptsApi apiInstance = new ConceptsApi();
        String conceptId = "conceptId_example"; // String | local object identifier of concept of interest
        try {
            List<InlineResponse200> result = apiInstance.getConceptDetails(conceptId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConceptsApi#getConceptDetails");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://knowledge.bio/api*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ConceptsApi* | [**getConceptDetails**](docs/ConceptsApi.md#getConceptDetails) | **GET** /concepts/{conceptId} | 
*ConceptsApi* | [**getConcepts**](docs/ConceptsApi.md#getConcepts) | **GET** /concepts | 
*EvidenceApi* | [**getEvidence**](docs/EvidenceApi.md#getEvidence) | **GET** /evidence/{evidenceId} | 
*StatementsApi* | [**getStatements**](docs/StatementsApi.md#getStatements) | **GET** /statements | 
*XrefsApi* | [**getConceptXRefs**](docs/XrefsApi.md#getConceptXRefs) | **GET** /xrefs/{conceptId} | 
*XrefsApi* | [**getXRefConcepts**](docs/XrefsApi.md#getXRefConcepts) | **GET** /xrefs | 


## Documentation for Models

 - [ConceptsconceptIdDetails](docs/ConceptsconceptIdDetails.md)
 - [InlineResponse200](docs/InlineResponse200.md)
 - [InlineResponse2001](docs/InlineResponse2001.md)
 - [InlineResponse2001DataPage](docs/InlineResponse2001DataPage.md)
 - [InlineResponse2002](docs/InlineResponse2002.md)
 - [InlineResponse2003](docs/InlineResponse2003.md)
 - [InlineResponse2003DataPage](docs/InlineResponse2003DataPage.md)
 - [InlineResponse2003Evidence](docs/InlineResponse2003Evidence.md)
 - [InlineResponse2003Subject](docs/InlineResponse2003Subject.md)
 - [InlineResponse2004](docs/InlineResponse2004.md)
 - [InlineResponse2004DataPage](docs/InlineResponse2004DataPage.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

richard@starinformatics.com

