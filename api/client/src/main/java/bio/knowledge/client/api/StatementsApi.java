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


import bio.knowledge.client.model.Statement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /* Build call for getStatements */
    private com.squareup.okhttp.Call getStatementsCall(List<String> c, Integer pageNumber, Integer pageSize, String keywords, String semgroups, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/statements".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (c != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "c", c));
        if (pageNumber != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageNumber", pageNumber));
        if (pageSize != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "pageSize", pageSize));
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (semgroups != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "semgroups", semgroups));
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
    private com.squareup.okhttp.Call getStatementsValidateBeforeCall(List<String> c, Integer pageNumber, Integer pageSize, String keywords, String semgroups, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'c' is set
        if (c == null) {
            throw new ApiException("Missing the required parameter 'c' when calling getStatements(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getStatementsCall(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 
     * @param c set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements, e.g. wd:Q126691, wd:Q420626  (required)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts  (optional)
     * @param semgroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return List&lt;Statement&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Statement> getStatements(List<String> c, Integer pageNumber, Integer pageSize, String keywords, String semgroups, List<String> beacons, String sessionId) throws ApiException {
        ApiResponse<List<Statement>> resp = getStatementsWithHttpInfo(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId);
        return resp.getData();
    }

    /**
     * 
     * Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 
     * @param c set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements, e.g. wd:Q126691, wd:Q420626  (required)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts  (optional)
     * @param semgroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return ApiResponse&lt;List&lt;Statement&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Statement>> getStatementsWithHttpInfo(List<String> c, Integer pageNumber, Integer pageSize, String keywords, String semgroups, List<String> beacons, String sessionId) throws ApiException {
        com.squareup.okhttp.Call call = getStatementsValidateBeforeCall(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId, null, null);
        Type localVarReturnType = new TypeToken<List<Statement>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 
     * @param c set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements, e.g. wd:Q126691, wd:Q420626  (required)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts  (optional)
     * @param semgroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param beacons set of IDs of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getStatementsAsync(List<String> c, Integer pageNumber, Integer pageSize, String keywords, String semgroups, List<String> beacons, String sessionId, final ApiCallback<List<Statement>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getStatementsValidateBeforeCall(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Statement>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
