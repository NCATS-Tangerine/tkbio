package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.gson.JsonSyntaxException;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.ExactmatchesApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2002;
import bio.knowledge.client.model.InlineResponse2003;
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
//		if (string != null) {
//			return string.replace(".", "%2E").replace(" ", "%20").replace(":", "%3A");
//		} else {
//			return null;
//		}
		return string;
	}
	
	private void printError(ApiClient apiClient, Exception e) {
		System.err.println("Error Querying:   " + apiClient.getBasePath());
		System.err.println("Error message:    " + e.getMessage());
		if (e instanceof JsonSyntaxException) {
			System.err.println("PROBLEM WITH DESERIALIZING SERVER RESPONSE");
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
		final String sg = semanticGroups;
		
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
									urlEncode(sg),
									pageNumber,
									pageSize
							);
							List<Concept> concepts = new ArrayList<Concept>();
							for (InlineResponse2001 response : responses) {
								SemanticGroup semgroup;
								try {
									semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
								} catch (IllegalArgumentException ex) {
									semgroup = null;
								}
								
								Concept concept = new ConceptImpl(
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
							printError(apiClient, e);
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
								SemanticGroup semgroup;
								try {
									semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
								} catch (IllegalArgumentException e) {
									semgroup = null;
								}
								
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
							printError(apiClient, e);
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
			String semgroups,
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
						ExactmatchesApi exactMatchesApi = new ExactmatchesApi(apiClient);
						
						/* *****************************************************************
						 *  May 27, 2017 RMB: 
						 *  First implementation of the resolution of 'exact match' concepts
						 *  is undertaken here, just before statement retrieval; however, this
						 *  particular implementation may have some limitations.  The basic
						 *  assumption made here is that the list of input emcis only need 
						 *  to be locally resolved with the current Knowledge Beacon 
						 *  processing the call; however, it may be the case that some
						 *  other Knowledge Beacon knows about an equivalency that may be
						 *  useful here, in this Knowledge Beacon. This would not be known
						 *  without querying the whole series of available Knowledge Beacons
						 *  perhaps iteratively.
						 ******************************************************************/
						String[] emcis = emci.split(" ");
						
						Set<String> exactMatches = new HashSet<String>();
						try {

							for (int i = 0; i < emcis.length; i++) {
								emcis[i] = urlEncode(emcis[i]);

								List<String> matches = exactMatchesApi.getExactMatchesToConcept(emcis[i]);
								if (matches != null) {
									exactMatches.addAll(matches);
								}
							}

							exactMatches.addAll(Arrays.asList(emcis));
							List<String> conceptIds = new ArrayList<String>();
							conceptIds.addAll(exactMatches);
							
							/*-*****************************************************************/
							
							List<InlineResponse2002> responses = statementsApi.getStatements(conceptIds, pageNumber,
									pageSize, keywords, semgroups);
							List<Statement> statements = new ArrayList<Statement>();

							for (InlineResponse2002 response : responses) {
								String id = response.getId();
								StatementsObject statementsObject = response.getObject();
								StatementsSubject statementsSubject = response.getSubject();
								StatementsPredicate statementsPredicate = response.getPredicate();

								ConceptImpl subject = new ConceptImpl(statementsSubject.getId(), null,
										statementsSubject.getName());

								ConceptImpl object = new ConceptImpl(statementsObject.getId(), null,
										statementsObject.getName());

								PredicateImpl predicate = new PredicateImpl(statementsPredicate.getName());
								
								Statement statement = new GeneralStatement(id, subject, predicate, object);
								statement.setBeaconUrl(apiClient.getBasePath());
								statements.add(statement);
							}

							return statements;

						} catch (Exception e) {
							printError(apiClient, e);
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
						
						/**
						 * Wikidata statement id's sometimes contain url's. We want to extract that!
						 */
						String[] strings = statementId.split("\\|");
						String id = strings.length >= 2 ? strings[2] : statementId;
						
						try {
							List<InlineResponse2003> responses = evidenceApi.getEvidence(
									urlEncode(id),
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
								
								if (strings.length >= 2) {
									// There can be an id in here!
									annotation.setUrl(strings[1]);
								}
								
								annotations.add(annotation);
							}
							
							return annotations;
							
						} catch (Exception e) {
							printError(apiClient, e);
							return new ArrayList<Annotation>();
						}
					}
					
				};
			}
			
		};
		return query(builder);
	}
	
	public CompletableFuture<List<Annotation>> getEvidences(
			Statement statement,
			String keywords,
			int pageNumber,
			int pageSize
		) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(statement.getBeaconUrl());
		EvidenceApi evidenceApi = new EvidenceApi(apiClient);
		
		CompletableFuture<List<Annotation>> future =
				CompletableFuture.supplyAsync(new Supplier<List<Annotation>>() {
					@Override
					public List<Annotation> get() {
						try {
							List<InlineResponse2003> responses = 
									evidenceApi.getEvidence(statement.getId(), keywords, pageNumber, pageSize);
							
							List<Annotation> annotations = new ArrayList<Annotation>();
							for (InlineResponse2003 response : responses) {
								Annotation annotation = new AnnotationImpl();
								annotation.setId(response.getId());
								annotation.setPublicationDate(response.getDate());
								annotation.setName(response.getLabel());
								annotations.add(annotation);
							}
							return annotations;
						} catch (ApiException e) {
							return new ArrayList<Annotation>();
						}
					}
		});
		
		return future;
	}

	/**
	 * 
	 * @param emci
	 * @return
	 */
	public String discoverExactMatchClique(String emci) {
		return emci;
	}
	
}
