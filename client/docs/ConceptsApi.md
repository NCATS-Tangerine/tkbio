# ConceptsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getCliques**](ConceptsApi.md#getCliques) | **GET** /cliques/data/{queryId} | 
[**getCliquesQueryStatus**](ConceptsApi.md#getCliquesQueryStatus) | **GET** /cliques/status/{queryId} | 
[**getConceptDetails**](ConceptsApi.md#getConceptDetails) | **GET** /concepts/details/{cliqueId} | 
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts/data/{queryId} | 
[**getConceptsQueryStatus**](ConceptsApi.md#getConceptsQueryStatus) | **GET** /concepts/status/{queryId} | 
[**postCliquesQuery**](ConceptsApi.md#postCliquesQuery) | **POST** /cliques | 
[**postConceptsQuery**](ConceptsApi.md#postConceptsQuery) | **POST** /concepts | 


<a name="getCliques"></a>
# **getCliques**
> BeaconCliquesQueryResult getCliques(queryId)



Retrieves a list of concept cliques based on  &#39;data ready&#39; from a previously /cliques posted query parameter submission 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | the query identifier of a concepts query previously posted by the /cliques endpoint
try {
    BeaconCliquesQueryResult result = apiInstance.getCliques(queryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getCliques");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| the query identifier of a concepts query previously posted by the /cliques endpoint |

### Return type

[**BeaconCliquesQueryResult**](BeaconCliquesQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCliquesQueryStatus"></a>
# **getCliquesQueryStatus**
> BeaconCliquesQueryStatus getCliquesQueryStatus(queryId)



Retrieves the status of a given query about the cliques in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | an active query identifier as returned by a POST of clique query parameters.
try {
    BeaconCliquesQueryStatus result = apiInstance.getCliquesQueryStatus(queryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getCliquesQueryStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| an active query identifier as returned by a POST of clique query parameters. |

### Return type

[**BeaconCliquesQueryStatus**](BeaconCliquesQueryStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptDetails"></a>
# **getConceptDetails**
> BeaconConceptWithDetails getConceptDetails(cliqueId, beacons)



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
    BeaconConceptWithDetails result = apiInstance.getConceptDetails(cliqueId, beacons);
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

[**BeaconConceptWithDetails**](BeaconConceptWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConcepts"></a>
# **getConcepts**
> BeaconConceptsQueryResult getConcepts(queryId, beacons, pageNumber, pageSize)



Retrieves a (paged) list of basic equivalent concept clique data from beacons &#39;data ready&#39; from a previously /concepts posted query parameter submission 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | the query identifier of a concepts query previously posted by the /concepts endpoint
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons whose data are to be retrieved 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results. Defaults to '1'. 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results. Defaults to '10'. 
try {
    BeaconConceptsQueryResult result = apiInstance.getConcepts(queryId, beacons, pageNumber, pageSize);
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
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results. Defaults to &#39;1&#39;.  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results. Defaults to &#39;10&#39;.  | [optional]

### Return type

[**BeaconConceptsQueryResult**](BeaconConceptsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptsQueryStatus"></a>
# **getConceptsQueryStatus**
> BeaconConceptsQueryStatus getConceptsQueryStatus(queryId, beacons)



Retrieves the status of a given keyword search query about the concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String queryId = "queryId_example"; // String | an active query identifier as returned by a POST of concept query parameters.
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned) 
try {
    BeaconConceptsQueryStatus result = apiInstance.getConceptsQueryStatus(queryId, beacons);
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

[**BeaconConceptsQueryStatus**](BeaconConceptsQueryStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postCliquesQuery"></a>
# **postCliquesQuery**
> BeaconCliquesQuery postCliquesQuery(ids)



Retrieves the beacon aggregator assigned cliques of equivalent concepts that includes the specified (url-encoded) CURIE identifiers. Note that the clique to which a given concept CURIE belongs may change over time as the aggregator progressively discovers the members of the clique. Any unmatched identifiers will be ignored (e.g. the id couldn&#39;t be found in any of the beacons)  

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
List<String> ids = Arrays.asList("ids_example"); // List<String> | an array of [CURIE-encoded](https://www.w3.org/TR/curie/)  identifiers of interest to be resolved to a list of concept cliques
try {
    BeaconCliquesQuery result = apiInstance.postCliquesQuery(ids);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#postCliquesQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ids** | [**List&lt;String&gt;**](String.md)| an array of [CURIE-encoded](https://www.w3.org/TR/curie/)  identifiers of interest to be resolved to a list of concept cliques |

### Return type

[**BeaconCliquesQuery**](BeaconCliquesQuery.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postConceptsQuery"></a>
# **postConceptsQuery**
> BeaconConceptsQuery postConceptsQuery(keywords, categories, beacons)



Posts the query parameters to retrieves a list of  concepts from the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
List<String> keywords = Arrays.asList("keywords_example"); // List<String> | an array of keywords or substrings against which to match concept names and synonyms
List<String> categories = Arrays.asList("categories_example"); // List<String> | a subset array of concept categories (specified as codes 'gene',  'pathway', etc.) to which to constrain concepts matched by the main keyword search (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes) 
List<Integer> beacons = Arrays.asList(56); // List<Integer> | subset of aggregator indices of beacons to be used as knowledge sources for the query (if omitted, then the all beacons are queried) 
try {
    BeaconConceptsQuery result = apiInstance.postConceptsQuery(keywords, categories, beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#postConceptsQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | [**List&lt;String&gt;**](String.md)| an array of keywords or substrings against which to match concept names and synonyms |
 **categories** | [**List&lt;String&gt;**](String.md)| a subset array of concept categories (specified as codes &#39;gene&#39;,  &#39;pathway&#39;, etc.) to which to constrain concepts matched by the main keyword search (see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes)  | [optional]
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| subset of aggregator indices of beacons to be used as knowledge sources for the query (if omitted, then the all beacons are queried)  | [optional]

### Return type

[**BeaconConceptsQuery**](BeaconConceptsQuery.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

