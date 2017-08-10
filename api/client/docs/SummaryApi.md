# SummaryApi

All URIs are relative to *http://api.knowledge.bio/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**linkedTypes**](SummaryApi.md#linkedTypes) | **GET** /types | 


<a name="linkedTypes"></a>
# **linkedTypes**
> List&lt;SummaryEntry&gt; linkedTypes()



Get a list of types and # of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 

### Example
```java
// Import classes:
//import bio.knowledge.client.ApiException;
//import bio.knowledge.client.api.SummaryApi;


SummaryApi apiInstance = new SummaryApi();
try {
    List<SummaryEntry> result = apiInstance.linkedTypes();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SummaryApi#linkedTypes");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;SummaryEntry&gt;**](SummaryEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

