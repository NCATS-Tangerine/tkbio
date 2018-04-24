# StatementsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatements**](StatementsApi.md#getStatements) | **GET** /statements | 


<a name="getStatements"></a>
# **getStatements**
> List&lt;BeaconStatement&gt; getStatements(source, relations, target, keywords, types, pageNumber, pageSize, beacons, sessionId)



Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String source = "source_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching 'source' clique, as defined by other endpoints of the beacon aggregator API.  
String relations = "relations_example"; // String | a (url-encoded, space-delimited) string of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint. 
String target = "target_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching 'target' clique, as defined by other endpoints of the beacon aggregator API.  
String keywords = "keywords_example"; // String | a (url-encoded, space-delimited) string of keywords or substrings against which to match the 'target' concept or 'predicate' names of the set of concept-relations matched by the 'source' concepts. 
String types = "types_example"; // String | a (url-encoded, space-delimited) string of concept semantic types (specified as CURIEs of Translator ontology data type terms) to which to constrain 'target' concepts associated with the given 'source' concept. 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of aggregator indices of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<BeaconStatement> result = apiInstance.getStatements(source, relations, target, keywords, types, pageNumber, pageSize, beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatements");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **source** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   |
 **relations** | **String**| a (url-encoded, space-delimited) string of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  | [optional]
 **target** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   | [optional]
 **keywords** | **String**| a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  | [optional]
 **types** | **String**| a (url-encoded, space-delimited) string of concept semantic types (specified as CURIEs of Translator ontology data type terms) to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept.  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **beacons** | [**List&lt;String&gt;**](String.md)| set of aggregator indices of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;BeaconStatement&gt;**](BeaconStatement.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

