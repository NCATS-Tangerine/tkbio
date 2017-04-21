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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-20T17:24:29.683-07:00")

@Controller
public class EvidenceApiController implements EvidenceApi {



    public ResponseEntity<List<InlineResponse2004>> getEvidence(@ApiParam(value = "identifier of evidence subset of cited references",required=true ) @PathVariable("evidenceId") String evidenceId,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against set of concepts matched by the main search string ") @RequestParam(value = "textFilter", required = false) String textFilter) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2004>>(HttpStatus.OK);
    }

}
