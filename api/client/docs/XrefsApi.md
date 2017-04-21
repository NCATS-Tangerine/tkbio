# XrefsApi

All URIs are relative to *http://knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConceptXRefs**](XrefsApi.md#getConceptXRefs) | **GET** /xrefs/{conceptId} | 
[**getXRefConcepts**](XrefsApi.md#getXRefConcepts) | **GET** /xrefs | 


<a name="getConceptXRefs"></a>
# **getConceptXRefs**
> List&lt;String&gt; getConceptXRefs(conceptId)



Retrieves a list of cross-referencing identifiers associated with an specifically identified concept, typically, a concept selected from the list of concepts originally returned by a /concepts API call. This API call will typically be run against the same KS (only) from which the originally selected concept was retrieved. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.XrefsApi;


XrefsApi apiInstance = new XrefsApi();
String conceptId = "conceptId_example"; // String | local object identifier of the concept to be matched
try {
    List<String> result = apiInstance.getConceptXRefs(conceptId);
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

**List&lt;String&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getXRefConcepts"></a>
# **getXRefConcepts**
> List&lt;InlineResponse2002&gt; getXRefConcepts(xi)



Given an input list of cross-reference identifiers, retrieves the list of identifiers of concepts that match one or more cross-references posted in the previously initiated query session. This new list of concept identifierss is returned with the full list of their associated cross-reference identifiers. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.XrefsApi;


XrefsApi apiInstance = new XrefsApi();
List<String> xi = Arrays.asList("xi_example"); // List<String> | list of cross-reference identifiers to be used in a search for equivalent concepts 
try {
    List<InlineResponse2002> result = apiInstance.getXRefConcepts(xi);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling XrefsApi#getXRefConcepts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **xi** | [**List&lt;String&gt;**](String.md)| list of cross-reference identifiers to be used in a search for equivalent concepts  |

### Return type

[**List&lt;InlineResponse2002&gt;**](InlineResponse2002.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

