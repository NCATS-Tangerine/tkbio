package bio.knowledge.server.api;

import bio.knowledge.database.repository.EvidenceRepository;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.server.model.EvidenceevidenceIdDataPage;
import bio.knowledge.server.model.InlineResponse2004;
import bio.knowledge.server.utilities.UrlDecoder;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.*;
import javax.ws.rs.core.Response;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T09:12:42.779-07:00")

@Controller
public class EvidenceApiController implements EvidenceApi {

	@Autowired
	EvidenceRepository evidenceRepository;

    public ResponseEntity<List<InlineResponse2004>> getEvidence(@ApiParam(value = "(url-encoded) CURIE identifier of an evidence subset, of cited references supporting a given concept-relationship statement ",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement ") @RequestParam(value = "keywords", required = false) String keywords,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of cited references per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        
    	if (pageNumber == null || pageNumber < 0) { pageNumber = 0; }
		if (pageSize == null || pageSize < 1) { pageSize = 10; }
		
		evidenceId = UrlDecoder.decode(evidenceId);
		keywords = UrlDecoder.decode(keywords);
		
		String[] filter = keywords != null ? keywords.split(" ") : null;
		
		List<Map<String, Object>> data = evidenceRepository.apiGetEvidence(evidenceId, filter, pageNumber, pageSize);
		
		InlineResponse2004 response = new InlineResponse2004();
		
		response.setKeywords(filter != null ? Arrays.asList(filter) : null);
		response.setPageNumber(pageNumber);
		response.setPageSize(pageSize);
		
		for (Map<String, Object> entry : data) {
			String year = String.valueOf((Integer) entry.get("year"));
			String month = String.valueOf((Integer) entry.get("month"));
			String day = String.valueOf((Integer) entry.get("day"));
			Annotation annotation = (Annotation) entry.get("annotation");
			
			EvidenceevidenceIdDataPage dataPage = new EvidenceevidenceIdDataPage();
			dataPage.setId(annotation.getAccessionId());
			dataPage.setName(annotation.getName());
			dataPage.setPublicationDate(year + "-" + month + "-" + day);
			
			response.addDataPageItem(dataPage);
		}

		// TODO: This is awkward, modify API?
		List<InlineResponse2004> adHocResponse = new ArrayList<InlineResponse2004>();
		adHocResponse.add(response);

		return ResponseEntity.ok(adHocResponse);
    }

}
