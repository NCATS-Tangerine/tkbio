package bio.knowledge.server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import bio.knowledge.database.repository.StatementRepository;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;
import bio.knowledge.server.model.InlineResponse2002;
import bio.knowledge.server.model.StatementsObject;
import bio.knowledge.server.model.StatementsPredicate;
import bio.knowledge.server.model.StatementsSubject;
import bio.knowledge.server.utilities.UrlDecoder;
import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-05T22:03:23.165-07:00")

@Controller
public class StatementsApiController implements StatementsApi {

	@Autowired
	StatementRepository statementRepository;
	
    public ResponseEntity<List<InlineResponse2002>> getStatements( @NotNull @ApiParam(value = "set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements ", required = true) @RequestParam(value = "c", required = true) List<String> c,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "a (url-encoded, space-delimited) string of keywords or substrings against which to match the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts ") @RequestParam(value = "keywords", required = false) String keywords,
         @ApiParam(value = "a (url-encoded, space-delimited) string of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ") @RequestParam(value = "semgroups", required = false) String semgroups) {
    	
    	if (pageNumber == null || pageNumber < 0) { pageNumber = 0; }
		if (pageSize == null || pageSize < 1) { pageSize = 10; }

		c = UrlDecoder.decode(c);
		keywords = UrlDecoder.decode(keywords);
		semgroups = UrlDecoder.decode(semgroups);

		String[] curies = c.toArray(new String[c.size()]);
		String[] filter = keywords != null ? keywords.split(" ") : null;
		String[] semanticGroups = semgroups != null ? semgroups.split(" ") : null;

		List<InlineResponse2002> responses = new ArrayList<InlineResponse2002>();

		List<Map<String, Object>> data = 
				statementRepository.apiFindById(curies, filter, semanticGroups, pageNumber, pageSize);

		for (Map<String, Object> entry : data) {
			InlineResponse2002 response = new InlineResponse2002();

			StatementsObject statementsObject = new StatementsObject();
			StatementsSubject statementsSubject = new StatementsSubject();
			StatementsPredicate statementsPredicate = new StatementsPredicate();

			Statement statement = (Statement) entry.get("statement");
			Concept object = (Concept) entry.get("object");
			Concept subject = (Concept) entry.get("subject");
			Predicate relation = (Predicate) entry.get("relation");

			if (statement != null) {
				// RMB: May 5, 2017 - Statement ID hack here to fix ID
				// truncation problem
				String statementId = statement.getId().replaceAll("\\.", "_");

				response.setId(statementId);
			}

			if (object != null) {
				statementsObject.setId(object.getId());
				statementsObject.setName(object.getName());
			}

			if (subject != null) {
				statementsSubject.setId(subject.getId());
				statementsSubject.setName(subject.getName());
			}

			if (relation != null) {
				statementsPredicate.setId(relation.getId());
				statementsPredicate.setName(relation.getName());
			}

			response.setObject(statementsObject);
			response.setSubject(statementsSubject);
			response.setPredicate(statementsPredicate);

			responses.add(response);
		}

		return ResponseEntity.ok(responses);
	}

}
