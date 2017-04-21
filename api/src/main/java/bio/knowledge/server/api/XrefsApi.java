package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2002;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-20T17:24:29.683-07:00")

@Api(value = "xrefs", description = "the xrefs API")
public interface XrefsApi {

    @ApiOperation(value = "", notes = "Retrieves a list of cross-referencing identifiers associated with an specifically identified concept, typically, a concept selected from the list of concepts originally returned by a /concepts API call. This API call will typically be run against the same KS (only) from which the originally selected concept was retrieved. ", response = String.class, responseContainer = "List", tags={ "xrefs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of cross-reference identifiers associated with the specified conceptId (maybe an empty list?) ", response = String.class) })
    @RequestMapping(value = "/xrefs/{conceptId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getConceptXRefs(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId);


    @ApiOperation(value = "", notes = "Given an input list of cross-reference identifiers, retrieves the list of identifiers of concepts that match one or more cross-references posted in the previously initiated query session. This new list of concept identifierss is returned with the full list of their associated cross-reference identifiers. ", response = InlineResponse2002.class, responseContainer = "List", tags={ "xrefs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of identifiers of concepts matching at least one identifier in the input list of cross-reference identifiers. Each concept identifier is returned with  the full list of associated cross-reference identifiers known to the given Knowledge Source. ", response = InlineResponse2002.class) })
    @RequestMapping(value = "/xrefs",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2002>> getXRefConcepts( @NotNull @ApiParam(value = "list of cross-reference identifiers (xi) to be used in a search for equivalent concepts ", required = true) @RequestParam(value = "xi", required = true) List<String> xi);

}
