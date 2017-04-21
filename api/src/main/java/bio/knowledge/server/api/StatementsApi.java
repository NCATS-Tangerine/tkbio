package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2003;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T09:24:51.811-07:00")

@Api(value = "statements", description = "the statements API")
public interface StatementsApi {

    @ApiOperation(value = "", notes = "Given a list of concept identifiers to a given KS endpoint,  retrieves a paged list of concept-relations with either the subject or object concept matching a concept in the input list ", response = InlineResponse2003.class, tags={ "statements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response returns a list of concept-relations with one or more matches to query session concept identifiers ", response = InlineResponse2003.class) })
    @RequestMapping(value = "/statements",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<InlineResponse2003> getStatements( @NotNull @ApiParam(value = "list of concept identifiers (ci) to be used in a search for associated concept-relation statements ", required = true) @RequestParam(value = "ci", required = true) List<String> ci,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "keyword filter to apply against the subject, predicate or object names of the set of concept-relations matched by the query seed concept ") @RequestParam(value = "q", required = false) List<String> q,
         @ApiParam(value = "array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes) ") @RequestParam(value = "sg", required = false) List<String> sg);

}
