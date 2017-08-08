package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

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
	public static String AGGREGATOR_BASE_URL = "https://kba.ncats.io";
	
	
	private ApiClient apiClient;
	private ConceptsApi conceptsApi;
	private StatementsApi statementsApi;
	private EvidenceApi evidenceApi;
	private ExactmatchesApi exactmatchesApi;
	
	@PostConstruct
	public void init() {
		apiClient = new ApiClient();
		apiClient.setBasePath(AGGREGATOR_BASE_URL);
		conceptsApi = new ConceptsApi(apiClient);
		statementsApi = new StatementsApi(apiClient);
		evidenceApi = new EvidenceApi(apiClient);
		exactmatchesApi = new ExactmatchesApi(apiClient);
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
					List<InlineResponse2001> responses = conceptsApi.getConcepts(keywords, semanticGroups, pageNumber, pageSize);
					
					for (InlineResponse2001 response : responses) {		
						Concept concept = ModelConverter.buildConcept(response);
						if (concept != null) {
							concepts.add(concept);
						}
					}
					
				} catch (ApiException e) {
					e.printStackTrace();
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
					List<InlineResponse200> responses = conceptsApi.getConceptDetails(conceptId);
					
					for (InlineResponse200 response : responses) {
						Concept concept = ModelConverter.buildConcept(response);
						if (concept != null) {
							concepts.add(concept);
						}
					}
				} catch (ApiException e) {
					e.printStackTrace();
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
					List<String> exactmatches = exactmatchesApi.getExactMatchesToConceptList(Arrays.asList(emci.split(" ")));
					
					if (exactmatches.isEmpty()) {
						exactmatches.addAll(Arrays.asList(emci.split(" ")));
					}
					
					List<InlineResponse2002> responses = statementsApi.getStatements(
							exactmatches,
							pageNumber,
							pageSize,
							keywords,
							semgroups
					);
					
					for (InlineResponse2002 response : responses) {
						Statement statement = ModelConverter.buildStatement(response);
						if (statement != null) {
							statements.add(statement);
						}
					}
				} catch (ApiException e) {
					e.printStackTrace();
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
				
				try {
					String[] strings = statementId.split("\\|");
					String id = strings.length >= 2 ? strings[2] : statementId;
					List<InlineResponse2003> responses = evidenceApi.getEvidence(id, keywords, pageNumber, pageSize);
					
					for (InlineResponse2003 response : responses) {
						Annotation annotation = ModelConverter.buildAnnotation(response);
						
						if (annotation != null) {
							if (strings.length >= 2) {
								// There can be an id in here!
								annotation.setUrl(strings[1]);
							}
							annotations.add(annotation);
						}
					}
					
				} catch (ApiException e) {
					e.printStackTrace();
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
		throw new RuntimeException("NOT IMPLEMENTED YET");
//		return 7;
	}
	
}
