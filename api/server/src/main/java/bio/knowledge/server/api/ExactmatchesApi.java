package bio.knowledge.server.api;


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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

@Api(value = "exactmatches", description = "the exactmatches API")
public interface ExactmatchesApi {

    @ApiOperation(value = "", notes = "Retrieves a list of qualified identifiers of \"exact match\" concepts, [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch) associated with an specified local concept object identifier, typically, of a concept selected from the list of concepts originally returned by a /concepts API call on a given KS. This API call should generally only be run against the same KS (only) from which the originally selected concept was retrieved. ", response = String.class, responseContainer = "List", tags={ "exactmatches", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of [CURIE](https://www.w3.org/TR/curie/) identifiers to 3rd party concepts defined outside of the local KS, which are deemed semantic exact matches to  the specified identified concept (maybe an empty list?) ", response = String.class) })
    @RequestMapping(value = "/exactmatches/{conceptId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId);

}
