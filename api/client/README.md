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
        String identifier = "identifier_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of interest to be resolved to a concept clique
        try {
            BeaconCliqueIdentifier result = apiInstance.getClique(identifier);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConceptsApi#getClique");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://kba.ncats.io/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ConceptsApi* | [**getClique**](docs/ConceptsApi.md#getClique) | **GET** /clique/{identifier} | 
*ConceptsApi* | [**getConceptDetails**](docs/ConceptsApi.md#getConceptDetails) | **GET** /concepts/details/{cliqueId} | 
*ConceptsApi* | [**getConcepts**](docs/ConceptsApi.md#getConcepts) | **GET** /concepts/data/{queryId} | 
*ConceptsApi* | [**getConceptsQueryStatus**](docs/ConceptsApi.md#getConceptsQueryStatus) | **GET** /concepts/status/{queryId} | 
*ConceptsApi* | [**postConceptsQuery**](docs/ConceptsApi.md#postConceptsQuery) | **POST** /concepts | 
*MetadataApi* | [**getBeacons**](docs/MetadataApi.md#getBeacons) | **GET** /beacons | 
*MetadataApi* | [**getConceptTypes**](docs/MetadataApi.md#getConceptTypes) | **GET** /types | 
*MetadataApi* | [**getErrors**](docs/MetadataApi.md#getErrors) | **GET** /errorlog | 
*MetadataApi* | [**getKnowledgeMap**](docs/MetadataApi.md#getKnowledgeMap) | **GET** /kmap | 
*MetadataApi* | [**getPredicates**](docs/MetadataApi.md#getPredicates) | **GET** /predicates | 
*StatementsApi* | [**getEvidence**](docs/StatementsApi.md#getEvidence) | **GET** /evidence/{statementId} | 
*StatementsApi* | [**getStatementsQuery**](docs/StatementsApi.md#getStatementsQuery) | **GET** /statements/data/{queryId} | 
*StatementsApi* | [**getStatementsQueryStatus**](docs/StatementsApi.md#getStatementsQueryStatus) | **GET** /statements/status/{queryId} | 
*StatementsApi* | [**postStatementsQuery**](docs/StatementsApi.md#postStatementsQuery) | **POST** /statements | 


## Documentation for Models

 - [BeaconAnnotation](docs/BeaconAnnotation.md)
 - [BeaconBeaconConceptType](docs/BeaconBeaconConceptType.md)
 - [BeaconBeaconPredicate](docs/BeaconBeaconPredicate.md)
 - [BeaconCliqueIdentifier](docs/BeaconCliqueIdentifier.md)
 - [BeaconConcept](docs/BeaconConcept.md)
 - [BeaconConceptDetail](docs/BeaconConceptDetail.md)
 - [BeaconConceptTypes](docs/BeaconConceptTypes.md)
 - [BeaconConceptTypesByBeacon](docs/BeaconConceptTypesByBeacon.md)
 - [BeaconConceptWithDetails](docs/BeaconConceptWithDetails.md)
 - [BeaconConceptWithDetailsBeaconEntry](docs/BeaconConceptWithDetailsBeaconEntry.md)
 - [BeaconConceptsQuery](docs/BeaconConceptsQuery.md)
 - [BeaconConceptsQueryBeaconStatus](docs/BeaconConceptsQueryBeaconStatus.md)
 - [BeaconConceptsQueryResult](docs/BeaconConceptsQueryResult.md)
 - [BeaconConceptsQueryStatus](docs/BeaconConceptsQueryStatus.md)
 - [BeaconKnowledgeBeacon](docs/BeaconKnowledgeBeacon.md)
 - [BeaconKnowledgeMap](docs/BeaconKnowledgeMap.md)
 - [BeaconKnowledgeMapObject](docs/BeaconKnowledgeMapObject.md)
 - [BeaconKnowledgeMapPredicate](docs/BeaconKnowledgeMapPredicate.md)
 - [BeaconKnowledgeMapStatement](docs/BeaconKnowledgeMapStatement.md)
 - [BeaconKnowledgeMapSubject](docs/BeaconKnowledgeMapSubject.md)
 - [BeaconLogEntry](docs/BeaconLogEntry.md)
 - [BeaconPredicates](docs/BeaconPredicates.md)
 - [BeaconPredicatesByBeacon](docs/BeaconPredicatesByBeacon.md)
 - [BeaconStatement](docs/BeaconStatement.md)
 - [BeaconStatementObject](docs/BeaconStatementObject.md)
 - [BeaconStatementPredicate](docs/BeaconStatementPredicate.md)
 - [BeaconStatementSubject](docs/BeaconStatementSubject.md)
 - [BeaconStatementsQuery](docs/BeaconStatementsQuery.md)
 - [BeaconStatementsQueryBeaconStatus](docs/BeaconStatementsQueryBeaconStatus.md)
 - [BeaconStatementsQueryResult](docs/BeaconStatementsQueryResult.md)
 - [BeaconStatementsQueryStatus](docs/BeaconStatementsQueryStatus.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

richard@starinformatics.com

