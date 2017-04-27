package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2004;

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
public class EvidenceApiController implements EvidenceApi {



    public ResponseEntity<InlineResponse2004> getEvidence(@ApiParam(value = "local identifier of evidence subset, of cited references supporting a given concept-relationship statement ",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "keyword filter to apply against the citation titles of references related to the evidence supporting given concept-relationship statement ") @RequestParam(value = "keywords", required = false) String keywords,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of cited references per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        // do some magic!
        return new ResponseEntity<InlineResponse2004>(HttpStatus.OK);
    }

}
