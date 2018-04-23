# MetadataApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getBeacons**](MetadataApi.md#getBeacons) | **GET** /beacons | 
[**getConceptTypes**](MetadataApi.md#getConceptTypes) | **GET** /types | 
[**getErrors**](MetadataApi.md#getErrors) | **GET** /errorlog | 
[**getKnowledgeMap**](MetadataApi.md#getKnowledgeMap) | **GET** /kmap | 
[**getPredicates**](MetadataApi.md#getPredicates) | **GET** /predicates | 


<a name="getBeacons"></a>
# **getBeacons**
> List&lt;ServerKnowledgeBeacon&gt; getBeacons()



Get a list of all of the knowledge beacons that the aggregator can query 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.MetadataApi;


MetadataApi apiInstance = new MetadataApi();
try {
    List<ServerKnowledgeBeacon> result = apiInstance.getBeacons();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#getBeacons");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;ServerKnowledgeBeacon&gt;**](ServerKnowledgeBeacon.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConceptTypes"></a>
# **getConceptTypes**
> List&lt;ServerConceptTypes&gt; getConceptTypes(beacons)



Get a list of types and number of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.MetadataApi;


MetadataApi apiInstance = new MetadataApi();
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons to constrain types returned 
try {
    List<ServerConceptTypes> result = apiInstance.getConceptTypes(beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#getConceptTypes");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons to constrain types returned  | [optional]

### Return type

[**List&lt;ServerConceptTypes&gt;**](ServerConceptTypes.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getErrors"></a>
# **getErrors**
> List&lt;ServerLogEntry&gt; getErrors(queryId)



Get a log of the system errors associated with a specified query 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.MetadataApi;


MetadataApi apiInstance = new MetadataApi();
String queryId = "queryId_example"; // String | query identifier returned from a POSTed query 
try {
    List<ServerLogEntry> result = apiInstance.getErrors(queryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#getErrors");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **queryId** | **String**| query identifier returned from a POSTed query  |

### Return type

[**List&lt;ServerLogEntry&gt;**](ServerLogEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getKnowledgeMap"></a>
# **getKnowledgeMap**
> List&lt;ServerKnowledgeMap&gt; getKnowledgeMap(beacons)



Get a high level knowledge map of the all the beacons specified by triplets of subject concept type, relationship predicate and concept object type 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.MetadataApi;


MetadataApi apiInstance = new MetadataApi();
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons constraining knowledge maps returned  
try {
    List<ServerKnowledgeMap> result = apiInstance.getKnowledgeMap(beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#getKnowledgeMap");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons constraining knowledge maps returned   | [optional]

### Return type

[**List&lt;ServerKnowledgeMap&gt;**](ServerKnowledgeMap.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getPredicates"></a>
# **getPredicates**
> List&lt;ServerPredicates&gt; getPredicates(beacons)



Get a list of predicates used in statements issued by the knowledge source 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.MetadataApi;


MetadataApi apiInstance = new MetadataApi();
List<Integer> beacons = Arrays.asList(56); // List<Integer> | set of aggregator indices of beacons to constrain predicates returned 
try {
    List<ServerPredicates> result = apiInstance.getPredicates(beacons);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#getPredicates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **beacons** | [**List&lt;Integer&gt;**](Integer.md)| set of aggregator indices of beacons to constrain predicates returned  | [optional]

### Return type

[**List&lt;ServerPredicates&gt;**](ServerPredicates.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

