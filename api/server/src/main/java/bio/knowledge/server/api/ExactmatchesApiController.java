package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2002;

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
public class ExactMatchesApiController implements ExactMatchesApi {



    public ResponseEntity<List<InlineResponse2002>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts ", required = true) @RequestParam(value = "emci", required = true) String emci) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2002>>(HttpStatus.OK);
    }

}
