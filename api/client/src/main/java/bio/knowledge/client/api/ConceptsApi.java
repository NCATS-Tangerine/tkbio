/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API). 
 *
 * OpenAPI spec version: 1.0.4
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.ApiResponse;
import bio.knowledge.client.Configuration;
import bio.knowledge.client.Pair;
import bio.knowledge.client.ProgressRequestBody;
import bio.knowledge.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import bio.knowledge.client.model.Concept;
import bio.knowledge.client.model.ConceptDetail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptsApi {
    private ApiClient apiClient;

    public ConceptsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ConceptsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for getConceptDetails */
    private com.squareup.okhttp.Call getConceptDetailsCall(String conceptId, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/concepts/{conceptId}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "conceptId" + "\\}", apiClient.escapeString(conceptId.toString()));

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
    private com.squareup.okhttp.Call getConceptDetailsValidateBeforeCall(String conceptId, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'conceptId' is set
        if (conceptId == null) {
            throw new ApiException("Missing the required parameter 'conceptId' when calling getConceptDetails(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getConceptDetailsCall(conceptId, beacons, sessionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE identifier of a concept known the given knowledge source 
     * @param conceptId (url-encoded) CURIE identifier of concept of interest, e.g. wd:Q126691 (required)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return List&lt;ConceptDetail&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<ConceptDetail> getConceptDetails(String conceptId, List<String> beacons, String sessionId) throws ApiException {
        ApiResponse<List<ConceptDetail>> resp = getConceptDetailsWithHttpInfo(conceptId, beacons, sessionId);
        return resp.getData();
    }

    /**
     * 
     * Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE identifier of a concept known the given knowledge source 
     * @param conceptId (url-encoded) CURIE identifier of concept of interest, e.g. wd:Q126691 (required)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return ApiResponse&lt;List&lt;ConceptDetail&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<ConceptDetail>> getConceptDetailsWithHttpInfo(String conceptId, List<String> beacons, String sessionId) throws ApiException {
        com.squareup.okhttp.Call call = getConceptDetailsValidateBeforeCall(conceptId, beacons, sessionId, null, null);
        Type localVarReturnType = new TypeToken<List<ConceptDetail>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE identifier of a concept known the given knowledge source 
     * @param conceptId (url-encoded) CURIE identifier of concept of interest, e.g. wd:Q126691 (required)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getConceptDetailsAsync(String conceptId, List<String> beacons, String sessionId, final ApiCallback<List<ConceptDetail>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getConceptDetailsValidateBeforeCall(conceptId, beacons, sessionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<ConceptDetail>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getConcepts */
    private com.squareup.okhttp.Call getConceptsCall(String keywords, String semgroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/concepts".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (semgroups != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "semgroups", semgroups));
        if (pageNumber != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageNumber", pageNumber));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageSize", pageSize));
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
    private com.squareup.okhttp.Call getConceptsValidateBeforeCall(String keywords, String semgroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'keywords' is set
        if (keywords == null) {
            throw new ApiException("Missing the required parameter 'keywords' when calling getConcepts(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getConceptsCall(keywords, semgroups, pageNumber, pageSize, beacons, sessionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Retrieves a (paged) list of concepts in the system 
     * @param keywords a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes. (required)
     * @param semgroups a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return List&lt;Concept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Concept> getConcepts(String keywords, String semgroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId) throws ApiException {
        ApiResponse<List<Concept>> resp = getConceptsWithHttpInfo(keywords, semgroups, pageNumber, pageSize, beacons, sessionId);
        return resp.getData();
    }

    /**
     * 
     * Retrieves a (paged) list of concepts in the system 
     * @param keywords a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes. (required)
     * @param semgroups a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return ApiResponse&lt;List&lt;Concept&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Concept>> getConceptsWithHttpInfo(String keywords, String semgroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId) throws ApiException {
        com.squareup.okhttp.Call call = getConceptsValidateBeforeCall(keywords, semgroups, pageNumber, pageSize, beacons, sessionId, null, null);
        Type localVarReturnType = new TypeToken<List<Concept>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieves a (paged) list of concepts in the system 
     * @param keywords a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms, e.g. diabetes. (required)
     * @param semgroups a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getConceptsAsync(String keywords, String semgroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ApiCallback<List<Concept>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getConceptsValidateBeforeCall(keywords, semgroups, pageNumber, pageSize, beacons, sessionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Concept>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
