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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T16:41:12.704-07:00")

@Api(value = "exactMatches", description = "the exactMatches API")
public interface ExactmatchesApi {


//    @ApiOperation(value = "", notes = "Retrieves a list of qualified identifiers of \"exact match\" concepts, [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch) associated with a specified (url-encoded) CURIE (without brackets) concept object identifier,  typically, of a concept selected from the list of concepts originally returned by a /concepts API call on a given KS. This API call should generally only be run against the same KS (only) from which the originally selected concept was retrieved. ", response = String.class, responseContainer = "List", tags={ "exactmatches", })
//
//    @ApiResponses(value = { 
//        @ApiResponse(code = 200, message = "Successful response returns a list of [CURIE-encoded](https://www.w3.org/TR/curie/)  identifiers of concepts (with supporting evidence code and reference) matching at least one identifier in the input list of known exactly matched concepts [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). Each concept identifier is returned with  the full list of any additional associated [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers known to the local Knowledge Source. ", response = InlineResponse2002.class) })
//    @RequestMapping(value = "/exactMatches",
//        produces = { "application/json" }, 
//        method = RequestMethod.GET)
//    ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "(url-encoded) CURIE identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId);


    @ApiOperation(value = "", notes = "Given an input list of [CURIE](https://www.w3.org/TR/curie/) identifiers of known exactly matched concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch), retrieves the list of [CURIE](https://www.w3.org/TR/curie/) identifiers of **additional** concepts that are deemed by the given knowledge source to be exact matches to one or more of the input concepts.  If an empty set is returned, the it can be assumed that the given knowledge source does not know of any new equivalent concepts to add to the input set. ", response = InlineResponse2002.class, responseContainer = "List", tags={ "exactmatches", })

    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of [CURIE-encoded](https://www.w3.org/TR/curie/)  identifiers of concepts (with supporting evidence code and reference) matching at least one identifier in the input list of known exactly matched concepts [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). Each concept identifier is returned with  the full list of any additional associated [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers known to the local Knowledge Source. ", response = InlineResponse2002.class) })
    @RequestMapping(value = "/exactMatches",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2002>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "a (url-encoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts ", required = true) @RequestParam(value = "emci", required = true) String emci);

}
