package bio.knowledge.server.api;

import bio.knowledge.database.repository.StatementRepository;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Statement;
import bio.knowledge.server.model.InlineResponse2003;
import bio.knowledge.server.model.StatementsDataPage;
import bio.knowledge.server.model.StatementsEvidence;
import bio.knowledge.server.model.StatementsObject;
import bio.knowledge.server.model.StatementsPredicate;
import bio.knowledge.server.model.StatementsSubject;
import bio.knowledge.server.utilities.UrlDecoder;
import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T09:47:16.640-07:00")

@Controller
public class StatementsApiController implements StatementsApi {

	@Autowired
	StatementRepository statementRepository;

	public ResponseEntity<List<InlineResponse2003>> getStatements(
			@NotNull @ApiParam(value = "a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts to be used in a search for associated concept-relation statements ", required = true) @RequestParam(value = "emci", required = true) String emci,
			@ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ApiParam(value = "a (urlencoded) space delimited set of keywords or substrings against which to apply against the subject, predicate or object names of the set of concept-relations matched by any of the input exact matching concepts ") @RequestParam(value = "keywords", required = false) String keywords,
			@ApiParam(value = "a (urlencoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain the subject or object concepts associated with the query seed concept (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ") @RequestParam(value = "semgroups", required = false) String semgroups) {

		InlineResponse2003 response = new InlineResponse2003();

		emci = UrlDecoder.decode(emci);
		keywords = UrlDecoder.decode(keywords);
		semgroups = UrlDecoder.decode(semgroups);

		String[] curies = emci.split(" ");
		String[] filter = keywords != null ? keywords.split(" ") : null;
		String[] semanticGroups = semgroups != null ? semgroups.split(" ") : null;

		if (pageNumber == null || pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize == null || pageSize < 1) {
			pageSize = 10;
		}

		response.setKeywords(asList(filter));
		response.setSemanticGroups(asList(semanticGroups));
		response.setPageNumber(pageNumber);
		response.setPageSize(pageSize);

		List<Map<String, Object>> data = statementRepository.apiFindById(curies, filter, semanticGroups, pageNumber,
				pageSize);
		response.setTotalEntries(data.size());

		for (Map<String, Object> entry : data) {
			StatementsDataPage dataPage = new StatementsDataPage();
			StatementsObject statementsObject = new StatementsObject();
			StatementsSubject statementsSubject = new StatementsSubject();
			StatementsPredicate statementsPredicate = new StatementsPredicate();
			StatementsEvidence statementsEvidence = new StatementsEvidence();

			Statement statement = (Statement) entry.get("statement");
			Concept object = (Concept) entry.get("object");
			Concept subject = (Concept) entry.get("subject");
			Predicate relation = (Predicate) entry.get("relation");
			Evidence evidence = (Evidence) entry.get("evidence");

			if (statement != null) {
				dataPage.setId(statement.getAccessionId());
			}

			if (object != null) {
				statementsObject.setId(object.getAccessionId());
				statementsObject.setName(object.getName());
			}

			if (subject != null) {
				statementsSubject.setId(subject.getAccessionId());
				statementsSubject.setName(subject.getName());
			}

			if (relation != null) {
				statementsPredicate.setId(relation.getAccessionId());
				statementsPredicate.setName(relation.getName());
			}

			if (evidence != null) {
				statementsEvidence.setId(evidence.getAccessionId());
				statementsEvidence.setCount(evidence.getCount());
			}

			dataPage.setObject(statementsObject);
			dataPage.setSubject(statementsSubject);
			dataPage.setPredicate(statementsPredicate);
			dataPage.setEvidence(statementsEvidence);

			response.addDataPageItem(dataPage);
		}

		// TODO: Return either a list of statements, or a responses that
		// encapsulates a list of statements. Not a list of responses that
		// encapsulate a list of statements!

		List<InlineResponse2003> adHocList = new ArrayList<InlineResponse2003>();
		adHocList.add(response);

		return ResponseEntity.ok(adHocList);
	}

	private <T> List<T> asList(T[] array) {
		if (array != null) {
			return Arrays.asList(array);
		} else {
			return null;
		}
	}

}
