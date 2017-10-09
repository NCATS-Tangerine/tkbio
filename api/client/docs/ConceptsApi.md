# ConceptsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConceptDetails**](ConceptsApi.md#getConceptDetails) | **GET** /concepts/{conceptId} | 
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts | 


<a name="getConceptDetails"></a>
# **getConceptDetails**
> List&lt;ConceptDetail&gt; getConceptDetails(conceptId, beacons, sessionId)



Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE identifier of a concept known the given knowledge source 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String conceptId = "conceptId_example"; // String | (url-encoded) CURIE identifier of concept of interest, e.g. wd:Q126691
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of IDs of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<ConceptDetail> result = apiInstance.getConceptDetails(conceptId, beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConceptDetails");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conceptId** | **String**| (url-encoded) CURIE identifier of concept of interest, e.g. wd:Q126691 |
 **beacons** | [**List&lt;String&gt;**](String.md)| set of IDs of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;ConceptDetail&gt;**](ConceptDetail.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConcepts"></a>
# **getConcepts**
> List&lt;Concept&gt; getConcepts(keywords, semgroups, pageNumber, pageSize, beacons, sessionId)



Retrieves a (paged) list of concepts in the system 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String keywords = "keywords_example"; // String | a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes.
String semgroups = "semgroups_example"; // String | a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of IDs of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<Concept> result = apiInstance.getConcepts(keywords, semgroups, pageNumber, pageSize, beacons, sessionId);
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
 **semgroups** | **String**| a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **beacons** | [**List&lt;String&gt;**](String.md)| set of IDs of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;Concept&gt;**](Concept.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

