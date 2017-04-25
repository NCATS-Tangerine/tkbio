package bio.knowledge.server.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.server.model.InlineResponse2003;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T13:55:25.453-07:00")

@Controller
public class StatementsApiController implements StatementsApi {



    public ResponseEntity<InlineResponse2003> getStatements( @NotNull @ApiParam(value = "list of concept identifiers (ci) to be used in a search for associated concept-relation statements ", required = true) @RequestParam(value = "ci", required = true) List<String> ci,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "keyword filter to apply against the subject, predicate or object names of the set of concept-relations matched by the query seed concept ") @RequestParam(value = "q", required = false) List<String> q,
         @ApiParam(value = "array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes) ") @RequestParam(value = "sg", required = false) List<String> sg) {
        // do some magic!
        return new ResponseEntity<InlineResponse2003>(HttpStatus.OK);
    }

}
