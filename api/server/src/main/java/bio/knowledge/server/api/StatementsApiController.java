package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2003;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

@Controller
public class StatementsApiController implements StatementsApi {



    public ResponseEntity<InlineResponse2003> getStatements( @NotNull @ApiParam(value = "a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements ", required = true) @RequestParam(value = "emci", required = true) String emci,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "a (urlencoded) space delimited set of keywords or substrings against which to apply against the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts ") @RequestParam(value = "keywords", required = false) String keywords,
         @ApiParam(value = "a (urlencoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ") @RequestParam(value = "semgroups", required = false) String semgroups) {
        // do some magic!
        return new ResponseEntity<InlineResponse2003>(HttpStatus.OK);
    }

}
