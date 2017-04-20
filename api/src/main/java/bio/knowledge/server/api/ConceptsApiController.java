package bio.knowledge.server.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.model.Concept;
import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {
	
	@Autowired
	private ConceptRepository conceptRepository ;

    public ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "local identifier of concept of interest",required=true ) @PathVariable("conceptId") Integer conceptId) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse2001>> getConcepts( @NotNull @ApiParam(value = "string of comma delimited keywords to match against concept names and aliases", required = true) @RequestParam(value = "textFilter", required = true) String textFilter,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "string of comma-delimited semantic type names to apply against set of concepts matched by the main search string ") @RequestParam(value = "semanticType", required = false) String semanticType) {
    	
    	System.out.println("MAGIC: " + textFilter);
    	
    	Pageable pageable = new Pageable() {

			@Override
			public int getPageNumber() {
				return pageNumber;
			}

			@Override
			public int getPageSize() {
				return pageSize;
			}

			@Override
			public int getOffset() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Sort getSort() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Pageable next() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Pageable previousOrFirst() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Pageable first() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasPrevious() {
				// TODO Auto-generated method stub
				return false;
			}
    		
    	};
    	
    	List<Concept> concepts = conceptRepository.findByNameLikeIgnoreCase(textFilter, pageable);
    	List<InlineResponse2001> responses = new ArrayList<InlineResponse2001>();
    	
    	for (Concept concept : concepts) {
    		InlineResponse2001 response = new InlineResponse2001();
    		response.setId(String.valueOf(concept.getId()));
    		response.setName(concept.getName());
    		response.setSemanticType(concept.getSemanticGroup().name());
    		response.setSynonyms(Arrays.asList(concept.getSynonyms().split(" ")));
    		
    		responses.add(response);
    	}
    	
    	InlineResponse2001 response = new InlineResponse2001();
    	response.setId("123");
    	response.setName("abc");
    	response.setSemanticType("some kind of thing");
    	response.setSource("source");
    	response.setSynonyms(null);
    	responses.add(response);
    	
    	return ResponseEntity.ok(responses);
    }

}
