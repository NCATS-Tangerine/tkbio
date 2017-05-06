package bio.knowledge.server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.database.repository.EvidenceRepository;
import bio.knowledge.model.Annotation;
import bio.knowledge.server.model.InlineResponse2003;
import bio.knowledge.server.utilities.UrlDecoder;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-05T22:03:23.165-07:00")

@Controller
public class EvidenceApiController implements EvidenceApi {

	@Autowired
	EvidenceRepository evidenceRepository;

    public ResponseEntity<List<InlineResponse2003>> getEvidence(@ApiParam(value = "(url-encoded) CURIE identifier of the concept-relationship statement (\"assertion\", \"claim\") for which associated evidence is sought ",required=true ) @PathVariable("statementId") String statementId,
         @ApiParam(value = "(url-encoded, space delimited) keyword filter to apply against the label field of the annotation ") @RequestParam(value = "keywords", required = false) String keywords,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of cited references per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
    	
    	if (pageNumber == null || pageNumber < 0) { pageNumber = 0; }
		if (pageSize == null || pageSize < 1) { pageSize = 10; }
		
		statementId = UrlDecoder.decode(statementId);
		
		// RMB: May 5, 2017 - Statement ID hack here to fix ID truncation problem
		statementId = statementId.replaceAll("_",".");
				
		keywords = UrlDecoder.decode(keywords);
		
		String[] filter = keywords != null ? keywords.split(" ") : null;
		
		List<Map<String, Object>> data = evidenceRepository.apiGetEvidence(statementId, filter, pageNumber, pageSize);
		
		List<InlineResponse2003> responses = new ArrayList<InlineResponse2003>();
		
		for (Map<String, Object> entry : data) {
			String year = String.valueOf((Integer) entry.get("year"));
			String month = String.valueOf((Integer) entry.get("month"));
			String day = String.valueOf((Integer) entry.get("day"));
			Annotation annotation = (Annotation) entry.get("annotation");
			
			InlineResponse2003 response = new InlineResponse2003();
			response.setId(annotation.getId());
			response.setLabel(annotation.getName());
			response.setDate(year + "-" + month + "-" + day);
			
			responses.add(response);
		}
		
		return ResponseEntity.ok(responses);
    }

}
