
package bio.knowledge.service.beacon;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.squareup.okhttp.OkHttpClient;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.AggregatorApi;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.PredicatesApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.BeaconAnnotation;
import bio.knowledge.client.model.BeaconCliqueIdentifier;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconConceptDetail;
import bio.knowledge.client.model.BeaconConceptWithDetails;
import bio.knowledge.client.model.BeaconConceptWithDetailsBeaconEntry;
import bio.knowledge.client.model.BeaconConceptsQueryResult;
import bio.knowledge.client.model.BeaconKnowledgeBeacon;
import bio.knowledge.client.model.BeaconPredicate;
import bio.knowledge.client.model.BeaconPredicatesByBeacon;
import bio.knowledge.client.model.BeaconStatementObject;
import bio.knowledge.client.model.BeaconStatementPredicate;
import bio.knowledge.client.model.BeaconStatementSubject;
import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.AnnotatedConcept.BeaconEntry;
import bio.knowledge.model.AnnotatedConceptImpl;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.AnnotationImpl;
import bio.knowledge.model.ConceptDetailImpl;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.GeneralStatement;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.IdentifiedConceptImpl;
import bio.knowledge.model.OntologyTermImpl;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.Predicate.PredicateBeacon;
import bio.knowledge.model.PredicateImpl;
import bio.knowledge.model.Statement;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.OntologyTerm;
import bio.knowledge.model.util.Util;

/**
 * 
 * @author Lance Hannestad
 * 
 *         It may seem wasteful to instantiate a new {@code ConceptApi} (or
 *         other API classes) within each {@code ListSupplier<T>}, but in fact
 *         it is necessary because we're asynchronously setting their ApiClient
 *         objects (which encapsulate the URI to be queried) in
 *         {@code GenericDataService}.
 *         <br/><br/>
 *         The methods in this class are ugly and confusing.. But it's somewhat
 *         unavoidable. Take a look at how they are used in
 *         {@code GenericKnowledgeService}. A SupplierBuilder builds a
 *         ListSupplier which extends a Supplier, which is used to generate
 *         CompletableFutures.
 *
 */
@Service
public class KnowledgeBeaconService implements Util {
	
	private Logger _logger = LoggerFactory.getLogger(KnowledgeBeaconService.class);
	
	/*
	 *  ApiClient timeout weightings here are in milliseconds
	 *  These are used below alongside beacon number and pagesizes 
	 *  to set some reasonable timeouts for various queries
	 */
	public final int DEFAULT_TIMEOUT_WEIGHTING = 5000;
	public final int CONCEPTS_QUERY_TIMEOUT_WEIGHTING   = 20000;
	public final int STATEMENTS_QUERY_TIMEOUT_WEIGHTING = 60000; 

	/*
	 * Here we need to discriminate between the
	 * "local" private (i.e. Docker LAN) version of
	 * the beacon aggregator URL and the fully
	 * public "external" URL.
	 */
	@Value( "${publicBeaconAggregator.url}" )
	private String PUBLIC_AGGREGATOR_BASE_URL;
	
	@Value( "${beaconAggregator.url}" )
	private String AGGREGATOR_BASE_URL;
	
	@Autowired
	private KnowledgeBeaconRegistry registry;
	
	public String getPublicAggregatorBaseUrl() {
		if (!PUBLIC_AGGREGATOR_BASE_URL.startsWith("http://") && !PUBLIC_AGGREGATOR_BASE_URL.startsWith("https://")) {
			PUBLIC_AGGREGATOR_BASE_URL = "http://" + PUBLIC_AGGREGATOR_BASE_URL;
		}
		
		return PUBLIC_AGGREGATOR_BASE_URL;
	}
	
	public String getAggregatorBaseUrl() {
		if (!AGGREGATOR_BASE_URL.startsWith("http://") && !AGGREGATOR_BASE_URL.startsWith("https://")) {
			AGGREGATOR_BASE_URL = "http://" + AGGREGATOR_BASE_URL;
		}
		
		return AGGREGATOR_BASE_URL;
	}
	
	private Map<Integer, String> beaconIdMap = null;
	private List<BeaconKnowledgeBeacon> beacons = null;
	
	//private ApiClient apiClient;
	private ConceptsApi conceptsApi;
	private PredicatesApi predicatesApi;
	private StatementsApi statementsApi;
	private EvidenceApi evidenceApi;
	private AggregatorApi aggregatorApi;
	
