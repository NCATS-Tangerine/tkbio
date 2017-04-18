package io.swagger.api;

import io.swagger.model.InlineResponse200;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-17T22:13:20.980-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {



    public ResponseEntity<List<InlineResponse200>> getConcepts( @NotNull @ApiParam(value = "search string to match against concept names", required = true) @RequestParam(value = "search", required = true) String search,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "text filter to apply against set of concepts matched by the main search string ") @RequestParam(value = "textFilter", required = false) String textFilter,
         @ApiParam(value = "semanticType filter to apply against set of concepts matched by the main search string ") @RequestParam(value = "semanticType", required = false) String semanticType) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

}
