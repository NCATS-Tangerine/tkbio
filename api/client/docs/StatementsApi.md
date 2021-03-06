# StatementsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatementDetails**](StatementsApi.md#getStatementDetails) | **GET** /statements/details/{statementId} | 
[**getStatementsQuery**](StatementsApi.md#getStatementsQuery) | **GET** /statements/data/{queryId} | 
[**getStatementsQueryStatus**](StatementsApi.md#getStatementsQueryStatus) | **GET** /statements/status/{queryId} | 
[**postStatementsQuery**](StatementsApi.md#postStatementsQuery) | **POST** /statements | 


<a name="getStatementDetails"></a>
# **getStatementDetails**
> BeaconStatementDetails getStatementDetails(statementId, keywords, pageNumber, pageSize)



Retrieves a details relating to a specified concept-relationship statement include &#39;is_defined_by and &#39;provided_by&#39; provenance; extended edge properties exported as tag &#x3D; value; and any associated annotations (publications, etc.)  cited as evidence for the given statement. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String statementId = "statementId_example"; // String | (url-encoded) CURIE identifier of the concept-relationship statement (\"assertion\", \"claim\") for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691 
List<String> keywords = Arrays.asList("keywords_example"); // List<String> | an array of keywords or substrings against which to filter a reference label (e.g. title) statement evidence citation array.
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of statement.evidence array entries. Defaults to 1. 
Integer pageSize = 56; // Integer | number of cited references per page to be returned in a paged set of statement.evidence array entries. Defaults to '10'. 
try {
    BeaconStatementDetails result = apiInstance.getStatementDetails(statementId, keywords, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatementDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **statementId** | **String**| (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691  |
 **keywords** | [**List&lt;String&gt;**](String.md)| an array of keywords or substrings against which to filter a reference label (e.g. title) statement evidence citation array. | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of statement.evidence array entries. Defaults to 1.  | [optional]
 **pageSize** | **Integer**| number of cited references per page to be returned in a paged set of statement.evidence array entries. Defaults to &#39;10&#39;.  | [optional]

### Return type

[**BeaconStatementDetails**](BeaconStatementDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getStatementsQuery"></a>
# **getStatementsQuery**
> BeaconStatementsQueryResult getStatementsQuery(queryId, beacons, pageNumber, pageSize)



Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String queryId = "queryId_example"; // String | an active query identifier as returned by a POST of statement query parameters.
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons whose statements are to be retrieved 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results. Defaults to '1'. 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results. Defaults to '10'. 
try {
    BeaconStatementsQueryResult result = apiInstance.getStatementsQuery(queryId, beacons, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatementsQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| an active query identifier as returned by a POST of statement query parameters. |
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| subset of aggregator indices of beacons whose statements are to be retrieved  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results. Defaults to &#39;1&#39;.  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results. Defaults to &#39;10&#39;.  | [optional]

### Return type

[**BeaconStatementsQueryResult**](BeaconStatementsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getStatementsQueryStatus"></a>
# **getStatementsQueryStatus**
> BeaconStatementsQueryStatus getStatementsQueryStatus(queryId, beacons)



Retrieves the status of a given query about the statements in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String queryId = "queryId_example"; // String | an active query identifier as returned by a POST of statements  query parameters.
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned) 
try {
    BeaconStatementsQueryStatus result = apiInstance.getStatementsQueryStatus(queryId, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatementsQueryStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| an active query identifier as returned by a POST of statements  query parameters. |
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned)  | [optional]

### Return type

[**BeaconStatementsQueryStatus**](BeaconStatementsQueryStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postStatementsQuery"></a>
# **postStatementsQuery**
> BeaconStatementsQuery postStatementsQuery(source, relations, target, keywords, categories, beacons)



Posts a query to retrieve concept-relations where either the subject or object concept matches a [CURIE-encoded &#39;source&#39;](https://www.w3.org/TR/curie/) clique identifier designating a set of exactly matching concepts. A &#39;target&#39; clique identifier may optionally be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String source = "source_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching 'source' clique, cliques as identified by other endpoints of the beacon aggregator API.  
List<String> relations = Arrays.asList("relations_example"); // List<String> | a subset (array) of identifiers of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint. 
String target = "target_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching 'target' clique, cliques as identified by other endpoints of the beacon aggregator API.  
List<String> keywords = Arrays.asList("keywords_example"); // List<String> | an array of keywords or substrings against which to match the  'target' concept or 'predicate' names of the set of  concept-relations matched by the 'source' concepts.
List<String> categories = Arrays.asList("categories_example"); // List<String> | a subset (array) of identifiers of concept categories to which to constrain 'target' concepts associated with the given 'source' concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of categories). 
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons to be used as knowledge sources for the query 
try {
    BeaconStatementsQuery result = apiInstance.postStatementsQuery(source, relations, target, keywords, categories, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#postStatementsQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **source** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, cliques as identified by other endpoints of the beacon aggregator API.   |
 **relations** | [**List&lt;String&gt;**](String.md)| a subset (array) of identifiers of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  | [optional]
 **target** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, cliques as identified by other endpoints of the beacon aggregator API.   | [optional]
 **keywords** | [**List&lt;String&gt;**](String.md)| an array of keywords or substrings against which to match the  &#39;target&#39; concept or &#39;predicate&#39; names of the set of  concept-relations matched by the &#39;source&#39; concepts. | [optional]
 **categories** | [**List&lt;String&gt;**](String.md)| a subset (array) of identifiers of concept categories to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of categories).  | [optional]
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons to be used as knowledge sources for the query  | [optional]

### Return type

[**BeaconStatementsQuery**](BeaconStatementsQuery.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

