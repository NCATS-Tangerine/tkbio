# AggregatorApi

All URIs are relative to *https://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getBeacons**](AggregatorApi.md#getBeacons) | **GET** /beacons | 
[**getErrors**](AggregatorApi.md#getErrors) | **GET** /errorlog | 


<a name="getBeacons"></a>
# **getBeacons**
> List&lt;BeaconMetadata&gt; getBeacons()



Get a list of all of the knowledge beacons that the aggregator can query 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.AggregatorApi;


AggregatorApi apiInstance = new AggregatorApi();
try {
    List<BeaconMetadata> result = apiInstance.getBeacons();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AggregatorApi#getBeacons");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;BeaconMetadata&gt;**](BeaconMetadata.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getErrors"></a>
# **getErrors**
> List&lt;BeaconLogEntry&gt; getErrors(sessionId)



Get a log of the most recent errors in this session 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.AggregatorApi;


AggregatorApi apiInstance = new AggregatorApi();
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<BeaconLogEntry> result = apiInstance.getErrors(sessionId);
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

[**List&lt;BeaconLogEntry&gt;**](BeaconLogEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

