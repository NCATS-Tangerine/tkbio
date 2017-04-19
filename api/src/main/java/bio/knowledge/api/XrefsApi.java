package bio.knowledge.api;

import bio.knowledge.model.Identifiers;
import bio.knowledge.model.InlineResponse2001;
import bio.knowledge.model.InlineResponse2002;
import bio.knowledge.model.InlineResponse201;
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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-18T15:45:38.310-07:00")

@Api(value = "xrefs", description = "the xrefs API")
public interface XrefsApi {

    @ApiOperation(value = "", notes = "Retrieves a list of cross-referencing identifiers associated with an specifically identified concept, typically, a concept selected from the list of concepts originally returned by a /concepts API call. This API call will typically be run against the same KS (only) from which the originally selected concept was retrieved. ", response = InlineResponse2001.class, responseContainer = "List", tags={ "xrefs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of cross-reference identifiers associated with the specified conceptId (maybe an empty list?) ", response = InlineResponse2001.class) })
    @RequestMapping(value = "/xrefs/{conceptId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2001>> getConceptXRefs(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId);


    @ApiOperation(value = "", notes = "Retrieves the list of concepts with that match one or more cross-references posted in the previously initiated query session. This new list of concepts is returned with the full list of their associated cross-references. ", response = InlineResponse2002.class, responseContainer = "List", tags={ "xrefs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of concepts with one or more matches to query session cross-reference identifiers ", response = InlineResponse2002.class) })
    @RequestMapping(value = "/xrefs",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2002>> getXRefConcepts( @NotNull @ApiParam(value = "identifier of the query session established by prior POST'ing of a list of cross-references identifiers of interest ", required = true) @RequestParam(value = "queryId", required = true) String queryId);


    @ApiOperation(value = "", notes = "Posts a list of cross-reference identifiers to a given KS endpoint, initiating a query session ", response = InlineResponse201.class, tags={ "xrefs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response returns the identifier of a newly initiated query session ", response = InlineResponse201.class) })
    @RequestMapping(value = "/xrefs",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<InlineResponse201> postXRefQuery(@ApiParam(value = "list of cross-reference identifiers to be used in a search for equivalent concepts " ,required=true ) @RequestBody List<Identifiers> identifiers);

}
