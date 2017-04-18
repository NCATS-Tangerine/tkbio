package io.swagger.api;

import io.swagger.model.Identifiers;
import io.swagger.model.InlineResponse2003;
import io.swagger.model.InlineResponse201;
import java.util.List;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-17T22:13:20.980-07:00")

@Api(value = "statements", description = "the statements API")
public interface StatementsApi {

    @ApiOperation(value = "", notes = "Retrieves a paged list of concept-relations with either the subject or object concept matching the list of concepts previously POST'ed into the specified query session ", response = InlineResponse2003.class, responseContainer = "List", tags={ "statements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of concept-relations with one or more matches to query session concept identifiers ", response = InlineResponse2003.class) })
    @RequestMapping(value = "/statements",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2003>> getStatements( @NotNull @ApiParam(value = "identifier of the query session established by prior POST'ing of a list of concept identifiers of interest ", required = true) @RequestParam(value = "queryId", required = true) String queryId,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search ") @RequestParam(value = "textFilter", required = false) String textFilter,
         @ApiParam(value = "semanticType filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search main search string ") @RequestParam(value = "semanticType", required = false) String semanticType);


    @ApiOperation(value = "", notes = "Posts a list of concept identifiers to a given KS endpoint, initiating a query session for concept-relation statement retrieval ", response = InlineResponse201.class, tags={ "statements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response returns the identifier of a newly initiated query session ", response = InlineResponse201.class) })
    @RequestMapping(value = "/statements",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<InlineResponse201> postStatementQuery(@ApiParam(value = "list of concept identifiers to be used in a search for associated concept-relation statements " ,required=true ) @RequestBody List<Identifiers> identifiers);

}
