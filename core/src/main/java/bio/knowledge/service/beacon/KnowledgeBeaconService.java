package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2003;
import bio.knowledge.client.model.InlineResponse2004;
import bio.knowledge.client.model.StatementsObject;
import bio.knowledge.client.model.StatementsPredicate;
import bio.knowledge.client.model.StatementsSubject;
import bio.knowledge.model.Concept;
import bio.knowledge.model.ConceptImpl;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.EvidenceImpl;
import bio.knowledge.model.GeneralStatement;
import bio.knowledge.model.PredicateImpl;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Statement;

/**
 * 
 * @author Lance Hannestad
 * 
 *         It may seem wasteful to instantiate a new {@code ConceptApi} (or
 *         other API classes) within each {@code ListSupplier<T>}, but in fact
 *         it is necessary because we're asynchrounously setting their ApiClient
 *         objects (which encapsulate the URI to be queried) in
 *         {@code GenericDataService}.
 *
 */
@Service
public class KnowledgeBeaconService extends GenericKnowledgeService {

	/**
	 * Gets a list of concepts satisfying a query with the given parameters.
	 * @param keywords
	 * @param semanticGroups
	 * @param pageNumber
	 * @param pageSize
	 * @return a {@code CompletableFuture} of all the concepts from all the
	 *         knowledge sources in the {@code KnowledgeBeaconRegistry} that
	 *         satisfy a query with the given parameters.
	 */
	public CompletableFuture<List<Concept>> getConcepts(String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return query(buildConceptSupplier(
				keywords,
				semanticGroups,
				pageNumber,
				pageSize
		));
	}
	
	public CompletableFuture<List<Concept>> getConceptDetails(String conceptId) {
		return query(buildConceptDetailsSupplier(conceptId));
	}
	
	public CompletableFuture<List<Statement>> getStatements(
			String emci,
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return query(buildStatementsSupplier(emci, keywords, semanticGroups, pageNumber, pageSize));
	}
	
	public CompletableFuture<List<Evidence>> getEvidences(
			String statementId,
			String keywords,
			int pageNumber,
			int pageSize
	) {
		return query(buildEvidenceSupplier(statementId, keywords, pageNumber, pageSize));
	}
	
	private ListSupplier<Concept> buildConceptSupplier(
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
		) {
		return new ListSupplier<Concept>() {

			@Override
			public List<Concept> get() {
				ConceptsApi conceptsApi = new ConceptsApi(getApiClient());
				
				try {
					List<InlineResponse2001> responses =
							conceptsApi.getConcepts(keywords, semanticGroups, pageNumber, pageSize);
					List<Concept> concepts = new ArrayList<Concept>();
					for (InlineResponse2001 response : responses) {
						SemanticGroup semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						ConceptImpl concept = new ConceptImpl(
								response.getId(),
								semgroup,
								response.getName()
						);
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						
						concepts.add(concept);
					}
					return concepts;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
	
	private ListSupplier<Concept> buildConceptDetailsSupplier(String conceptId) {
		return new ListSupplier<Concept>() {

			@Override
			public List<Concept> get() {
				ConceptsApi conceptsApi = new ConceptsApi(getApiClient());
				
				try {
					List<InlineResponse200> responses = conceptsApi.getConceptDetails(conceptId);
					List<Concept> concepts = new ArrayList<Concept>();
					
					for (InlineResponse200 response : responses) {
						SemanticGroup semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						ConceptImpl concept = new ConceptImpl(
								response.getId(),
								semgroup,
								response.getName()
						);
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						
						concepts.add(concept);
					}
					
					return concepts;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
	
	private ListSupplier<Statement> buildStatementsSupplier(
			String emci, 
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return new ListSupplier<Statement>() {

			@Override
			public List<Statement> get() {
				StatementsApi statementsApi = new StatementsApi(getApiClient());
				
				try {
					List<InlineResponse2003> responses =
							statementsApi.getStatements(emci, pageNumber, pageSize, keywords, semanticGroups);
					List<Statement> statements = new ArrayList<Statement>();
					
					for (InlineResponse2003 response : responses) {
						String id = response.getId();
						StatementsObject statementsObject = response.getObject();
						StatementsSubject statementsSubject = response.getSubject();
						StatementsPredicate statementsPredicate = response.getPredicate();
						
						ConceptImpl subject = new ConceptImpl(
								statementsSubject.getId(),
								null,
								statementsSubject.getName()
						);
						
						ConceptImpl object = new ConceptImpl(
								statementsObject.getId(),
								null,
								statementsObject.getName()
						);
						
						PredicateImpl predicate = new PredicateImpl(
								statementsPredicate.getName()
						);
						
						statements.add(new GeneralStatement(id, subject, predicate, object));
					}
					
					return statements;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
	
	public ListSupplier<Evidence> buildEvidenceSupplier(
			String statementId,
			String keywords,
			int pageNumber,
			int pageSize
	) {
		return new ListSupplier<Evidence>() {

			@Override
			public List<Evidence> get() {
				EvidenceApi evidenceApi = new EvidenceApi(getApiClient());
				
				try {
					List<InlineResponse2004> responses =
							evidenceApi.getEvidence(statementId, keywords, pageNumber, pageSize);
					
					List<Evidence> evidences = new ArrayList<Evidence>();
					
					for (InlineResponse2004 response : responses) {
						EvidenceImpl evidence = new EvidenceImpl();
						evidence.setId(response.getId());
						evidence.setName(response.getLabel());
						evidence.setPublicationDate(response.getDate());
						
						evidences.add(evidence);
					}
					
					return evidences;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
}
