# ConceptsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getClique**](ConceptsApi.md#getClique) | **GET** /clique/{identifier} | 
[**getConceptDetails**](ConceptsApi.md#getConceptDetails) | **GET** /concepts/{cliqueId} | 
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts | 


<a name="getClique"></a>
# **getClique**
> BeaconCliqueIdentifier getClique(identifier, sessionId)



Retrieves the beacon aggregator assigned clique of equivalent concepts that includes the specified (url-encoded) CURIE identifier. Note that the clique to which a given concept CURIE belongs may change over time as the aggregator progressively discovers the members of the clique. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String identifier = "identifier_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of interest to be resolved to a concept clique
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    BeaconCliqueIdentifier result = apiInstance.getClique(identifier, sessionId);
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
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**BeaconCliqueIdentifier**](BeaconCliqueIdentifier.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptDetails"></a>
# **getConceptDetails**
> BeaconConceptWithDetails getConceptDetails(cliqueId, beacons, sessionId)



Retrieves details for a specified clique of equivalent concepts in the system,  as specified by a (url-encoded) CURIE identifier of a clique known to the aggregator 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String cliqueId = "cliqueId_example"; // String | a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier, as returned  by any other endpoint of the beacon aggregator API, of an exactly matching  concept clique of interest.
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of aggregator indices of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    BeaconConceptWithDetails result = apiInstance.getConceptDetails(cliqueId, beacons, sessionId);
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
 **beacons** | [**List&lt;String&gt;**](String.md)| set of aggregator indices of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**BeaconConceptWithDetails**](BeaconConceptWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConcepts"></a>
# **getConcepts**
> List&lt;BeaconConcept&gt; getConcepts(keywords, types, pageNumber, pageSize, beacons, sessionId)



Retrieves a (paged) list of concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String keywords = "keywords_example"; // String | a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes.
String types = "types_example"; // String | a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [semantic groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of aggregator indices of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<BeaconConcept> result = apiInstance.getConcepts(keywords, types, pageNumber, pageSize, beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | **String**| a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes. |
 **types** | **String**| a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [semantic groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **beacons** | [**List&lt;String&gt;**](String.md)| set of aggregator indices of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;BeaconConcept&gt;**](BeaconConcept.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

