package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.StatementsObject;
import bio.knowledge.client.model.StatementsPredicate;
import bio.knowledge.client.model.Subject;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.AnnotationImpl;
import bio.knowledge.model.Concept;
import bio.knowledge.model.ConceptImpl;
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
public class KnowledgeBeaconService {
	public static String AGGREGATOR_BASE_URL = "https://kba.ncats.io";
	
	
	private ApiClient apiClient;
	private ConceptsApi conceptsApi;
	private StatementsApi statementsApi;
	private EvidenceApi evidenceApi;
	
	private List<String> customBeacons = null;
	private String sessionId = null;
	
	@PostConstruct
	public void init() {
		apiClient = new ApiClient();
		apiClient.setBasePath(AGGREGATOR_BASE_URL);
		conceptsApi = new ConceptsApi(apiClient);
		statementsApi = new StatementsApi(apiClient);
		evidenceApi = new EvidenceApi(apiClient);
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
		CompletableFuture<List<Concept>> future = CompletableFuture.supplyAsync(new Supplier<List<Concept>>() {

			@Override
			public List<Concept> get() {
				List<Concept> concepts = new ArrayList<Concept>();
				
				try {
					List<bio.knowledge.client.model.Concept> responses = conceptsApi.getConcepts(
							keywords,
							semanticGroups,
							pageNumber,
							pageSize,
							customBeacons,
							sessionId
					);
					for (bio.knowledge.client.model.Concept response : responses) {
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
					
				} catch (Exception e) {
					
				}
				
				return concepts;
			}
			
		});
		return future;
	}
	
	public CompletableFuture<List<Concept>> getConceptDetails(String conceptId) {
		CompletableFuture<List<Concept>> future = CompletableFuture.supplyAsync(new Supplier<List<Concept>>() {

			@Override
			public List<Concept> get() {
				List<Concept> concepts = new ArrayList<Concept>();
				
				try {
					List<bio.knowledge.client.model.ConceptDetail> responses = conceptsApi.getConceptDetails(conceptId, customBeacons, sessionId);
					
					for (bio.knowledge.client.model.ConceptDetail response : responses) {
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

				}
				
				return concepts;
			}
			
		});
		
		return future;
	}
	
	public CompletableFuture<List<Statement>> getStatements(
			String emci,
			String keywords,
			String semgroups,
			int pageNumber,
			int pageSize
	) {
		CompletableFuture<List<Statement>> future = CompletableFuture.supplyAsync(new Supplier<List<Statement>>() {

			@Override
			public List<Statement> get() {
				List<Statement> statements = new ArrayList<Statement>();
				try {					
					List<bio.knowledge.client.model.Statement> responses = statementsApi.getStatements(
							Arrays.asList(emci.split(" ")),
							pageNumber,
							pageSize,
							keywords,
							semgroups,
							customBeacons,
							sessionId
					);

					for (bio.knowledge.client.model.Statement response : responses) {
						String id = response.getId();
						StatementsObject statementsObject = response.getObject();
						Subject statementsSubject = response.getSubject();
						StatementsPredicate statementsPredicate = response.getPredicate();

						ConceptImpl subject = new ConceptImpl(statementsSubject.getId(), null,
								statementsSubject.getName());

						ConceptImpl object = new ConceptImpl(statementsObject.getId(), null,
								statementsObject.getName());

						PredicateImpl predicate = new PredicateImpl(statementsPredicate.getName());
						
						Statement statement = new GeneralStatement(id, subject, predicate, object);
						
						statements.add(statement);
					}


				} catch (Exception e) {

				}
				
				return statements;
			}
			
		});
		
		return future;
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
		CompletableFuture<List<Annotation>> future = CompletableFuture.supplyAsync(new Supplier<List<Annotation>>() {

			@Override
			public List<Annotation> get() {
				List<Annotation> annotations = new ArrayList<Annotation>();
				
				String[] strings = statementId.split("\\|");
				String id = strings.length >= 2 ? strings[2] : statementId;
				
				try {
					List<bio.knowledge.client.model.Annotation> responses = evidenceApi.getEvidence(
							id,
							keywords,
							pageNumber,
							pageSize,
							customBeacons,
							sessionId
					);
					
					for (bio.knowledge.client.model.Annotation response : responses) {
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
										
				} catch (Exception e) {
					
				}
				
				return annotations;
			}
			
		});
		
		return future;
	}
	
	public CompletableFuture<List<Annotation>> getEvidences(
			Statement statement,
			String keywords,
			int pageNumber,
			int pageSize
		) {
		return getEvidences(statement.getId(), keywords, pageNumber, pageSize);
	}

	/**
	 * 
	 * @param emci
	 * @return
	 */
	public String discoverExactMatchClique(String emci) {
		return emci;
	}
	
	public int getKnowledgeBeaconCount() {
//		throw new RuntimeException("NOT IMPLEMENTED YET");
		return 7;
	}
	
}
