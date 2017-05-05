package bio.knowledge.server.api;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-04T09:58:27.108-07:00")

@Controller
public class ExactmatchesApiController implements ExactmatchesApi {



    public ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "(url-encoded) CURIE identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts [*sensa*-SKOS](http://www.w3.org/2004/02/skos/core#exactMatch). ", required = true) @RequestParam(value = "c", required = true) List<String> c) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

}

