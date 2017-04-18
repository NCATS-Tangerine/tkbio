# StatementsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatements**](StatementsApi.md#getStatements) | **GET** /statements | 
[**postStatementQuery**](StatementsApi.md#postStatementQuery) | **POST** /statements | 


<a name="getStatements"></a>
# **getStatements**
> List&lt;InlineResponse2003&gt; getStatements(queryId, pageNumber, pageSize, textFilter, semanticType)



Retrieves a paged list of concept-relations with either the subject or object concept matching the list of concepts previously POST&#39;ed into the specified query session 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
String queryId = "queryId_example"; // String | identifier of the query session established by prior POST'ing of a list of concept identifiers of interest 
Integer pageNumber = 56; // Integer | (1-based) number of the page to be returned in a paged set of query results 
Integer pageSize = 56; // Integer | number of concepts per page to be returned in a paged set of query results 
String textFilter = "textFilter_example"; // String | text filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search 
String semanticType = "semanticType_example"; // String | semanticType filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search main search string 
try {
    List<InlineResponse2003> result = apiInstance.getStatements(queryId, pageNumber, pageSize, textFilter, semanticType);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#getStatements");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| identifier of the query session established by prior POST&#39;ing of a list of concept identifiers of interest  |
 **pageNumber** | **Integer**| (1-based) number of the page to be returned in a paged set of query results  | [optional]
 **pageSize** | **Integer**| number of concepts per page to be returned in a paged set of query results  | [optional]
 **textFilter** | **String**| text filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search  | [optional]
 **semanticType** | **String**| semanticType filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search main search string  | [optional]

### Return type

[**List&lt;InlineResponse2003&gt;**](InlineResponse2003.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postStatementQuery"></a>
# **postStatementQuery**
> InlineResponse201 postStatementQuery(identifiers)



Posts a list of concept identifiers to a given KS endpoint, initiating a query session for concept-relation statement retrieval 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.StatementsApi;


StatementsApi apiInstance = new StatementsApi();
List<Identifiers> identifiers = Arrays.asList(new Identifiers()); // List<Identifiers> | list of concept identifiers to be used in a search for associated concept-relation statements 
try {
    InlineResponse201 result = apiInstance.postStatementQuery(identifiers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatementsApi#postStatementQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **identifiers** | [**List&lt;Identifiers&gt;**](Identifiers.md)| list of concept identifiers to be used in a search for associated concept-relation statements  |

### Return type

[**InlineResponse201**](InlineResponse201.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

