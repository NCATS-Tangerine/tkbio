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

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2001DataPage;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ConceptsApi
 */
//@Ignore
public class ConceptsApiTest {
    private static final boolean RUNNING_CLIENT_LOCALLY = true;
    
	private final static ConceptsApi api = new ConceptsApi();
    static {
    	if (RUNNING_CLIENT_LOCALLY) {
	    	ApiClient apiClient = new ApiClient();
	    	apiClient.setBasePath("http://localhost:8080/api/");
	    	api.setApiClient(apiClient);
    	}
    }

    
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
        String conceptId = "3";
        List<InlineResponse200> responses = api.getConceptDetails(conceptId);
        
        assertTrue(responses != null);
        
        for (InlineResponse200 response : responses) {
        	assertTrue(response.getName().equals("INSL3"));
        }
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
        String q = "chocolate milk";
        String sg = "GENE OBJC";
        Integer pageNumber = 0;
        Integer pageSize = 20;
        InlineResponse2001 response = api.getConcepts(q, sg, pageNumber, pageSize);
        
        assertTrue(response != null);
        
        boolean milkChocolateFound = false;
        int count = 0;
        for (InlineResponse2001DataPage page : response.getDataPage()) {
        	count += 1;
        	if (page.getName().equals("Milk chocolate")) {
        		milkChocolateFound = true;
        	}
        }
        
        assertTrue(count == 18);
        assertTrue(milkChocolateFound);
    }
    
}
