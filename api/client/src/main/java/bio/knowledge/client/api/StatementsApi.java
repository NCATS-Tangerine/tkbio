/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.12
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
import bio.knowledge.client.model.BeaconAnnotation;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.client.model.BeaconStatementsQueryResult;
import bio.knowledge.client.model.BeaconStatementsQueryStatus;

public class StatementsApi {
    private ApiClient apiClient;

    public StatementsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public StatementsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for getEvidence */
    private com.squareup.okhttp.Call getEvidenceCall(String statementId, String keywords, Integer pageNumber, Integer pageSize, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/evidence/{statementId}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "statementId" + "\\}", apiClient.escapeString(statementId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (pageNumber != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageNumber", pageNumber));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageSize", pageSize));
        if (beacons != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "beacons", beacons));

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
    private com.squareup.okhttp.Call getEvidenceValidateBeforeCall(String statementId, String keywords, Integer pageNumber, Integer pageSize, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'statementId' is set
        if (statementId == null) {
            throw new ApiException("Missing the required parameter 'statementId' when calling getEvidence(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getEvidenceCall(statementId, keywords, pageNumber, pageSize, beacons, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @return List&lt;BeaconAnnotation&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<BeaconAnnotation> getEvidence(String statementId, String keywords, Integer pageNumber, Integer pageSize, List<Integer> beacons) throws ApiException {
        ApiResponse<List<BeaconAnnotation>> resp = getEvidenceWithHttpInfo(statementId, keywords, pageNumber, pageSize, beacons);
        return resp.getData();
    }

    /**
     * 
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @return ApiResponse&lt;List&lt;BeaconAnnotation&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<BeaconAnnotation>> getEvidenceWithHttpInfo(String statementId, String keywords, Integer pageNumber, Integer pageSize, List<Integer> beacons) throws ApiException {
        com.squareup.okhttp.Call call = getEvidenceValidateBeforeCall(statementId, keywords, pageNumber, pageSize, beacons, null, null);
        Type localVarReturnType = new TypeToken<List<BeaconAnnotation>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     * @param statementId (url-encoded) CURIE identifier of the concept-relationship statement (\&quot;assertion\&quot;, \&quot;claim\&quot;) for which associated evidence is sought, e.g. kbs:Q420626_P2175_Q126691  (required)
     * @param keywords (url-encoded, space delimited) keyword filter to apply against the label field of the annotation  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of cited references per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getEvidenceAsync(String statementId, String keywords, Integer pageNumber, Integer pageSize, List<Integer> beacons, final ApiCallback<List<BeaconAnnotation>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getEvidenceValidateBeforeCall(statementId, keywords, pageNumber, pageSize, beacons, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<BeaconAnnotation>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getStatementsQuery */
    private com.squareup.okhttp.Call getStatementsQueryCall(String queryId, List<Integer> beacons, Integer pageNumber, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/statements/data/{queryId}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "queryId" + "\\}", apiClient.escapeString(queryId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (beacons != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "beacons", beacons));
        if (pageNumber != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageNumber", pageNumber));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageSize", pageSize));

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
    private com.squareup.okhttp.Call getStatementsQueryValidateBeforeCall(String queryId, List<Integer> beacons, Integer pageNumber, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'queryId' is set
        if (queryId == null) {
            throw new ApiException("Missing the required parameter 'queryId' when calling getStatementsQuery(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getStatementsQueryCall(queryId, beacons, pageNumber, pageSize, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param queryId an active query identifier as returned by a POST of statement query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose statements are to be retrieved  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @return BeaconStatementsQueryResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BeaconStatementsQueryResult getStatementsQuery(String queryId, List<Integer> beacons, Integer pageNumber, Integer pageSize) throws ApiException {
        ApiResponse<BeaconStatementsQueryResult> resp = getStatementsQueryWithHttpInfo(queryId, beacons, pageNumber, pageSize);
        return resp.getData();
    }

    /**
     * 
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param queryId an active query identifier as returned by a POST of statement query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose statements are to be retrieved  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @return ApiResponse&lt;BeaconStatementsQueryResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BeaconStatementsQueryResult> getStatementsQueryWithHttpInfo(String queryId, List<Integer> beacons, Integer pageNumber, Integer pageSize) throws ApiException {
        com.squareup.okhttp.Call call = getStatementsQueryValidateBeforeCall(queryId, beacons, pageNumber, pageSize, null, null);
        Type localVarReturnType = new TypeToken<BeaconStatementsQueryResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param queryId an active query identifier as returned by a POST of statement query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose statements are to be retrieved  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getStatementsQueryAsync(String queryId, List<Integer> beacons, Integer pageNumber, Integer pageSize, final ApiCallback<BeaconStatementsQueryResult> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getStatementsQueryValidateBeforeCall(queryId, beacons, pageNumber, pageSize, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BeaconStatementsQueryResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getStatementsQueryStatus */
    private com.squareup.okhttp.Call getStatementsQueryStatusCall(String queryId, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/statements/status/{queryId}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "queryId" + "\\}", apiClient.escapeString(queryId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (beacons != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "beacons", beacons));

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
    private com.squareup.okhttp.Call getStatementsQueryStatusValidateBeforeCall(String queryId, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'queryId' is set
        if (queryId == null) {
            throw new ApiException("Missing the required parameter 'queryId' when calling getStatementsQueryStatus(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getStatementsQueryStatusCall(queryId, beacons, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Retrieves the status of a given query about the statements in the system 
     * @param queryId an active query identifier as returned by a POST of statements  query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned)  (optional)
     * @return BeaconStatementsQueryStatus
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BeaconStatementsQueryStatus getStatementsQueryStatus(String queryId, List<Integer> beacons) throws ApiException {
        ApiResponse<BeaconStatementsQueryStatus> resp = getStatementsQueryStatusWithHttpInfo(queryId, beacons);
        return resp.getData();
    }

    /**
     * 
     * Retrieves the status of a given query about the statements in the system 
     * @param queryId an active query identifier as returned by a POST of statements  query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned)  (optional)
     * @return ApiResponse&lt;BeaconStatementsQueryStatus&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BeaconStatementsQueryStatus> getStatementsQueryStatusWithHttpInfo(String queryId, List<Integer> beacons) throws ApiException {
        com.squareup.okhttp.Call call = getStatementsQueryStatusValidateBeforeCall(queryId, beacons, null, null);
        Type localVarReturnType = new TypeToken<BeaconStatementsQueryStatus>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Retrieves the status of a given query about the statements in the system 
     * @param queryId an active query identifier as returned by a POST of statements  query parameters. (required)
     * @param beacons subset of aggregator indices of beacons whose status is being polled (if omitted, then the status of all beacons from the query are returned)  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getStatementsQueryStatusAsync(String queryId, List<Integer> beacons, final ApiCallback<BeaconStatementsQueryStatus> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getStatementsQueryStatusValidateBeforeCall(queryId, beacons, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BeaconStatementsQueryStatus>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for postStatementsQuery */
    private com.squareup.okhttp.Call postStatementsQueryCall(String source, List<String> relations, String target, String keywords, List<String> categories, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/statements".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (source != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "source", source));
        if (relations != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "relations", relations));
        if (target != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "target", target));
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (categories != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "categories", categories));
        if (beacons != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "beacons", beacons));

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
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call postStatementsQueryValidateBeforeCall(String source, List<String> relations, String target, String keywords, List<String> categories, List<Integer> beacons, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'source' is set
        if (source == null) {
            throw new ApiException("Missing the required parameter 'source' when calling postStatementsQuery(Async)");
        }
        
        
        com.squareup.okhttp.Call call = postStatementsQueryCall(source, relations, target, keywords, categories, beacons, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Posts a query to retrieve concept-relations where either the subject or object concept matches a [CURIE-encoded &#39;source&#39;](https://www.w3.org/TR/curie/) clique identifier designating a set of exactly matching concepts. A &#39;target&#39; clique identifier may optionally be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a subset (array) of identifiers of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param categories a subset (array) of identifiers of concept categories to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes).  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @return BeaconStatementsQuery
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BeaconStatementsQuery postStatementsQuery(String source, List<String> relations, String target, String keywords, List<String> categories, List<Integer> beacons) throws ApiException {
        ApiResponse<BeaconStatementsQuery> resp = postStatementsQueryWithHttpInfo(source, relations, target, keywords, categories, beacons);
        return resp.getData();
    }

    /**
     * 
     * Posts a query to retrieve concept-relations where either the subject or object concept matches a [CURIE-encoded &#39;source&#39;](https://www.w3.org/TR/curie/) clique identifier designating a set of exactly matching concepts. A &#39;target&#39; clique identifier may optionally be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a subset (array) of identifiers of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param categories a subset (array) of identifiers of concept categories to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes).  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @return ApiResponse&lt;BeaconStatementsQuery&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BeaconStatementsQuery> postStatementsQueryWithHttpInfo(String source, List<String> relations, String target, String keywords, List<String> categories, List<Integer> beacons) throws ApiException {
        com.squareup.okhttp.Call call = postStatementsQueryValidateBeforeCall(source, relations, target, keywords, categories, beacons, null, null);
        Type localVarReturnType = new TypeToken<BeaconStatementsQuery>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Posts a query to retrieve concept-relations where either the subject or object concept matches a [CURIE-encoded &#39;source&#39;](https://www.w3.org/TR/curie/) clique identifier designating a set of exactly matching concepts. A &#39;target&#39; clique identifier may optionally be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a subset (array) of identifiers of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param categories a subset (array) of identifiers of concept categories to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept ((see [Biolink Model](https://biolink.github.io/biolink-model) for the full list of codes).  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call postStatementsQueryAsync(String source, List<String> relations, String target, String keywords, List<String> categories, List<Integer> beacons, final ApiCallback<BeaconStatementsQuery> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = postStatementsQueryValidateBeforeCall(source, relations, target, keywords, categories, beacons, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BeaconStatementsQuery>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
