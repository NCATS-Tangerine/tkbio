# EvidenceApi

All URIs are relative to *http://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEvidence**](EvidenceApi.md#getEvidence) | **GET** /evidence/{statementId} | 


<a name="getEvidence"></a>
# **getEvidence**
> List&lt;Annotation&gt; getEvidence(statementId, keywords, pageNumber, pageSize, beacons, sessionId)



Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.EvidenceApi;


EvidenceApi apiInstance = new EvidenceApi();
String statementId = "statementId_example"; // String | (url-encoded) CURIE identifier of the concept-relationship statement (\"assertion\", \"claim\") for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691 
String keywords = "keywords_example"; // String | (url-encoded, space delimited) keyword filter to apply against the label field of the annotation 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of cited references per page to be returned in a paged set of query results 
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of IDs of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<Annotation> result = apiInstance.getEvidence(statementId, keywords, pageNumber, pageSize, beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EvidenceApi#getEvidence");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **statementId** | **String**| (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691  |
 **keywords** | **String**| (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of cited references per page to be returned in a paged set of query results  | [optional]
 **beacons** | [**List&lt;String&gt;**](String.md)| set of IDs of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;Annotation&gt;**](Annotation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

