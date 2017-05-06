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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-05T22:03:23.165-07:00")

@Api(value = "exactmatches", description = "the exactmatches API")
public interface ExactmatchesApi {

    @ApiOperation(value = "", notes = "Retrieves a list of qualified identifiers of \"exact match\" concepts, [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch) associated with a specified (url-encoded) CURIE (without brackets) concept object identifier,  typically, of a concept selected from the list of concepts originally returned by a /concepts API call on a given KS.  ", response = String.class, responseContainer = "List", tags={ "exactmatches", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a set of [CURIE](https://www.w3.org/TR/curie/) identifiers to 3rd party concepts defined outside of the local KS, which are deemed semantic exact matches [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch) to  the specified identified concept (maybe an empty set?) ", response = String.class) })
    @RequestMapping(value = "/exactmatches/{conceptId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "(url-encoded) CURIE identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId);


    @ApiOperation(value = "", notes = "Given an input list of [CURIE](https://www.w3.org/TR/curie/) identifiers of known exactly matched concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch), retrieves the list of [CURIE](https://www.w3.org/TR/curie/) identifiers of **additional** concepts that are deemed by the given knowledge source to be exact matches to one or more of the input concepts.  If an empty set is returned, the it can be assumed that the given knowledge source does not know of any new equivalent concepts to add to the input set. ", response = String.class, responseContainer = "List", tags={ "exactmatches", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of concepts (with supporting evidence code and reference) matching at least one identifier in the input list of known exactly matched concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). Each concept identifier is returned with  the full list of any additional associated [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exact match concepts known to the given Knowledge Source. ", response = String.class) })
    @RequestMapping(value = "/exactmatches",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). ", required = true) @RequestParam(value = "c", required = true) List<String> c);

}
