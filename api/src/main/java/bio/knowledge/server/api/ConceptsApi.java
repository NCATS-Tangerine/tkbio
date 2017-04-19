package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

@Api(value = "concepts", description = "the concepts API")
public interface ConceptsApi {

    @ApiOperation(value = "", notes = "Retrieves details for a specified concepts in the system ", response = InlineResponse200.class, responseContainer = "List", tags={ "concepts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response with concept details returned ", response = InlineResponse200.class) })
    @RequestMapping(value = "/concepts/{conceptId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "local identifier of concept of interest",required=true ) @PathVariable("conceptId") Integer conceptId);


    @ApiOperation(value = "", notes = "Retrieves a (paged) list of concepts in the system ", response = InlineResponse2001.class, responseContainer = "List", tags={ "concepts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response with concept list returned ", response = InlineResponse2001.class) })
    @RequestMapping(value = "/concepts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2001>> getConcepts( @NotNull @ApiParam(value = "string of comma delimited keywords to match against concept names and aliases", required = true) @RequestParam(value = "textFilter", required = true) String textFilter,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "string of comma-delimited semantic type names to apply against set of concepts matched by the main search string ") @RequestParam(value = "semanticType", required = false) String semanticType);

}
