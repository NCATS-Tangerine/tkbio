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

import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.Statement;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for StatementsApi
 */
@Ignore
public class StatementsApiTest {

    private final StatementsApi api = new StatementsApi();

    
    /**
     * 
     *
     * Given a list of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, retrieves a paged list of concept-relations where either the subject or object concept matches at least one concept in the input list 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getStatementsTest() throws ApiException {
        List<String> c = null;
        Integer pageNumber = null;
        Integer pageSize = null;
        String keywords = null;
        String semgroups = null;
        List<String> beacons = null;
        String sessionId = null;
        List<Statement> response = api.getStatements(c, pageNumber, pageSize, keywords, semgroups, beacons, sessionId);

        // TODO: test validations
    }
    
}
