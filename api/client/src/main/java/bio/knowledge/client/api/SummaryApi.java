/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API). 
 *
 * OpenAPI spec version: 1.0.3
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.ApiResponse;
import bio.knowledge.client.Configuration;
import bio.knowledge.client.Pair;
import bio.knowledge.client.ProgressRequestBody;
import bio.knowledge.client.ProgressResponseBody;
import bio.knowledge.client.model.Summary;

public class SummaryApi {
    private ApiClient apiClient;

    public SummaryApi() {
        this(Configuration.getDefaultApiClient());
    }

    public SummaryApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for linkedTypes */
    private com.squareup.okhttp.Call linkedTypesCall(List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/types".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (beacons != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "beacons", beacons));
        if (sessionId != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "sessionId", sessionId));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call linkedTypesValidateBeforeCall(List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        
        com.squareup.okhttp.Call call = linkedTypesCall(beacons, sessionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Get a list of types and # of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return List&lt;Summary&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Summary> linkedTypes(List<String> beacons, String sessionId) throws ApiException {
        ApiResponse<List<Summary>> resp = linkedTypesWithHttpInfo(beacons, sessionId);
        return resp.getData();
    }

    /**
     * 
     * Get a list of types and # of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return ApiResponse&lt;List&lt;Summary&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Summary>> linkedTypesWithHttpInfo(List<String> beacons, String sessionId) throws ApiException {
        com.squareup.okhttp.Call call = linkedTypesValidateBeforeCall(beacons, sessionId, null, null);
        Type localVarReturnType = new TypeToken<List<Summary>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get a list of types and # of instances in the knowledge source, and a link to the API call for the list of equivalent terminology 
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call linkedTypesAsync(List<String> beacons, String sessionId, final ApiCallback<List<Summary>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = linkedTypesValidateBeforeCall(beacons, sessionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Summary>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
