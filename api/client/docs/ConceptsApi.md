# ConceptsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getClique**](ConceptsApi.md#getClique) | **GET** /clique/{identifier} | 
[**getConceptDetails**](ConceptsApi.md#getConceptDetails) | **GET** /concepts/details/{cliqueId} | 
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts/data/{queryId} | 
[**getConceptsQueryStatus**](ConceptsApi.md#getConceptsQueryStatus) | **GET** /concepts/status/{queryId} | 
[**postConceptsQuery**](ConceptsApi.md#postConceptsQuery) | **POST** /concepts | 


<a name="getClique"></a>
# **getClique**
> ServerCliqueIdentifier getClique(identifier)



Retrieves the beacon aggregator assigned clique of equivalent concepts that includes the specified (url-encoded) CURIE identifier. Note that the clique to which a given concept CURIE belongs may change over time as the aggregator progressively discovers the members of the clique. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String identifier = "identifier_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of interest to be resolved to a concept clique
try {
    ServerCliqueIdentifier result = apiInstance.getClique(identifier);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getClique");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **identifier** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of interest to be resolved to a concept clique |

### Return type

[**ServerCliqueIdentifier**](ServerCliqueIdentifier.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptDetails"></a>
# **getConceptDetails**
> ServerConceptWithDetails getConceptDetails(cliqueId, beacons)



Retrieves details for a specified clique of equivalent concepts in the system,  as specified by a (url-encoded) CURIE identifier of a clique known to the aggregator 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String cliqueId = "cliqueId_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier, as returned  by any other endpoint of the beacon aggregator API, of an exactly matching  concept clique of interest.
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons to be used as knowledge sources for the query 
try {
    ServerConceptWithDetails result = apiInstance.getConceptDetails(cliqueId, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConceptDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **cliqueId** | **String**| a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier, as returned  by any other endpoint of the beacon aggregator API, of an exactly matching  concept clique of interest. |
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons to be used as knowledge sources for the query  | [optional]

### Return type

[**ServerConceptWithDetails**](ServerConceptWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConcepts"></a>
# **getConcepts**
> ServerConceptsQueryResult getConcepts(queryId, beacons, pageNumber, pageSize)



Retrieves a (paged) simple list of concepts from beacons with status &#39;data ready&#39; from a previously /concepts posted query parameter submission 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | the query identifier of a concepts query previously posted by the /concepts endpoint
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons whose data are to be retrieved 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
try {
    ServerConceptsQueryResult result = apiInstance.getConcepts(queryId, beacons, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| the query identifier of a concepts query previously posted by the /concepts endpoint |
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons whose data are to be retrieved  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]

### Return type

[**ServerConceptsQueryResult**](ServerConceptsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptsQueryStatus"></a>
# **getConceptsQueryStatus**
> ServerConceptsQueryStatus getConceptsQueryStatus(queryId, beacons)



Retrieves the status of a given query about the concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | an active query identifier as returned by a POST of concept query parameters.
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned) 
try {
    ServerConceptsQueryStatus result = apiInstance.getConceptsQueryStatus(queryId, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConceptsQueryStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| an active query identifier as returned by a POST of concept query parameters. |
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned)  | [optional]

### Return type

[**ServerConceptsQueryStatus**](ServerConceptsQueryStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postConceptsQuery"></a>
# **postConceptsQuery**
> ServerConceptsQuery postConceptsQuery(keywords, types, beacons)



Posts the query parameters to retrieves a (paged) list of  concepts from the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String keywords = "keywords_example"; // String | a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes.
List<String> types = Arrays.asList("types_example"); // List<String> | a subset array of concept types (specified as codes gene, pathway, etc.) to which to constrain concepts matched by the main keyword search (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes) 
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons to be used as knowledge sources for the query (if omitted, then the all beacons are queried) 
try {
    ServerConceptsQuery result = apiInstance.postConceptsQuery(keywords, types, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#postConceptsQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | **String**| a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes. |
 **types** | [**List&lt;String&gt;**](String.md)| a subset array of concept types (specified as codes gene, pathway, etc.) to which to constrain concepts matched by the main keyword search (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes)  | [optional]
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| subset of aggregator indices of beacons to be used as knowledge sources for the query (if omitted, then the all beacons are queried)  | [optional]

### Return type

[**ServerConceptsQuery**](ServerConceptsQuery.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

