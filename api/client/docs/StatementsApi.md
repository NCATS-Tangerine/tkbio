# StatementsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatements**](StatementsApi.md#getStatements) | **GET** /statements | 


<a name="getStatements"></a>
# **getStatements**
> InlineResponse2003 getStatements(ci, pageNumber, pageSize, q, sg)



Given a list of concept identifiers to a given KS endpoint,  retrieves a paged list of concept-relations with either the subject or object concept matching a concept in the input list 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
List<String> ci = Arrays.asList("ci_example"); // List<String> | list of concept identifiers (ci) to be used in a search for associated concept-relation statements 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
List<String> q = Arrays.asList("q_example"); // List<String> | keyword filter to apply against the subject, predicate or object names of the set of concept-relations matched by the query seed concept 
List<String> sg = Arrays.asList("sg_example"); // List<String> | array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes) 
try {
    InlineResponse2003 result = apiInstance.getStatements(ci, pageNumber, pageSize, q, sg);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatements");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ci** | [**List&lt;String&gt;**](String.md)| list of concept identifiers (ci) to be used in a search for associated concept-relation statements  |
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **q** | [**List&lt;String&gt;**](String.md)| keyword filter to apply against the subject, predicate or object names of the set of concept-relations matched by the query seed concept  | [optional]
 **sg** | [**List&lt;String&gt;**](String.md)| array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes)  | [optional]

### Return type

[**InlineResponse2003**](InlineResponse2003.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