	public ConceptsApi getConceptsApi(int pageSize) {
		
		conceptsApi.getApiClient().setConnectTimeout( apiWeightedTimeout( CONCEPTS_QUERY_TIMEOUT_WEIGHTING, new Integer(pageSize)) );
		_logger.debug("kbs.getConcepts() conceptsApi() connect timeout is currently set to '"+new Integer(conceptsApi.getApiClient().getConnectTimeout())+"' milliseconds");

		OkHttpClient httpClient = conceptsApi.getApiClient().getHttpClient();
		httpClient.setReadTimeout( apiWeightedTimeout( CONCEPTS_QUERY_TIMEOUT_WEIGHTING, new Integer(pageSize)), TimeUnit.MILLISECONDS);
		_logger.debug("kbs.getConcepts() conceptsApi() HTTP client read timeout is currently set to '"+new Long(httpClient.getReadTimeout())+"' milliseconds");

		return conceptsApi;
	}
	
	public ConceptsApi getConceptsApi() {
		return getConceptsApi(0);  // default without pagesize
	}
	
	public PredicatesApi getPredicatesApi() {
		return this.predicatesApi;
	}
	
	public StatementsApi getStatementsApi(int pageSize) {
		
		statementsApi.getApiClient().setConnectTimeout( apiWeightedTimeout( STATEMENTS_QUERY_TIMEOUT_WEIGHTING, new Integer(pageSize)) );
		_logger.debug("kbs.getStatements() getStatementsApi() connect timeout is currently set to '"+new Integer(statementsApi.getApiClient().getConnectTimeout())+"' milliseconds");

		OkHttpClient httpClient = statementsApi.getApiClient().getHttpClient();
		httpClient.setReadTimeout( apiWeightedTimeout( STATEMENTS_QUERY_TIMEOUT_WEIGHTING, new Integer(pageSize)), TimeUnit.MILLISECONDS);
		_logger.debug("kbs.getStatements() getStatementsApi() HTTP client read timeout is currently set to '"+new Long(httpClient.getReadTimeout())+"' milliseconds");

		return statementsApi;
	}
	
	public EvidenceApi getEvidenceApi() {
		return this.evidenceApi;
	}
	
	public AggregatorApi getAggregatorApi() {
		return this.aggregatorApi;
	}
	
	public static final long     BEACON_TIMEOUT_DURATION = 1;
	public static final TimeUnit BEACON_TIMEOUT_UNIT = TimeUnit.MINUTES;

