# EvidenceApi

All URIs are relative to *http://api.knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEvidence**](EvidenceApi.md#getEvidence) | **GET** /evidence/{statementId} | 


<a name="getEvidence"></a>
# **getEvidence**
> List&lt;InlineResponse2003&gt; getEvidence(statementId, keywords, pageNumber, pageSize)



Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.EvidenceApi;


EvidenceApi apiInstance = new EvidenceApi();
String statementId = "statementId_example"; // String | (url-encoded) CURIE identifier of the concept-relationship statement (\"assertion\", \"claim\") for which associated evidence is sought 
String keywords = "keywords_example"; // String | (url-encoded, space delimited) keyword filter to apply against the label field of the annotation 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of cited references per page to be returned in a paged set of query results 
try {
    List<InlineResponse2003> result = apiInstance.getEvidence(statementId, keywords, pageNumber, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EvidenceApi#getEvidence");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **statementId** | **String**| (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought  |
 **keywords** | **String**| (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  | [optional]
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of cited references per page to be returned in a paged set of query results  | [optional]

### Return type

[**List&lt;InlineResponse2003&gt;**](InlineResponse2003.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

