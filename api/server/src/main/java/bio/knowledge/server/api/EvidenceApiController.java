package bio.knowledge.server.api;

import bio.knowledge.database.repository.EvidenceRepository;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.server.model.InlineResponse2004;
import bio.knowledge.server.model.InlineResponse2004DataPage;
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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T16:09:22.822-07:00")

@Controller
public class EvidenceApiController implements EvidenceApi {

	@Autowired
	private EvidenceRepository evidenceRepository;

    public ResponseEntity<InlineResponse2004> getEvidence(@ApiParam(value = "identifier of evidence subset, of cited references supporting a given concept-relationship statement ",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement ") @RequestParam(value = "q", required = false) List<String> q,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of cited references per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        
    	//TODO: Find a better way to do this. The second period is being filtered out in the query somehow.
    	evidenceId = evidenceId.replace("%2E", ".");
    	
    	if (pageNumber == null || pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize == null || pageSize < 1) {
			pageSize = 10;
		}
		
		String[] filter;
		
		List<Neo4jEvidence> evidences;
		
		if (q != null ) {
			filter = q.toArray(new String[q.size()]);
			evidences = evidenceRepository.apiGetEvidenceFiltered(evidenceId, filter, pageNumber, pageSize);
		} else {
			evidences = evidenceRepository.apiGetEvidence(evidenceId, pageNumber, pageSize);
		}
		
    	InlineResponse2004 response = new InlineResponse2004();
    	
    	response.setKeywords(q);
    	response.setPageNumber(pageNumber);
    	response.setPageSize(pageSize);
    	response.setTotalEntries(evidences.size());
    	
    	List<InlineResponse2004DataPage> dataPages = new ArrayList<InlineResponse2004DataPage>();
    	
    	for (Evidence evidence : evidences) {
    		InlineResponse2004DataPage dataPage = new InlineResponse2004DataPage();
    		
    		dataPage.setName(evidence.getName());
    		dataPage.setId(evidence.getAccessionId());    		
    		dataPages.add(dataPage);
    	}
    	
    	response.setDataPage(dataPages);
    	
        return ResponseEntity.ok(response);
    }

}
