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

import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConceptCategory;
import bio.knowledge.client.model.BeaconKnowledgeBeacon;
import bio.knowledge.client.model.BeaconKnowledgeMap;
import bio.knowledge.client.model.BeaconLogEntry;
import bio.knowledge.client.model.BeaconPredicate;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for MetadataApi
 */
@Ignore
public class MetadataApiTest {

    private final MetadataApi api = new MetadataApi();

    
    /**
     * 
     *
     * Get a list of all of the knowledge beacons that the aggregator can query 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getBeaconsTest() throws ApiException {
        List<BeaconKnowledgeBeacon> response = api.getBeacons();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Get a list of semantic categories and number of instances in each  available knowledge beacon, including associated beacon-specific metadata 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getConceptCategoriesTest() throws ApiException {
        List<Integer> beacons = null;
        List<BeaconConceptCategory> response = api.getConceptCategories(beacons);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Get a log of the system errors associated with a specified query 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getErrorsTest() throws ApiException {
        String queryId = null;
        List<BeaconLogEntry> response = api.getErrors(queryId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Get a high level knowledge map of the all the beacons specified by triplets of subject concept category, relationship predicate and concept object category 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getKnowledgeMapTest() throws ApiException {
        List<Integer> beacons = null;
        List<BeaconKnowledgeMap> response = api.getKnowledgeMap(beacons);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Get a list of predicates used in statements issued by the knowledge source 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getPredicatesTest() throws ApiException {
        List<Integer> beacons = null;
        List<BeaconPredicate> response = api.getPredicates(beacons);

        // TODO: test validations
    }
    
}
