package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2004;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T16:09:22.822-07:00")

@Api(value = "evidence", description = "the evidence API")
public interface EvidenceApi {

    @ApiOperation(value = "", notes = "Retrieves a (paged) list of references cited as evidence for a specified statement ", response = InlineResponse2004.class, tags={ "evidence", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful call returns a list of cited references ", response = InlineResponse2004.class) })
    @RequestMapping(value = "/evidence/{evidenceId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<InlineResponse2004> getEvidence(@ApiParam(value = "identifier of evidence subset, of cited references supporting a given concept-relationship statement ",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement ") @RequestParam(value = "q", required = false) List<String> q,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of cited references per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize);

}
