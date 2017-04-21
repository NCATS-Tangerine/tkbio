package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T09:24:51.811-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {



    public ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "local object identifier of concept of interest",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse2001> getConcepts( @NotNull @ApiParam(value = "array of keywords or substrings against which to match concept names and synonyms", required = true) @RequestParam(value = "q", required = true) List<String> q,
         @ApiParam(value = "array of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt for the full list of codes) ") @RequestParam(value = "sg", required = false) List<String> sg,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        // do some magic!
        return new ResponseEntity<InlineResponse2001>(HttpStatus.OK);
    }

}
