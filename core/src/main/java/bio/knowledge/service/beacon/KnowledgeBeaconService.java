package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 *         it is necessary because we're asynchronously setting their ApiClient
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
public class KnowledgeBeaconService extends KnowledgeBeaconServiceBase {
	
	@Autowired
	private KnowledgeBeaconRegistry registry;
	
	public static final long     BEACON_TIMEOUT_DURATION = 1;
	public static final TimeUnit BEACON_TIMEOUT_UNIT = TimeUnit.MINUTES;

	/**
	 * Dynamically compute adjustment to query timeouts proportionately to 
	 * the number of beacons and pageSize

	 * @param beacons
	 * @param pageSize
	 * @return
	 */
	public long weightedTimeout( List<String> beacons, Integer pageSize) {
		long timescale;
		if(!(beacons==null || beacons.isEmpty())) 
			timescale = beacons.size();
		else
			timescale = registry.countAllBeacons();
		
		timescale *= Math.max(1,pageSize/10) ;
		
		return timescale*BEACON_TIMEOUT_DURATION;
	}
	
	/**
	 * Timeout simply weighted by total number of beacons and pagesize
	 * @return
	 */
	public long weightedTimeout(Integer pageSize) {
		return weightedTimeout(null, pageSize); // 
	}
	
	/**
	 * Timeout simply weighted by number of beacons
	 * @return
	 */
	public long weightedTimeout() {
		return weightedTimeout(null, 0); // 
	}
	
	private List<String> customBeacons = null;
	private String sessionId = null;
	
	private String getBeaconNameFromId(String id) {
		return getBeaconIdMap().get(id);
	}
	
	public void setCustomBeacons(List<String> customBeacons){ 
		this.customBeacons = customBeacons;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public void clearCustomBeacons() {
		customBeacons = null;
	}
	
	public void clearSessionId() {
		sessionId = null;
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
					List<bio.knowledge.client.model.Concept> responses = getConceptsApi().getConcepts(
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
								response.getClique(),
								response.getId(),
								semgroup,
								response.getName()
						);
						
						Set<String> xrefs = concept.getCrossReferences() ;
						xrefs.addAll(response.getAliases());
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						concept.setBeaconSource(getBeaconNameFromId(response.getBeacon()));
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
					List<bio.knowledge.client.model.ConceptDetail> responses = getConceptsApi().getConceptDetails(conceptId, customBeacons, sessionId);
					
					for (bio.knowledge.client.model.ConceptDetail response : responses) {
						SemanticGroup semgroup;
						try {
							semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						} catch (IllegalArgumentException e) {
							semgroup = null;
						}
						
						Concept concept = new ConceptImpl(
								response.getClique(),
								response.getId(),
								semgroup,
								response.getName()
						);

						Set<String> xrefs = concept.getCrossReferences() ;
						xrefs.addAll(response.getAliases());
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						concept.setBeaconSource(getBeaconNameFromId(response.getBeacon()));
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
					List<bio.knowledge.client.model.Statement> responses = getStatementsApi().getStatements(
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
						
						Subject statementsSubject = response.getSubject();
						bio.knowledge.client.model.Predicate statementsPredicate = response.getPredicate();
						bio.knowledge.client.model.Object statementsObject = response.getObject();

						ConceptImpl subject = new ConceptImpl(
								statementsSubject.getClique(), 
								statementsSubject.getId(), 
								statementsSubject.getSemgroup(), 
								statementsSubject.getName()
						);

						PredicateImpl predicate = new PredicateImpl(statementsPredicate.getName());

						ConceptImpl object = new ConceptImpl(
								statementsObject.getClique(), 
								statementsObject.getId(), 
								statementsObject.getSemgroup(), 
								statementsObject.getName()
						);
						
						Statement statement = new GeneralStatement(id, subject, predicate, object);
						
						statement.setBeaconSource(getBeaconNameFromId(response.getBeacon()));
						
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
					List<bio.knowledge.client.model.Annotation> responses = getEvidenceApi().getEvidence(
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
						
						annotation.setBeaconSource(getBeaconNameFromId(response.getBeacon()));
						
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
		return customBeacons != null ? customBeacons.size() : getBeaconIdMap().size();
	}

	public boolean hasSessionId() {
		return this.sessionId != null;
	}
	
}