	/**
	 * Dynamically compute adjustment to query timeouts proportionately to 
	 * the number of beacons and pageSize

	 * @param beacons
	 * @param pageSize
	 * @return
	 */
	public long weightedTimeout( List<String> beacons, Integer pageSize ) {
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
	
	public int apiWeightedTimeout(Integer timeOutWeighting,List<String> beacons, Integer pageSize ) {
		int numberOfBeacons = beacons!=null ? beacons.size() : registry.countAllBeacons() ;
		_logger.debug("apiWeightedTimeout parameters: timeout weight: "+ timeOutWeighting + ", # beacons: "+ numberOfBeacons +", data page size: "+ pageSize);
		return timeOutWeighting*(int)weightedTimeout(beacons,pageSize);
	}
	
	public int apiWeightedTimeout(Integer timeOutWeighting, Integer pageSize) {
		return apiWeightedTimeout(timeOutWeighting, null, pageSize );
	}
	
	public int apiWeightedTimeout(Integer timeOutWeighting) {
		return apiWeightedTimeout(timeOutWeighting, 0);
	}
	
	public int apiWeightedTimeout() {
		return apiWeightedTimeout(DEFAULT_TIMEOUT_WEIGHTING);
	}
	
	@PostConstruct
	private void initBeacons() {
		
		/*
		 *  RMB: Oct 16, 2017
		 *  This function originally assigned a single ApiCLient 
		 *  for all the various distinct Api data type (concepts, 
		 *  predicates, statements, et al.) classes, but it is 
		 *  unclear if this is the right thing to do since one 
		 *  may need to process some concurrent activities, 
		 *  class-specific timeouts, etc. Therefore, I have 
		 *  rewritten the code to create a separate 
		 *  ApiClient instance for each data type class.
		 */
		conceptsApi = new ConceptsApi(new ApiClient().setBasePath(AGGREGATOR_BASE_URL));
		predicatesApi = new PredicatesApi(new ApiClient().setBasePath(AGGREGATOR_BASE_URL));
		statementsApi = new StatementsApi(new ApiClient().setBasePath(AGGREGATOR_BASE_URL));
		evidenceApi = new EvidenceApi(new ApiClient().setBasePath(AGGREGATOR_BASE_URL));
		aggregatorApi = new AggregatorApi(new ApiClient().setBasePath(AGGREGATOR_BASE_URL));
	}
	
	public List<BeaconKnowledgeBeacon> getKnowledgeBeacons() {
		if (beacons == null) {
			setupBeacons();
		}
		return beacons;
	}
	
	public Map<Integer, String> getBeaconIdMap() {
		if (beaconIdMap == null) {
			setupBeacons();
		}
		return beaconIdMap;
	}
	
	public int countAllBeacons() {
		return getBeaconIdMap().size();
	}
	
	private void setupBeacons() {
		try {
			beaconIdMap = new HashMap<Integer, String>();
			beacons = aggregatorApi.getBeacons();
			for (BeaconKnowledgeBeacon b : beacons) {
				beaconIdMap.put(b.getBeacon(), b.getName());
			}
		} catch (ApiException e) {
			beaconIdMap = null;
			throw new RuntimeException("Could not connect to the Beacon Aggregator. Make sure that it is online and that you have set up application.properties properly");
		}
	}

	private String getBeaconNameFromId(Integer id) {
		return getBeaconIdMap().get(id);
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
	public CompletableFuture<List<IdentifiedConcept>> getConcepts(String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize,
			List<Integer> beacons,
			String queryId
			
	) {
		CompletableFuture<List<IdentifiedConcept>> future = 
				
			CompletableFuture.supplyAsync(
					
				new Supplier<List<IdentifiedConcept>>() {
	
					@Override
					public List<IdentifiedConcept> get() {
						
						// Utility time variable for profiling
						Instant start = Instant.now();
						
						List<IdentifiedConcept> concepts = new ArrayList<IdentifiedConcept>();
						
						try {
							
							_logger.debug("kbs.getConcepts() - before responses");
							
							BeaconConceptsQueryResult response = 
									getConceptsApi(pageSize).getConcepts(
											queryId,
											beacons,
											pageNumber,
											pageSize
									);
		
							for (BeaconConcept beaconConcept : response.getResults()) {
								
								ConceptType category;
								try {
									String type = beaconConcept.getType();
									if (type.contains(":")) {
										// type might be a string combined from multiple types separated by spaces
										if (type.contains(" ")) {
											type = type.split("\\s")[0];
										}
										type = type.split(":", 2)[1].toUpperCase();
									}
									category = ConceptType.valueOf(type);
								} catch (IllegalArgumentException ex) {
									category = null;
								}
								
								IdentifiedConcept concept = new IdentifiedConceptImpl(
										beaconConcept.getClique(),
										beaconConcept.getName(),
										category
								);
								
								concepts.add(concept);
							}
		
							_logger.debug("Concept retrieval successfully completed?");
							
						} catch (Exception e) {
							_logger.error("getConcepts() ERROR: "+e.getMessage());
						}
						
						Instant end = Instant.now();
						
						// Time of exit either after success or timeout failure
						long ms = start.until(end, ChronoUnit.MILLIS);
						_logger.error("getConcepts() ApiClient data processing duration was: "+new Long(ms)+" milliseconds.");
						
						return concepts;
					}
					
				});
		
		return future;
	}
	
	/**
	 * 
	 * @return
	 */
	public CompletableFuture<String> findByIdentifier(String identifier) {
		
		CompletableFuture<String> future = 
				CompletableFuture.supplyAsync( new Supplier<String>() {

			@Override
			public String get() {
				
				try {
					BeaconCliqueIdentifier response = 
							getConceptsApi().getClique(identifier);
					
					if(response != null) 
						return response.getCliqueId();

				} catch (Exception e) {
					_logger.warn("KnowledgeBeaconService.findByIdentifier() ERROR: "
							+ "BeaconCliqueIdentifier not found for "
							+ "identifier '"+identifier+"'? Exception: " + e.getMessage());
				}
				return "";
			}
		});
		
		return future;
	}
	
	private OntologyTerm resolveTag(String tag) {
		// TODO Does this simple-minded implementation need to be elaborated?
		// For example, do I need to manage a catalog of known ontology terms here?
		return new OntologyTermImpl(tag);
	}
	
	/**
	 * The beacon aggregator called by this method via the ConceptsApi, searches across the clique identifier.
	 * 
	 * @param cliqueId
	 * @param beacons
	 * @param sessionId
	 * @return
	 */
	public CompletableFuture<AnnotatedConcept> getConceptWithDetails( 
			String cliqueId,
			List<Integer> beacons,
			String sessionId
		) {
		CompletableFuture<AnnotatedConcept> future = 
				CompletableFuture.supplyAsync(new Supplier<AnnotatedConcept>() {

			@Override
			public AnnotatedConcept get() {
				
				AnnotatedConceptImpl concept = null;
				
				try {
					BeaconConceptWithDetails response = 
							getConceptsApi().getConceptDetails( cliqueId, beacons );

					ConceptType category;
					try {
						String type = response.getType();
						if (type.contains(":")) {
							// type might be a string combined from multiple types separated by spaces
							if (type.contains(" ")) {
								type = type.split("\\s")[0];
							}
							type = type.split(":", 2)[1].toUpperCase();
						}
						category = ConceptType.valueOf(type);

					} catch (IllegalArgumentException e) {
						category = null;
					}

					concept = new AnnotatedConceptImpl(
							response.getClique(),
							response.getName(),
							category
							);

					Set<String> xrefs = concept.getAliases() ;
					xrefs.addAll(response.getAliases());
					
					List<BeaconConceptWithDetailsBeaconEntry> beaconEntries = response.getEntries() ;
					List<BeaconEntry> conceptBeaconEntries = concept.getEntries();
					
					for(BeaconConceptWithDetailsBeaconEntry entry : beaconEntries) {

						Integer beacon = entry.getBeacon();
						String id = entry.getId();
						
						BeaconEntry beaconEntry = 
								concept.new ConceptBeaconEntry(beacon,id);
								
						List<String> synonyms = entry.getSynonyms();
						beaconEntry.getSynonyms().addAll(synonyms);
						
						String definition = entry.getDefinition();
						beaconEntry.setDefinition(definition);
						
						List<BeaconConceptDetail> beaconDetails = entry.getDetails();
						Set<Feature> conceptDetails = beaconEntry.getDetails();
						
						for(BeaconConceptDetail detail : beaconDetails) {
							Feature feature = new ConceptDetailImpl();
							OntologyTerm tag = resolveTag(detail.getTag());
							feature.setTag(tag);
							feature.setValue(detail.getValue());
							conceptDetails.add(feature);
						}
						
						conceptBeaconEntries.add(beaconEntry);
					}


				} catch (Exception e) {
					_logger.error("kbs.getConceptDetails() Exception: "+e.getMessage());
				}
				
				return concept;
			}
			
		});
		
		return future;
	}
	
	/**
	 * 
	 * @return
	 */
	public CompletableFuture<Set<Predicate>> getPredicates() {
		
		CompletableFuture<Set<Predicate>> future = 
				CompletableFuture.supplyAsync(new Supplier<Set<Predicate>>() {

			@Override
			public Set<Predicate> get() {
				
				Set<Predicate> predicates = new TreeSet<Predicate>();
				
				try {
					List<BeaconPredicate> responses = 
							getPredicatesApi().getPredicates();
					
					for ( BeaconPredicate response : responses ) {
						
						PredicateImpl predicate = new PredicateImpl();
						
						predicate.setName(response.getEdgeLabel());

						List<BeaconPredicatesByBeacon> beacons = response.getBeacons();
						if(beacons!=null)
							for(BeaconPredicatesByBeacon pb : beacons) {
								Predicate.PredicateBeacon beacon = 
										predicate.new PredicateBeaconImpl(
												pb.getBeacon(),
												pb.getId(),
												pb.getDefinition()
										) ;
								predicate.addBeacon(beacon);
							}
						
						predicates.add(predicate);
					}
										
				} catch (Exception e) {
					
				}
				
				return predicates;
			}
			
		});
		
		return future;
	}
	
	public CompletableFuture<List<Statement>> getStatements(
			String sourceClique,
			Set<Predicate> relations,
			String targetClique,
			String keywords,
			String categories,
			int pageNumber,
			int pageSize,
			List<String> beacons,
			String sessionId
	) {
		CompletableFuture<List<Statement>> future = CompletableFuture.supplyAsync(new Supplier<List<Statement>>() {

			@Override
			public List<Statement> get() {
				
				_logger.debug(
						"kbs.getStatements(): processing cliqueId: "+sourceClique+
						", keywords: "+keywords+
						", categories: "+categories+
						", relation: "+relations
				);
				
				// Utility time variable for profiling
				Instant start = Instant.now();
				
				List<Statement> statements = new ArrayList<Statement>();
				try {
					
					/*
					 * First iteration only supports filtering of statements
					 * on a single predicate relation.
					 * 
					 * Filtering by multiple predicate identifiers is supported
					 * by the beacon-aggregator but TKBio support will
					 * require recoding of the code stack above this point.
					 */
					String relationIds = "" ;
					if(!nullOrEmpty(relations)) {
						for(Predicate p : relations) {
						List<PredicateBeacon> beacons = p.getBeacons();
						if(!nullOrEmpty(beacons))
							for(PredicateBeacon beacon : beacons) {
								if(!relationIds.isEmpty())
									relationIds += " ";
								relationIds += beacon.getId();
							}
						}
					}
					
					_logger.debug("kbs.getStatements() - before responses");
				
					List<bio.knowledge.client.model.BeaconStatement> responses = 
							getStatementsApi(pageSize).getStatements(
									sourceClique,
									relationIds,
									targetClique,
									keywords,
									categories,
									pageNumber,
									pageSize,
									beacons,
									sessionId
							);

					_logger.debug("kbs.getStatements() - after responses");
					
					for (bio.knowledge.client.model.BeaconStatement response : responses) {
						
						String id = response.getId();
						
						BeaconStatementSubject statementsSubject = response.getSubject();
						BeaconStatementPredicate statementsPredicate = response.getPredicate();
						BeaconStatementObject statementsObject = response.getObject();

						IdentifiedConceptImpl subject = new IdentifiedConceptImpl(
								statementsSubject.getClique(), 
								statementsSubject.getId(),
								statementsSubject.getName(), 
								statementsSubject.getType()
						);
						subject.setId(sessionId);

						PredicateImpl predicate = new PredicateImpl(statementsPredicate.getName());

						IdentifiedConceptImpl object = new IdentifiedConceptImpl(
								statementsObject.getClique(), 
								statementsObject.getId(), 
								statementsObject.getName(),
								statementsObject.getType()
						);
						
						Statement statement = new GeneralStatement(id, subject, predicate, object);
						
						statement.setBeaconSource(getBeaconNameFromId(response.getBeacon()));
						
						statements.add(statement);
					}

					_logger.debug("Statement retrieval successfully completed?");

				} catch (Exception e) {
					_logger.error("getStatements() ERROR: "+e.getMessage());
				}
				
				Instant end = Instant.now();

				// Time of exit either after success or timeout failure
				long ms = start.until(end, ChronoUnit.MILLIS);
				_logger.error("getStatements() ApiClient data processing duration was: "+new Long(ms)+" milliseconds.");
				
				return statements;
			}
			
		});
		
		return future;
	}
	
	/**
	 * In our project, annotations really play this role of evidence.
	 */
	public CompletableFuture<List<Annotation>> getEvidence(
			String statementId,
			String keywords,
			int pageNumber,
			int pageSize,
			List<Integer> beacons,
			String sessionId
	) {
		CompletableFuture<List<Annotation>> future = CompletableFuture.supplyAsync(new Supplier<List<Annotation>>() {

			@Override
			public List<Annotation> get() {
				List<Annotation> annotations = new ArrayList<Annotation>();
				
				String[] strings = statementId.split("\\|");
				String id = strings.length >= 2 ? strings[2] : statementId;
				
				try {
					List<BeaconAnnotation> responses = getEvidenceApi().getEvidence(
							id,
							keywords,
							pageNumber,
							pageSize,
							beacons,
							sessionId
					);
					
					for (bio.knowledge.client.model.BeaconAnnotation response : responses) {
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
	
	public CompletableFuture<List<Annotation>> getEvidence(
			Statement statement,
			String keywords,
			int pageNumber,
			int pageSize,
			List<Integer> beacons,
			String sessionId
		) {
		return getEvidence( statement.getId(), keywords, pageNumber, pageSize, beacons, sessionId );
	}

	/**
	 * 
	 * @param emci
	 * @return
	 */
	public String discoverExactMatchClique(String emci) {
		return emci;
	}
	
	public int getKnowledgeBeaconCount( List<Integer> beacons ) {
		int cbsize = beacons==null?0:beacons.size();
		return cbsize > 0 ? cbsize : countAllBeacons();
	}

	
}

