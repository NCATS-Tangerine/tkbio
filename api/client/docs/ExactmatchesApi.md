# ExactmatchesApi

All URIs are relative to *http://api.knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getExactMatchesToConcept**](ExactmatchesApi.md#getExactMatchesToConcept) | **GET** /exactmatches/{conceptId} | 
[**getExactMatchesToConceptList**](ExactmatchesApi.md#getExactMatchesToConceptList) | **GET** /exactmatches | 


<a name="getExactMatchesToConcept"></a>
# **getExactMatchesToConcept**
> List&lt;String&gt; getExactMatchesToConcept(conceptId)



Retrieves a list of qualified identifiers of \&quot;exact match\&quot; concepts, [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch) associated with a specified (url-encoded) CURIE (without brackets) concept object identifier,  typically, of a concept selected from the list of concepts originally returned by a /concepts API call on a given KS.  

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ExactmatchesApi;


ExactmatchesApi apiInstance = new ExactmatchesApi();
String conceptId = "conceptId_example"; // String | (url-encoded) CURIE identifier of the concept to be matched
try {
    List<String> result = apiInstance.getExactMatchesToConcept(conceptId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExactmatchesApi#getExactMatchesToConcept");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conceptId** | **String**| (url-encoded) CURIE identifier of the concept to be matched |

### Return type

**List&lt;String&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getExactMatchesToConceptList"></a>
# **getExactMatchesToConceptList**
> List&lt;String&gt; getExactMatchesToConceptList(c)



Given an input list of [CURIE](https://www.w3.org/TR/curie/) identifiers of known exactly matched concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch), retrieves the list of [CURIE](https://www.w3.org/TR/curie/) identifiers of **additional** concepts that are deemed by the given knowledge source to be exact matches to one or more of the input concepts.  If an empty set is returned, the it can be assumed that the given knowledge source does not know of any new equivalent concepts to add to the input set. 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.ExactmatchesApi;


ExactmatchesApi apiInstance = new ExactmatchesApi();
List<String> c = Arrays.asList("c_example"); // List<String> | set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). 
try {
    List<String> result = apiInstance.getExactMatchesToConceptList(c);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExactmatchesApi#getExactMatchesToConceptList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **c** | [**List&lt;String&gt;**](String.md)| set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch).  |

### Return type

**List&lt;String&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

