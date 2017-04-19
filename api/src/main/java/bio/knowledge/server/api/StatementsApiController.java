package bio.knowledge.server.api;

import bio.knowledge.server.model.Identifiers;
import bio.knowledge.server.model.InlineResponse2004;
import bio.knowledge.server.model.InlineResponse201;
import java.util.List;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T13:50:33.049-07:00")

@Controller
public class StatementsApiController implements StatementsApi {



    public ResponseEntity<List<InlineResponse2004>> getStatements( @NotNull @ApiParam(value = "identifier of the query session established by prior POST'ing of a list of concept identifiers of interest ", required = true) @RequestParam(value = "queryId", required = true) String queryId,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search ") @RequestParam(value = "textFilter", required = false) String textFilter,
         @ApiParam(value = "semanticType filter to apply against the subject, predicate or object names of the set of concept-relations matched by the main concept search main search string ") @RequestParam(value = "semanticType", required = false) String semanticType) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2004>>(HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse201> postStatementQuery(@ApiParam(value = "list of concept identifiers to be used in a search for associated concept-relation statements " ,required=true ) @RequestBody List<Identifiers> identifiers) {
        // do some magic!
        return new ResponseEntity<InlineResponse201>(HttpStatus.OK);
    }

}
