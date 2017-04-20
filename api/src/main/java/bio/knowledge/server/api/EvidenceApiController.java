package bio.knowledge.server.api;

import bio.knowledge.database.repository.EvidenceRepository;
import bio.knowledge.model.Evidence;
import bio.knowledge.server.model.InlineResponse2005;

import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

@Controller
public class EvidenceApiController implements EvidenceApi {
	
	@Autowired
	EvidenceRepository evidenceRepository;


    public ResponseEntity<List<InlineResponse2005>> getEvidence(@ApiParam(value = "identifier of evidence subset of cited references",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against set of concepts matched by the main search string ") @RequestParam(value = "textFilter", required = false) String textFilter) {
        
    	if (pageNumber == null || pageNumber < 0) { pageNumber = 0; }
    	if (pageSize == null || pageSize < 1) { pageSize = 10; }
    	
    	List<InlineResponse2005> responses = new ArrayList<InlineResponse2005>();
    	
    	List<Evidence> evidences = evidenceRepository.apiGetEvidence(evidenceId, pageNumber, pageSize);
    	
    	for (Evidence evidence : evidences) {
    		InlineResponse2005 response = new InlineResponse2005();
    		
    		response.setId(String.valueOf(evidence.getId()));
    		response.setName(evidence.getName());
    		
    		responses.add(response);
    	}
    	
    	
        return ResponseEntity.ok(responses);
    }

}
