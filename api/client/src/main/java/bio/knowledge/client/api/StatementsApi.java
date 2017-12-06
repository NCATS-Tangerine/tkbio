/*
 * Translator Knowledge Beacon Aggregator API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API) that provides integrated access to a pool of knowledge sources publishing concepts and relations through the Translator Knowledge Beacon API. This API is similar to that of the latter mentioned API with the addition of some extra informative endpoints plus session identifier and beacon indices. These latter identifiers are locally assigned numeric indices provided to track the use of specific registered beacons within the aggregator API itself. 
 *
 * OpenAPI spec version: 1.0.6
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
import bio.knowledge.client.model.BeaconStatement;

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
    private com.squareup.okhttp.Call getStatementsCall(String source, String relations, String target, String keywords, String semanticGroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/statements".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (source != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "source", source));
        if (relations != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "relations", relations));
        if (target != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "target", target));
        if (keywords != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "keywords", keywords));
        if (semanticGroups != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "semanticGroups", semanticGroups));
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
    private com.squareup.okhttp.Call getStatementsValidateBeforeCall(String source, String relations, String target, String keywords, String semanticGroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'source' is set
        if (source == null) {
            throw new ApiException("Missing the required parameter 'source' when calling getStatements(Async)");
        }
        
        
        com.squareup.okhttp.Call call = getStatementsCall(source, relations, target, keywords, semanticGroups, pageNumber, pageSize, beacons, sessionId, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a (url-encoded, space-delimited) string of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param semanticGroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept  (see [Semantic Groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return List&lt;BeaconStatement&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<BeaconStatement> getStatements(String source, String relations, String target, String keywords, String semanticGroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId) throws ApiException {
        ApiResponse<List<BeaconStatement>> resp = getStatementsWithHttpInfo(source, relations, target, keywords, semanticGroups, pageNumber, pageSize, beacons, sessionId);
        return resp.getData();
    }

    /**
     * 
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a (url-encoded, space-delimited) string of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param semanticGroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept  (see [Semantic Groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @return ApiResponse&lt;List&lt;BeaconStatement&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<BeaconStatement>> getStatementsWithHttpInfo(String source, String relations, String target, String keywords, String semanticGroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId) throws ApiException {
        com.squareup.okhttp.Call call = getStatementsValidateBeforeCall(source, relations, target, keywords, semanticGroups, pageNumber, pageSize, beacons, sessionId, null, null);
        Type localVarReturnType = new TypeToken<List<BeaconStatement>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Given a specification [CURIE-encoded](https://www.w3.org/TR/curie/) a &#39;source&#39; clique identifier for a set of exactly matching concepts,  retrieves a paged list of concept-relations where either the subject or object concept matches the &#39;source&#39; clique identifier.  Optionally, a &#39;target&#39; clique identifier may also be given, in which case the &#39;target&#39; clique identifier should match the concept clique opposing the &#39;source&#39;, that is, if the &#39;source&#39; matches a subject, then the  &#39;target&#39; should match the object of a given statement (or vice versa). 
     * @param source a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;source&#39; clique, as defined by other endpoints of the beacon aggregator API.   (required)
     * @param relations a (url-encoded, space-delimited) string of predicate relation identifiers with which to constrain the statement relations retrieved  for the given query seed concept. The predicate ids sent should  be as published by the beacon-aggregator by the /predicates API endpoint.  (optional)
     * @param target a [CURIE-encoded](https://www.w3.org/TR/curie/) identifier of the  exactly matching &#39;target&#39; clique, as defined by other endpoints of the beacon aggregator API.   (optional)
     * @param keywords a (url-encoded, space-delimited) string of keywords or substrings against which to match the &#39;target&#39; concept or &#39;predicate&#39; names of the set of concept-relations matched by the &#39;source&#39; concepts.  (optional)
     * @param semanticGroups a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain &#39;target&#39; concepts associated with the given &#39;source&#39; concept  (see [Semantic Groups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes)  (optional)
     * @param pageNumber (1-based) number of the page to be returned in a paged set of query results  (optional)
     * @param pageSize number of concepts per page to be returned in a paged set of query results  (optional)
     * @param beacons set of aggregator indices of beacons to be used as knowledge sources for the query  (optional)
     * @param sessionId client-defined session identifier  (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getStatementsAsync(String source, String relations, String target, String keywords, String semanticGroups, Integer pageNumber, Integer pageSize, List<String> beacons, String sessionId, final ApiCallback<List<BeaconStatement>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = getStatementsValidateBeforeCall(source, relations, target, keywords, semanticGroups, pageNumber, pageSize, beacons, sessionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<BeaconStatement>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
