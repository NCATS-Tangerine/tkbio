# AggregatorApi

All URIs are relative to *http://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getBeacons**](AggregatorApi.md#getBeacons) | **GET** /beacons | 
[**getErrors**](AggregatorApi.md#getErrors) | **GET** /errorlog | 


<a name="getBeacons"></a>
# **getBeacons**
> List&lt;KnowledgeBeacon&gt; getBeacons(sessionId)



Get a list of the knowledge beacons that the aggregator can query 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.AggregatorApi;


AggregatorApi apiInstance = new AggregatorApi();
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<KnowledgeBeacon> result = apiInstance.getBeacons(sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregatorApi#getBeacons");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;KnowledgeBeacon&gt;**](KnowledgeBeacon.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getErrors"></a>
# **getErrors**
> List&lt;LogEntry&gt; getErrors(sessionId)



Get a log of the most recent errors in this session 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.AggregatorApi;


AggregatorApi apiInstance = new AggregatorApi();
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<LogEntry> result = apiInstance.getErrors(sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregatorApi#getErrors");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **sessionId** | **String**| client-defined session identifier  |

### Return type

[**List&lt;LogEntry&gt;**](LogEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

