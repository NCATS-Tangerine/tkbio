package bio.knowledge.server.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;
import bio.knowledge.server.utilities.UrlDecoder;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-05T22:03:23.165-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {

	@Autowired
	ConceptRepository conceptRepository;

    public ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "(url-encoded) CURIE identifier of concept of interest",required=true ) @PathVariable("conceptId") String conceptId) {
    	Concept concept = conceptRepository.apiGetConceptById(conceptId);
		List<InlineResponse200> responses = new ArrayList<InlineResponse200>();

		if (concept != null) {
			InlineResponse200 response = new InlineResponse200();
			response.setDefinition(concept.getDescription());
			response.setId(concept.getId());
			response.setName(concept.getName());
			String semanticType = concept.getSemanticGroup().name();
			response.setSemanticGroup(semanticType);
			response.setSynonyms(Arrays.asList(concept.getSynonyms().split("\\|")));

			responses.add(response);
		}

		return ResponseEntity.ok(responses);
    }

    public ResponseEntity<List<InlineResponse2001>> getConcepts( @NotNull @ApiParam(value = "a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms", required = true) @RequestParam(value = "keywords", required = true) String keywords,
         @ApiParam(value = "a (url-encoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ") @RequestParam(value = "semgroups", required = false) String semgroups,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
    	
    	if (pageNumber == null || pageNumber < 0) { pageNumber = 0; }
		if (pageSize == null || pageSize < 1) { pageSize = 10; }

		keywords = UrlDecoder.decode(keywords);
		semgroups = UrlDecoder.decode(semgroups);

		String[] filter = keywords != null ? keywords.split(" ") : null;
		String[] semanticGroups = semgroups != null ? semgroups.split(" ") : null;

		List<Neo4jConcept> concepts = conceptRepository.apiGetConcepts(filter, semanticGroups, pageNumber, pageSize);
		List<InlineResponse2001> responses = new ArrayList<InlineResponse2001>();
		
		for (Concept concept : concepts) {
			InlineResponse2001 response = new InlineResponse2001();
			
			response.setId(concept.getId());
			response.setName(concept.getName());
			response.setSemanticGroup(concept.getSemanticGroup().name());
			response.setDefinition(concept.getDescription());
			response.setSynonyms(Arrays.asList(concept.getSynonyms().split("\\|")));

			responses.add(response);
		}

		return ResponseEntity.ok(responses);
    }

}
