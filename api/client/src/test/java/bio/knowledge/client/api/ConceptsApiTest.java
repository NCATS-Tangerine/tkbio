/*
 * Translator Knowledge.Bio API
 * Documentation of the Translator Knowledge.Bio (TKBio) knowledge sources query web service application programming interfaces. Learn more about [TKBio](https://github.com/STARInformatics/tkbio) 
 *
 * OpenAPI spec version: 4.0.4
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ConceptsApi
 */
@Ignore
public class ConceptsApiTest {

    private final ConceptsApi api = new ConceptsApi();

    
    /**
     * 
     *
     * Retrieves details for a specified concepts in the system 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getConceptDetailsTest() throws ApiException {
        String conceptId = null;
        List<InlineResponse200> response = api.getConceptDetails(conceptId);

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
        List<String> q = null;
        List<String> sg = null;
        Integer pageNumber = null;
        Integer pageSize = null;
        InlineResponse2001 response = api.getConcepts(q, sg, pageNumber, pageSize);

        // TODO: test validations
    }
    
}
