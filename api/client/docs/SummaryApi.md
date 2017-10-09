# SummaryApi

All URIs are relative to *http://kba.ncats.io/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**linkedTypes**](SummaryApi.md#linkedTypes) | **GET** /types | 


<a name="linkedTypes"></a>
# **linkedTypes**
> List&lt;Summary&gt; linkedTypes(beacons, sessionId)



Get a list of types and # of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.SummaryApi;


SummaryApi apiInstance = new SummaryApi();
List<String> beacons = Arrays.asList("beacons_example"); // List<String> | set of IDs of beacons to be used as knowledge sources for the query 
String sessionId = "sessionId_example"; // String | client-defined session identifier 
try {
    List<Summary> result = apiInstance.linkedTypes(beacons, sessionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SummaryApi#linkedTypes");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **beacons** | [**List&lt;String&gt;**](String.md)| set of IDs of beacons to be used as knowledge sources for the query  | [optional]
 **sessionId** | **String**| client-defined session identifier  | [optional]

### Return type

[**List&lt;Summary&gt;**](Summary.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

