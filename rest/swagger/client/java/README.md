# swagger-java-client

## Requirements

Building this API client library requires Gradle to be installed.

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

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.ConceptsApi;

import java.io.File;
import java.util.*;

public class ConceptsApiExample {

    public static void main(String[] args) {
        
        ConceptsApi apiInstance = new ConceptsApi();
        String search = "search_example"; // String | search string to match against concept names
        Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
        Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
        String textFilter = "textFilter_example"; // String | text filter to apply against set of concepts matched by the main search string 
        String semanticType = "semanticType_example"; // String | semanticType filter to apply against set of concepts matched by the main search string 
        try {
            List<InlineResponse200> result = apiInstance.getConcepts(search, pageNumber, pageSize, textFilter, semanticType);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConceptsApi#getConcepts");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://knowledge.bio/api*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ConceptsApi* | [**getConcepts**](docs/ConceptsApi.md#getConcepts) | **GET** /concepts | 
*StatementsApi* | [**getStatements**](docs/StatementsApi.md#getStatements) | **GET** /statements | 
*StatementsApi* | [**postStatementQuery**](docs/StatementsApi.md#postStatementQuery) | **POST** /statements | 
*XrefsApi* | [**getConceptXRefs**](docs/XrefsApi.md#getConceptXRefs) | **GET** /xrefs/{conceptId} | 
*XrefsApi* | [**getXRefConcepts**](docs/XrefsApi.md#getXRefConcepts) | **GET** /xrefs | 
*XrefsApi* | [**postXRefQuery**](docs/XrefsApi.md#postXRefQuery) | **POST** /xrefs | 


## Documentation for Models

 - [Identifiers](docs/Identifiers.md)
 - [InlineResponse200](docs/InlineResponse200.md)
 - [InlineResponse2001](docs/InlineResponse2001.md)
 - [InlineResponse2002](docs/InlineResponse2002.md)
 - [InlineResponse2003](docs/InlineResponse2003.md)
 - [InlineResponse201](docs/InlineResponse201.md)
 - [StatementsEvidence](docs/StatementsEvidence.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

richard@starinformatics.com

