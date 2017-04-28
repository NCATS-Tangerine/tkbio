package bio.knowledge.server.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.server.model.InlineResponse2002;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-21T13:55:25.453-07:00")

@Controller
public class XrefsApiController implements XrefsApi {



    public ResponseEntity<List<String>> getConceptXRefs(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse2002>> getXRefConcepts( @NotNull @ApiParam(value = "list of cross-reference identifiers to be used in a search for equivalent concepts ", required = true) @RequestParam(value = "xi", required = true) List<String> xi) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2002>>(HttpStatus.OK);
    }

}
