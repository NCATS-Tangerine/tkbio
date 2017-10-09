# StatementsApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatements**](StatementsApi.md#getStatements) | **GET** /statements | 


<a name="getStatements"></a>
# **getStatements**
> List&lt;Statement&gt; getStatements(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId)



Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
List<String> c = Arrays.asList("c_example"); // List<String> | set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements, e.g. wd:Q126691, wd:Q420626 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
String keywords = "keywords_example"; // String | a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts 
String semgroups = "semgroups_example"; // String | a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) 
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of IDs of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<Statement> result = apiInstance.getStatements(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatements");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **c** | [**List&lt;String&gt;**](String.md)| set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements, e.g. wd:Q126691, wd:Q420626  |
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **keywords** | **String**| a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts  | [optional]
 **semgroups** | **String**| a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  | [optional]
 **beacons** | [**List&lt;String&gt;**](String.md)| set of IDs of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;Statement&gt;**](Statement.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

