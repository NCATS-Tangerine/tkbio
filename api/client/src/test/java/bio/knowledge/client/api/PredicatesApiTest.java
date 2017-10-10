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

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.Predicate;

/**
 * API tests for PredicatesApi
 */
@Ignore
public class PredicatesApiTest {

    private final PredicatesApi api = new PredicatesApi();

    
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
        List<Predicate> response = api.getPredicates();

        // TODO: test validations
    }
    
}
