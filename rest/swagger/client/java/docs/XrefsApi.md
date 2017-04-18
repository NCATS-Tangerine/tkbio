# XrefsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConceptXRefs**](XrefsApi.md#getConceptXRefs) | **GET** /xrefs/{conceptId} | 
[**getXRefConcepts**](XrefsApi.md#getXRefConcepts) | **GET** /xrefs | 
[**postXRefQuery**](XrefsApi.md#postXRefQuery) | **POST** /xrefs | 


<a name="getConceptXRefs"></a>
# **getConceptXRefs**
> List&lt;InlineResponse2001&gt; getConceptXRefs(conceptId)



Retrieves a list of cross-referencing identifiers associated with an specifically identified concept, typically, a concept selected from the list of concepts originally returned by a /concepts API call. This API call will typically be run against the same KS (only) from which the originally selected concept was retrieved. 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.XrefsApi;


XrefsApi apiInstance = new XrefsApi();
String conceptId = "conceptId_example"; // String | local object identifier of the concept to be matched
try {
    List<InlineResponse2001> result = apiInstance.getConceptXRefs(conceptId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling XrefsApi#getConceptXRefs");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conceptId** | **String**| local object identifier of the concept to be matched |

### Return type

[**List&lt;InlineResponse2001&gt;**](InlineResponse2001.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getXRefConcepts"></a>
# **getXRefConcepts**
> List&lt;InlineResponse2002&gt; getXRefConcepts(queryId)



Retrieves the list of concepts with that match one or more cross-references posted in the previously initiated query session. This new list of concepts is returned with the full list of their associated cross-references. 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.XrefsApi;


XrefsApi apiInstance = new XrefsApi();
String queryId = "queryId_example"; // String | identifier of the query session established by prior POST'ing of a list of cross-references identifiers of interest 
try {
    List<InlineResponse2002> result = apiInstance.getXRefConcepts(queryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling XrefsApi#getXRefConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| identifier of the query session established by prior POST&#39;ing of a list of cross-references identifiers of interest  |

### Return type

[**List&lt;InlineResponse2002&gt;**](InlineResponse2002.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="postXRefQuery"></a>
# **postXRefQuery**
> InlineResponse201 postXRefQuery(identifiers)



Posts a list of cross-reference identifiers to a given KS endpoint, initiating a query session 

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.XrefsApi;


XrefsApi apiInstance = new XrefsApi();
List<Identifiers> identifiers = Arrays.asList(new Identifiers()); // List<Identifiers> | list of cross-reference identifiers to be used in a search for equivalent concepts 
try {
    InlineResponse201 result = apiInstance.postXRefQuery(identifiers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling XrefsApi#postXRefQuery");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **identifiers** | [**List&lt;Identifiers&gt;**](Identifiers.md)| list of cross-reference identifiers to be used in a search for equivalent concepts  |

### Return type

[**InlineResponse201**](InlineResponse201.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

