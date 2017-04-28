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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

@Api(value = "exactMatches", description = "the exactMatches API")
public interface ExactMatchesApi {

    @ApiOperation(value = "", notes = "Given an input list of [CURIE](https://www.w3.org/TR/curie/) identifiers of known exactly matched concepts [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch), retrieves the list of (CURIE)[https://www.w3.org/TR/curie/] identifiers of additional concepts that are deemed to be exact matches to one or more of the input concepts. This new list of concept identifiers is returned with the full list of any additional identifiers deemed by the KS to also be identifying exactly matched concepts. ", response = InlineResponse2002.class, responseContainer = "List", tags={ "exactmatches", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of [CURIE-encoded](https://www.w3.org/TR/curie/)  identifiers of concepts (with supporting evidence code and reference) matching at least one identifier in the input list of known exactly matched concepts [sensa SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). Each concept identifier is returned with  the full list of any additional associated [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers known to the local Knowledge Source. ", response = InlineResponse2002.class) })
    @RequestMapping(value = "/exactMatches",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2002>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts ", required = true) @RequestParam(value = "emci", required = true) String emci);

}
