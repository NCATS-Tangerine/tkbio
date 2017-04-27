# StatementsApi

All URIs are relative to *http://api.knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatements**](StatementsApi.md#getStatements) | **GET** /statements | 


<a name="getStatements"></a>
# **getStatements**
> InlineResponse2003 getStatements(emci, pageNumber, pageSize, keywords, semgroups)



Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String emci = "emci_example"; // String | a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
String keywords = "keywords_example"; // String | a (urlencoded) space delimited set of keywords or substrings against which to apply against the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts 
String semgroups = "semgroups_example"; // String | a (urlencoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
try {
    InlineResponse2003 result = apiInstance.getStatements(emci, pageNumber, pageSize, keywords, semgroups);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatements");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **emci** | **String**| a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements  |
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **keywords** | **String**| a (urlencoded) space delimited set of keywords or substrings against which to apply against the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts  | [optional]
 **semgroups** | **String**| a (urlencoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  | [optional]

### Return type

[**InlineResponse2003**](InlineResponse2003.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

