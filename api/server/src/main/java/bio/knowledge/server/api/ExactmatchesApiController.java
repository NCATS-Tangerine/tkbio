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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T16:41:12.704-07:00")

@Controller
public class ExactmatchesApiController implements ExactmatchesApi {

    public ResponseEntity<List<InlineResponse2002>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "a (url-encoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts ", required = true) @RequestParam(value = "emci", required = true) String emci) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2002>>(HttpStatus.OK);
    }

}
