# ConceptsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConcepts**](ConceptsApi.md#getConcepts) | **GET** /concepts | 


<a name="getConcepts"></a>
# **getConcepts**
> List&lt;InlineResponse200&gt; getConcepts(search, pageNumber, pageSize, textFilter, semanticType)



Retrieves a (paged) list of concepts in the system 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConceptsApi;


ConceptsApi apiInstance = new ConceptsApi();
String search = "search_example"; // String | search string to match against concept names
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
String textFilter = "textFilter_example"; // String | text filter to apply against set of concepts matched by the main search string 
String semanticType = "semanticType_example"; // String | semanticType filter to apply against set of concepts matched by the main search string 
try {
    List<InlineResponse200> result = apiInstance.getConcepts(search, pageNumber, pageSize, textFilter, semanticType);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConceptsApi#getConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **search** | **String**| search string to match against concept names |
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **textFilter** | **String**| text filter to apply against set of concepts matched by the main search string  | [optional]
 **semanticType** | **String**| semanticType filter to apply against set of concepts matched by the main search string  | [optional]

### Return type

[**List&lt;InlineResponse200&gt;**](InlineResponse200.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

