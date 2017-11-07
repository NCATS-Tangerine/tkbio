/*
 * Translator Knowledge Beacon API
 * This is the Translator Knowledge Beacon Aggregator web service application programming interface (API). 
 *
 * OpenAPI spec version: 1.0.12
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconConceptWithDetails;

/**
 * API tests for ConceptsApi
 */
@Ignore
public class ConceptsApiTest {

    private final ConceptsApi api = new ConceptsApi();

    
    /**
     * 
     *
     * Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE identifier of a concept known the given knowledge source. 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getConceptDetailsTest() throws ApiException {
        String conceptId = null;
        List<String> beacons = null;
        String sessionId = null;
        List<BeaconConceptWithDetails> response = api.getConceptDetails(conceptId, beacons, sessionId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Retrieves a (paged) list of concepts in the system 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getConceptsTest() throws ApiException {
        String keywords = null;
        String semanticGroups = null;
        Integer pageNumber = null;
        Integer pageSize = null;
        List<String> beacons = null;
        String sessionId = null;
        List<BeaconConcept> response = api.getConcepts(keywords, semanticGroups, pageNumber, pageSize, beacons, sessionId);

        // TODO: test validations
    }
    
}
