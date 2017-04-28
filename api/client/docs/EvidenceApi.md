# EvidenceApi

All URIs are relative to *http://api.knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEvidence**](EvidenceApi.md#getEvidence) | **GET** /evidence/{evidenceId} | 


<a name="getEvidence"></a>
# **getEvidence**
> InlineResponse2004 getEvidence(evidenceId, keywords, pageNumber, pageSize)



Retrieves a (paged) list of references cited as evidence for a specified statement 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.EvidenceApi;


EvidenceApi apiInstance = new EvidenceApi();
String evidenceId = "evidenceId_example"; // String | local identifier of evidence subset, of cited references supporting a given concept-relationship statement 
String keywords = "keywords_example"; // String | keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of cited references per page to be returned in a paged set of query results 
try {
    InlineResponse2004 result = apiInstance.getEvidence(evidenceId, keywords, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EvidenceApi#getEvidence");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **evidenceId** | **String**| local identifier of evidence subset, of cited references supporting a given concept-relationship statement  |
 **keywords** | **String**| keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of cited references per page to be returned in a paged set of query results  | [optional]

### Return type

[**InlineResponse2004**](InlineResponse2004.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

