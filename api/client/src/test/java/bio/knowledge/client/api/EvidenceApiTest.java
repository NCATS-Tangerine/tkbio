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
import bio.knowledge.client.model.Annotation;

/**
 * API tests for EvidenceApi
 */
@Ignore
public class EvidenceApiTest {

    private final EvidenceApi api = new EvidenceApi();

    
    /**
     * 
     *
     * Retrieves a (paged) list of annotations cited as evidence for a specified concept-relationship statement 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getEvidenceTest() throws ApiException {
        String statementId = null;
        String keywords = null;
        Integer pageNumber = null;
        Integer pageSize = null;
        List<String> beacons = null;
        String sessionId = null;
        List<Annotation> response = api.getEvidence(statementId, keywords, pageNumber, pageSize, beacons, sessionId);

        // TODO: test validations
    }
    
}
