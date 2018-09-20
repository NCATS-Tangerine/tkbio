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
        String queryId = "queryId_example"; // String | the query identifier of a concepts query previously posted by the /cliques endpoint
        try {
            BeaconCliquesQueryResult result = apiInstance.getCliques(queryId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConceptsApi#getCliques");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://kba.ncats.io/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ConceptsApi* | [**getCliques**](docs/ConceptsApi.md#getCliques) | **GET** /cliques/data/{queryId} | 
*ConceptsApi* | [**getCliquesQueryStatus**](docs/ConceptsApi.md#getCliquesQueryStatus) | **GET** /cliques/status/{queryId} | 
*ConceptsApi* | [**getConceptDetails**](docs/ConceptsApi.md#getConceptDetails) | **GET** /concepts/details/{cliqueId} | 
*ConceptsApi* | [**getConcepts**](docs/ConceptsApi.md#getConcepts) | **GET** /concepts/data/{queryId} | 
*ConceptsApi* | [**getConceptsQueryStatus**](docs/ConceptsApi.md#getConceptsQueryStatus) | **GET** /concepts/status/{queryId} | 
*ConceptsApi* | [**postCliquesQuery**](docs/ConceptsApi.md#postCliquesQuery) | **POST** /cliques | 
*ConceptsApi* | [**postConceptsQuery**](docs/ConceptsApi.md#postConceptsQuery) | **POST** /concepts | 
*MetadataApi* | [**getBeacons**](docs/MetadataApi.md#getBeacons) | **GET** /beacons | 
*MetadataApi* | [**getConceptCategories**](docs/MetadataApi.md#getConceptCategories) | **GET** /categories | 
*MetadataApi* | [**getErrors**](docs/MetadataApi.md#getErrors) | **GET** /errorlog | 
*MetadataApi* | [**getKnowledgeMap**](docs/MetadataApi.md#getKnowledgeMap) | **GET** /kmap | 
*MetadataApi* | [**getPredicates**](docs/MetadataApi.md#getPredicates) | **GET** /predicates | 
*StatementsApi* | [**getStatementDetails**](docs/StatementsApi.md#getStatementDetails) | **GET** /statements/details/{statementId} | 
*StatementsApi* | [**getStatementsQuery**](docs/StatementsApi.md#getStatementsQuery) | **GET** /statements/data/{queryId} | 
*StatementsApi* | [**getStatementsQueryStatus**](docs/StatementsApi.md#getStatementsQueryStatus) | **GET** /statements/status/{queryId} | 
*StatementsApi* | [**postStatementsQuery**](docs/StatementsApi.md#postStatementsQuery) | **POST** /statements | 


## Documentation for Models

 - [BeaconBeaconConceptCategory](docs/BeaconBeaconConceptCategory.md)
 - [BeaconBeaconPredicate](docs/BeaconBeaconPredicate.md)
 - [BeaconClique](docs/BeaconClique.md)
 - [BeaconCliquesQuery](docs/BeaconCliquesQuery.md)
 - [BeaconCliquesQueryBeaconStatus](docs/BeaconCliquesQueryBeaconStatus.md)
 - [BeaconCliquesQueryResult](docs/BeaconCliquesQueryResult.md)
 - [BeaconCliquesQueryStatus](docs/BeaconCliquesQueryStatus.md)
 - [BeaconConcept](docs/BeaconConcept.md)
 - [BeaconConceptCategoriesByBeacon](docs/BeaconConceptCategoriesByBeacon.md)
 - [BeaconConceptCategory](docs/BeaconConceptCategory.md)
 - [BeaconConceptDetail](docs/BeaconConceptDetail.md)
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
 - [BeaconPredicate](docs/BeaconPredicate.md)
 - [BeaconPredicatesByBeacon](docs/BeaconPredicatesByBeacon.md)
 - [BeaconStatement](docs/BeaconStatement.md)
 - [BeaconStatementAnnotation](docs/BeaconStatementAnnotation.md)
 - [BeaconStatementCitation](docs/BeaconStatementCitation.md)
 - [BeaconStatementDetails](docs/BeaconStatementDetails.md)
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

