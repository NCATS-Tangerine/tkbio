package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2005;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T13:50:33.049-07:00")

@Api(value = "evidence", description = "the evidence API")
public interface EvidenceApi {

    @ApiOperation(value = "", notes = "Retrieves a (paged) list of references cited as evidence for a specified statement ", response = InlineResponse2005.class, responseContainer = "List", tags={ "evidence", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful call returns a list of cited references ", response = InlineResponse2005.class) })
    @RequestMapping(value = "/evidence/{evidenceId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2005>> getEvidence(@ApiParam(value = "identifier of evidence subset of cited references",required=true ) @PathVariable("evidenceId") Integer evidenceId,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against set of concepts matched by the main search string ") @RequestParam(value = "textFilter", required = false) String textFilter);

}
