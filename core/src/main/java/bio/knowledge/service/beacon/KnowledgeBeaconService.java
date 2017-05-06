package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2002;
import bio.knowledge.client.model.InlineResponse2003;
import bio.knowledge.client.model.InlineResponse2004;
import bio.knowledge.client.model.StatementsObject;
import bio.knowledge.client.model.StatementsPredicate;
import bio.knowledge.client.model.StatementsSubject;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.AnnotationImpl;
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
 *         <br><br>
 *         The methods in this class are ugly and confusing.. But it's somewhat
 *         unavoidable. Take a look at how they are used in
 *         {@code GenericKnowledgeService}. A SupplierBuilder builds a
 *         ListSupplier which extends a Supplier, which is used to generate
 *         CompletableFutures.
 *
 */
@Service
public class KnowledgeBeaconService extends GenericKnowledgeService {
	
	/**
	 * Periods sometimes drop out of queries if they are not URL encoded. This
	 * is <b>not</b> a complete URL encoding. I have only encoded those few
	 * characters that might be problematic. We may have to revisit this in
	 * the future, and implement a proper encoder.
	 */
	private String urlEncode(String string) {
		if (string != null) {
			return string.replace(".", "%2E").replace(" ", "%20").replace(":", "%3A");
		} else {
			return null;
		}
	}

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
		SupplierBuilder<Concept> builder = new SupplierBuilder<Concept>() {

			@Override
			public ListSupplier<Concept> build(ApiClient apiClient) {
				return new ListSupplier<Concept>() {

					@Override
					public List<Concept> getList() {
						ConceptsApi conceptsApi = new ConceptsApi(apiClient);
						
						try {
							List<InlineResponse2001> responses = conceptsApi.getConcepts(
									urlEncode(keywords),
									urlEncode(semanticGroups),
									pageNumber,
									pageSize
							);
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
							
						} catch (Exception e) {
							return new ArrayList<Concept>();
						}
					}
					
				};
			}
			
		};
		
		return query(builder);
	}
	
	public CompletableFuture<List<Concept>> getConceptDetails(String conceptId) {
		SupplierBuilder<Concept> builder = new SupplierBuilder<Concept>() {

			@Override
			public ListSupplier<Concept> build(ApiClient apiClient) {
				return new ListSupplier<Concept>() {

					@Override
					public List<Concept> getList() {
						ConceptsApi conceptsApi = new ConceptsApi(apiClient);
						
						try {
							List<InlineResponse200> responses = conceptsApi.getConceptDetails(
									urlEncode(conceptId)
							);
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
							
						} catch (Exception e) {
							return new ArrayList<Concept>();
						}
					}
					
				};
			}
			
		};
		return query(builder);
	}
	
	public CompletableFuture<List<Statement>> getStatements(
			String emci,
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		SupplierBuilder<Statement> builder = new SupplierBuilder<Statement>() {

			@Override
			public ListSupplier<Statement> build(ApiClient apiClient) {
				return new ListSupplier<Statement>() {

					@Override
					public List<Statement> getList() {
						StatementsApi statementsApi = new StatementsApi(apiClient);
						
						String[] emcis = emci.split(" ");
						
						for (int i = 0; i < emcis.length; i++) {
							emcis[i] = urlEncode(emcis[i]);
						}
						
						try {
							List<InlineResponse2002> responses = statementsApi.getStatements(
									Arrays.asList(emcis),
									pageNumber,
									pageSize,
									urlEncode(keywords),
									urlEncode(semanticGroups)
							);
							List<Statement> statements = new ArrayList<Statement>();
							
							for (InlineResponse2002 response : responses) {
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
							
						} catch (Exception e) {
							return new ArrayList<Statement>();
						}
					}
					
				};
			}
			
		};
		return query(builder);
	}
	
	/**
	 * In our project, annotations really play this role of evidence.
	 */
	public CompletableFuture<List<Annotation>> getEvidences(
			String statementId,
			String keywords,
			int pageNumber,
			int pageSize
	) {
		SupplierBuilder<Annotation> builder = new SupplierBuilder<Annotation>() {

			@Override
			public ListSupplier<Annotation> build(ApiClient apiClient) {
				return new ListSupplier<Annotation>() {

					@Override
					public List<Annotation> getList() {
						EvidenceApi evidenceApi = new EvidenceApi(apiClient);
						
						try {
							List<InlineResponse2003> responses = evidenceApi.getEvidence(
									urlEncode(statementId),
									urlEncode(keywords),
									pageNumber,
									pageSize
							);
							
							List<Annotation> annotations = new ArrayList<Annotation>();
							
							for (InlineResponse2003 response : responses) {
								Annotation annotation = new AnnotationImpl();
								annotation.setId(response.getId());
								annotation.setName(response.getLabel());
								annotation.setPublicationDate(response.getDate());
								
								annotations.add(annotation);
							}
							
							return annotations;
							
						} catch (Exception e) {
							return new ArrayList<Annotation>();
						}
					}
					
				};
			}
			
		};
		return query(builder);
	}
	
}
