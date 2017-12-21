/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataServiceUtility;
import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.RdfUtil;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
//import bio.knowledge.service.core.FeatureService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.TableSorter;
import bio.knowledge.service.wikidata.WikiDataService;

/**
 * @author Richard
 *
 */
@Service
public class ConceptService 
	extends IdentifiedEntityServiceImpl<IdentifiedConcept>
	implements DataServiceUtility {
	
	private Logger _logger = LoggerFactory.getLogger(ConceptService.class);
	
	@Autowired
	private KBQuery query ;
	
	@Autowired
	private Cache cache;
	
	@Autowired
	private DataSourceRegistry dataSourceRegistry ;
	
	@Autowired
	WikiDataService wikiDataService ;
	
	//@Autowired
	//private FeatureService featureService ;
    
    @Autowired
    private KnowledgeBeaconService kbService;
    
    @Override
    public List<IdentifiedConcept> getDataPage(
    		int pageIndex,
    		int pageSize,
    		String filter,
    		TableSorter sorter,
    		boolean isAscending
    ) {
		
		// Capture the Semantic Group filter for use in the concept query
		Optional< Set<ConceptType> > semgroupsOpt = query.getInitialConceptTypes() ;
		String semgroups = "" ;
		if(semgroupsOpt.isPresent()) {
			Set<ConceptType> semgroupSet = semgroupsOpt.get();
			for(ConceptType sg : semgroupSet) {
				semgroups += sg.name()+" ";
			}
			semgroups = semgroups.trim();
		}
		
		List<String> beacons = query.getCustomBeacons();
		String sessionId = query.getUserSessionId();
		
    	CompletableFuture<List<IdentifiedConcept>> future =
    			kbService.getConcepts(filter, semgroups, pageIndex, pageSize,beacons,sessionId);
    	
    	try {
			return future.get(
					kbService.weightedTimeout(pageSize*10), // initial concept retrieval may be heavy... add an extra weighting
					KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
			);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return new ArrayList<IdentifiedConcept>();
		}
    }
    
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	
	public Neo4jConcept createInstance(Object... args) {
		if (args.length == 2)
			if (args[0] instanceof ConceptType) {
				return new Neo4jConcept(
						(ConceptType)   args[0], // SemanticGroup
						(String)          args[1]  // Concept.name
				);
			} else
				throw new RuntimeException("Invalid 1st argument to ConceptService.createInstance() ?");
		
		else if (args.length == 3)
			if (args[1] instanceof ConceptType) {
				return new Neo4jConcept(
						(String)        args[0], // Concept.id
						(ConceptType) args[1], // SemanticGroup
						(String)        args[2]  // Concept.name
				);
			} else
				throw new RuntimeException("Invalid 2nd argument to ConceptService.createInstance() ?");
		else
			throw new RuntimeException("Invalid number of ConceptService.createInstance() arguments?");
	}
    
	//@Autowired
	//private GraphDatabaseService graphDb;
   /* 
    private Stream<Concept> getConceptStream() {
    	List<Concept> Concepts = new ArrayList<Concept>() ;
    	for(Concept c : conceptRepository.getConcepts()) {
    		Concepts.add(c) ;
    	}
    	return Concepts.stream() ;
    }
    
    public List<Concept> getConcepts() {
    	if(Concepts.isEmpty()) {
    		Concepts = getConceptStream().sorted().collect(toList()) ;
    	}
    	return Concepts ;
    }
    */
    
    /* (non-Javadoc)
     * 
     * Should never actually be called since the system 
     * has a non-null ListView mapping to Concept data
     * so I return an empty list for expediency
     * 
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<IdentifiedConcept> getIdentifiers() {
		//return getConcepts();
		return new ArrayList<IdentifiedConcept>();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<IdentifiedConcept> getIdentifiers(Pageable pageable) {
		throw new RuntimeException("Not implemented in KB 4.0!") ;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<IdentifiedConcept> findByNameLike(String filter, Pageable pageable) {
		_logger.trace("Inside ConceptService.findByNameLike()");

		List<String> beacons = query.getCustomBeacons();
		String sessionId = query.getUserSessionId();
		
		return findAllFiltered(filter,pageable,beacons,sessionId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	// TODO: I think this is where the refactoring faltered
	private Page<IdentifiedConcept> findAllFiltered(
			String filter, 
			Pageable pageable,
			List<String> beacons,
			String sessionId
		) {
		
		CompletableFuture<List<IdentifiedConcept>> future = 
				kbService.getConcepts(
						filter,
						null,
						pageable.getPageNumber(),
						pageable.getPageSize(),
						beacons,
						sessionId
				);
		
		try {
			List<IdentifiedConcept> concepts = 
					future.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
			
//			String searchString = query.getCurrentQueryText();
//			if ( 
//					searchString == null 
//					
//					// Empty filter is problematic if used in a search so we don't allow it 
//					|| searchString.isEmpty() 
	//
//					/* DEPRECATED - we'll allow (helps user annotation discovery...)
//					 * We don't let people search against 
//					 * production with less than 
//					 * 3 characters... Too slow!
//					 */
////					|| searchString.trim().length()<3  
//				) {
//				return null;
//			} else {
//				searchString = filter + SEPARATOR + searchString;
//			}
	//
//			// getting selected semanticSemanticGroups filter after initial concept seach 
//			Optional<Set<SemanticGroup>> initialConceptTypesOpt = query.getInitialConceptTypes();
//			ArrayList<String> conceptTypes = new ArrayList<String>();
//			String conceptCodes = new String();
//			if (initialConceptTypesOpt.isPresent()) {
//				Set<SemanticGroup> initialConceptTypes = initialConceptTypesOpt.get();
//				for (SemanticGroup type : initialConceptTypes) {
//					conceptTypes.add(type.name());
//					// appending all concept types for making cache key
//					conceptCodes = conceptCodes + type.name();
//				}
//			}
//			
//			String pageKey = new Integer(pageable.hashCode()).toString();
//			CacheLocation cacheLocation = 
//					cache.searchForResultSet(
//							"Concept", 
//							searchString, 
//							new String[] { searchString, conceptCodes, pageKey }
//					);
//			
//			List<Neo4jConcept> searchedConceptResult = new ArrayList<>();
//			// Is key present ? then fetch it from cache
//			// List<Concept> cachedResult = (List<Concept>) cache.getResultSetCache().get(cacheKey);
//			
//			List<Neo4jConcept> cachedResult = (List<Neo4jConcept>) cacheLocation.getResultSet();
//			
//			// Is key present ? then fetch it from cache
//			if (cachedResult == null) {
//				String[] words = searchString.split(SEPARATOR);
//				if(words.length==0) {
//					searchedConceptResult = conceptRepository.findAllByPage(
//							pageable,
//							authenticationState.getUserId(),
//							authenticationState.getGroupIds()
//					);
//				} else {
//					if(filter.trim().isEmpty() && !initialConceptTypesOpt.isPresent()){
//						searchedConceptResult = conceptRepository.findByInitialSearch(
//								words,
//								pageable,
//								authenticationState.getUserId(),
//								authenticationState.getGroupIds()
//						);
//					} else {
//						searchedConceptResult = conceptRepository.findByNameLikeIgnoreCase(
//								conceptTypes,
//								words,
//								pageable,
//								authenticationState.getUserId(),
//								authenticationState.getGroupIds()
//						);
//					}
//				}
//				
//				//cache.getResultSetCache().put(cacheKey, searchedConceptResult);
//				cacheLocation.setResultSet(searchedConceptResult);
//				
//			} else {
//				searchedConceptResult = cachedResult;
//			}
//			return new PageImpl(searchedConceptResult);		
			
			return (Page<IdentifiedConcept>) (Page) new PageImpl(concepts);
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			future.completeExceptionally(e);
		}
		
		return (Page<IdentifiedConcept>) (Page) new PageImpl(new ArrayList<IdentifiedConcept>());
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<IdentifiedConcept> findAll(Pageable pageable) {
		_logger.trace("Inside ConceptService.findAll()");
		/*
		 *  "findAll()" for the initial concept search is not really "findAll" of all concepts
		 *  but rather, a search on the currently active concept, thus I'll constrain the search
		 *  to the currently known currentQueryString; The problem here is how to perform the 
		 *  secondary text filtering on the resulting table of data?
		 */

		List<String> beacons = query.getCustomBeacons();
		String sessionId = query.getUserSessionId();
		
		return findAllFiltered("",pageable,beacons,sessionId);
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		/*
		 *  "countEntries()" for the initial concept search is not really "countEntries" of all concepts
		 *  but rather, a search on the currently active concept, thus I'll constrain the search
		 *  to the currently known currentQueryString; The problem here is how to perform the 
		 *  secondary text filtering on the resulting table of data?
		 */
		String searchString = query.getCurrentQueryText();
		CacheLocation cacheLocation = 
				cache.searchForCounter(
						"Concept", 
						"SearchByText", 
						new String[] {searchString}
				);
		Long count = cacheLocation.getCounter();
		
		if (count == null) {
			if( 
					searchString==null 
					
					// Empty string is problematic if used in a search so we don't allow it 
					|| searchString.isEmpty()
	
					/* DEPRECATED TEST - we'll allow, helps user annotation
					 * We don't let people search against 
					 * production with less than 
					 * 3 characters... Too slow!
					 */
	//				|| searchString.trim().length()<3  
			) {
				count = 0L ;
				
			// Empty string is problematic if used in a search so we don't allow it 
			//} else if( searchString.isEmpty() ) {
			//	// a search on everything?
			//	count = conceptRepository.countAll();
				
			} else {
				count = this.countHitsByNameLike(searchString) ;
			}
		}
		
		cacheLocation.setCounter(count);
		
		return count;
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#countHitsByNameLike(java.lang.String)
	 */
	@Override
	@Deprecated
	public long countHitsByNameLike(String filter) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
	/* Specialized Concept Access Methods */
	
	/**
	 *  DEPRECATED
	 *
	 * Method to add a UMLS definition to a concept. Note: any  definition is returned
	 * @param source authority of the definition (e.g. UMLS SAB)
	 * @param id of the definition (i.e. UMLS Atom Unique Identifier)
	 * @param definition of the Concept indexed by the source and id
	 * @return previous definition if existing, or null
	 * /
	public Feature addDefinition( IdentifiedConcept concept, String source, String id, String definition ) {
		Feature existingDefinition = getDefinition( concept, source ) ;
		featureService.createFeature( concept, id, source, definition ) ;
		return existingDefinition ;
	}

	/**
	 * @param source Abbreviation of authority
	 * @return definition Feature of the Concept, associated with the SAB
	 * /
	public Feature getDefinition( IdentifiedConcept concept, String sab ) {
		List<Feature> features = featureService.findFeaturesByTagName( concept, sab ) ;
		if(features.isEmpty()) return null ;
		return features.get(0) ;
	}
	*/

	/*
	 *  Version deprecated in KB 3.0
	 *  
	 *  The UMLS Concept 'getCanonicalDescription()' may be called as a final
	 *  resort to access the canonical definition for the Concept
	 *  if there is no other available overriding source of such a 
	 *  definition (e.g. MyGene.Info, WikiData, OpenPHACTS, etc.)
	 *
	 * @return String of canonical definition retrieved
	 *
	
	public Feature getCanonicalDescription( Concept concept ) {
		Feature feature = 
				featureService.findFeatureByPrecedence( 
						concept,
						new String[]{
								"NCI_NCI-GLOSS",
								"NCI",
								"MSH",
								"CSP",
								"MDR"
						} 
		) ;
		return feature;
	}
	*/
	
	/**
	 * KB 3.0 version of function to retrieve "best" Concept description
	 * @param concept
	 * @return
	 */
	public String getCanonicalDescription( IdentifiedConcept concept ) {
		String description = "" ;
		return description;
	}

	/**
	 * @param cliqueId CURIE of the Concept to match 
	 * @parem list of beacons to search against (default: empty list triggers search against all known beacons)
	 * @return
	 */
	public AnnotatedConcept findByCliqueId( String cliqueId, List<String> beacons ) {
		
		String sessionId = query.getUserSessionId();

    	CompletableFuture<AnnotatedConcept> future = 
    			kbService.getConceptDetails(cliqueId,beacons,sessionId);
   
    	try {
    		
			AnnotatedConcept concept = future.get(
					
					// scale timeout by number of beacons
					KnowledgeBeaconService.BEACON_TIMEOUT_DURATION * 
					         kbService.getKnowledgeBeaconCount(beacons),
					         
					KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
			);
			return concept;
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_logger.error("cs.findByCliqueId() error: "+e.getMessage());
			return null;
		}
	}
	
	/* DEPRECATED?

	private IdentifiedConcept mergeConceptDetails(List<IdentifiedConcept> concepts) {
		
		/*
		 *  Blightly assuming that all the concepts records in
		 *  the list pertain to one clique. Identification
		 *  of the concept is likely also the clique id.
		 *  Semantic group assignment can be upgraded from
		 *  OBJC, if discovered.
		 *  
		 *  Need to perhaps merge aliases, synonyms and
		 *  most certainly, details?
		 *      /
		IdentifiedConcept merged = concepts.get(0);
		
		merged.setDescription(merged.getDescription());
		merged.setSynonyms(merged.getSynonyms());
		
		for(int i = 1;i<concepts.size();i++) {

			IdentifiedConcept additional = concepts.get(i);

			// Merge other definitions
			merged.setDescription(
					merged.getDescription()+
					" | "+additional.getDescription()
			);

			// Merge synonyms
			merged.setSynonyms(
					merged.getSynonyms()+
					" | "+additional.getSynonyms()
			);
			
			/* 
			 * More precise semantic type discovered?
			 * TODO: Need to generalize concepts 
			 * to handle multiple semantic types?
			 * /
			if( merged.getType().equals(ConceptType.OBJC) &&
				additional.getType() != null &&
				! additional.getType().equals(ConceptType.OBJC) 
			) merged.setType(additional.getType());
			
			// merge cross-references and terms 
			merged.getCrossReferences().addAll(additional.getCrossReferences());
			merged.getTerms().addAll(additional.getTerms());
			
			/*
			 *  Merge details... may have some duplication
			 *  in feature objects based on tags?
			 * /
			merged.getFeatures().addAll(additional.getFeatures());	
		}
		
		return merged;
	}
	*/

	/**
	 * @param CURIE concept identifier of the Concept to match across current session list of beacons
	 * 
	 * @return Concept found
	 */
	public IdentifiedConcept findByCliqueId( String cliqueId ) {
		List<String> beacons = query.getCustomBeacons();
		return findByCliqueId( cliqueId, beacons ) ;
		
	}
	
	/**
	 * 
	 * @param CURIE concept clique identifier to match across all known beacons
	 * 
	 * @return Concept returned with detailed annotation, if found
	 */
	public IdentifiedConcept searchAllBeacons( String cliqueId ) {
		return findByCliqueId( cliqueId, null ) ;
		
	}
	
	/**
	 * This method needs to search the Equivalent Concept Cliques 
	 * for a matching clique, then return the associated concept. 
	 * First iteration (flawed!), is to assume that the curie is 
	 * the clique id (probably patently false most of the time!
	 *  
	 * @param identifier CURIE to be resolved to a concept
	 * @return Optional<Concept> of matching concept
	 */
	public Optional<IdentifiedConcept> findByIdentifier( String identifier ) {
		
		String sessionId = query.getUserSessionId();

    	CompletableFuture<String> future = 
    			kbService.findByIdentifier(identifier,sessionId) ;
    	
    	IdentifiedConcept concept = null;
    	
    	try {
    		
			String cliqueId = future.get(
					KnowledgeBeaconService.BEACON_TIMEOUT_DURATION,
					KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
			);
			
			if(!cliqueId.isEmpty()) {
				// Need to retrieve actual Concept associated with clique here?
				concept = findByCliqueId(cliqueId);
			}
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_logger.warn("ConceptService.findByIdentifier(): matching concept "
					+ "could not be retrieved for identifier '"+ identifier
					+ "'. Exception: "+e.getMessage());
		}
    	
		if(concept == null) {
			return Optional.empty();
		} else {
			return Optional.of(concept) ;
		}
	}

	
	/**
	 * @param ConceptId
	 * @return Concept
	 */
	public Optional<IdentifiedConcept> getDetailsByCliqueId( String id ) {
		
		IdentifiedConcept concept = findByCliqueId(id) ;
		
		if(concept==null) {
			return Optional.empty();
		} else {
			return Optional.of(concept) ;
		}
	}

	/**
	 * 
	 * @param nameSpace
	 * @param resultSet
	 * @return
	 */
	public IdentifiedConcept processData( String nameSpace, ResultSet resultSet ) {
		return wikiDataService.createWikiDataItem(resultSet) ;
	}
	
	/*
	 * Method to retrieve remote data about a 
	 * node in a Qualified external namespace
	 */
	public IdentifiedConcept getQualifiedDataItem(String qualifiedId) {
		
		IdentifiedConcept dataItem = null ;
		
		String[] idPart  = qualifiedId.split("\\:") ;
		String nameSpace = idPart[0];
		String objectId  = idPart[1];
		
		//String cacheKey = "Concept" + "%" + nameSpace + "%" + objectId;
		//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());

		CacheLocation cacheLocation = 
				cache.searchForEntity( "Concept", nameSpace, new String[] {objectId} );
		
		// Is key present ? then fetch it from cache
		//Concept cachedResult = (Concept)cache.getEntityCache().get(cacheKey);
		IdentifiedConcept cachedResult = (IdentifiedConcept)cacheLocation.getEntity();
		
		if (cachedResult == null) {
			
			/*
			 *  Value not in the cache - attempt a query to the default
			 *  remote DataSource associated with the specified nameSpace?
			 */
			DataService ds = dataSourceRegistry.getDefaultDataService(nameSpace);
			
			// Can't resolve the nameSpace to a known DataService?
			if(ds==null) 
				throw new DataSourceException( 
						"ConceptService.getQualifiedDataItem() error: "
						+ "DataService not (yet) available for Data Qualifier NameSpace '"+nameSpace+"'?" );
				
			if( !ds.isSimple() ) 
				throw new DataSourceException( 
						"ConceptService.getQualifiedDataItem() error: "
						+ "simple data source expected?" );
	
			@SuppressWarnings("unchecked")
			SimpleDataService<String> sds = (SimpleDataService<String>)ds;
			
			CompletableFuture<ResultSet> futureMyGeneResultSet = sds.query(objectId);
			try {
				ResultSet resultSet = 
						futureMyGeneResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
	
				if ( resultSet != null && !resultSet.isEmpty() ) {
					/*
					 *  The manner in which the Concept result
					 *  is created, is dependent on the source nameSpace
					 *  of the object?
					 */
					dataItem = processData(nameSpace,resultSet) ;
					
					//cache.getEntityCache().put(cacheKey, dataItem);
					cacheLocation.setEntity(dataItem);
				}
				
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				futureMyGeneResultSet.completeExceptionally(e);
			}
			
		} else {
			dataItem = cachedResult ;
		}
		
		return dataItem ; // may be null if not available?
	}

	/**
	 * @param ConceptId
	 * @return Concept
	 * /
	public Optional<Concept> getDetailsByCliqueId( String id ) {
		
		// Try first to find this item in the local database(?)
		Concept concept = findByCliqueId(id);
		if(concept==null) {
			/* 
			 * This is a qualified URI identifying a local data item, 
			 * which must be resolved elsewhere (i.e. in WikiData?)
			 * Note that the system treats WikiData the same way as 'getDetails()'
			 * /
			try {
				concept = getQualifiedDataItem(id) ;
			} catch (DataSourceException e) {
				
			}
		}
		
		if(concept==null) {
			return Optional.empty();
		} else {
			return Optional.of(concept) ;
		}
	}
	*/
	
	/*
	 * Gene annotation by NCBI ("Entrez") Gene Id
	 */
	private void getGeneData( 
			Map<String, Object> args, 
			Function<ResultSet,Void> handler 
	)  throws Exception {
		
		// Gene annotation to be retrieved remotely by NCBI Gene Id from WikiData instead
		DataService ds = dataSourceRegistry.getDataService( 
							WikiDataDataSource.WIKIDATA_DATASOURCE_ID,
							WikiDataDataSource.WD_CDS_4_ID 
					) ;

		runComplexQuery( ds, args, handler );
	}

	/*
	 * Attempt to retrieve Concept details for non-Gene WikiData item?
	 */
	private void getWikiData( 
			Map<String, Object> args, 
			Function<ResultSet,Void> handler 
	)  throws Exception {
		
		DataService ds = dataSourceRegistry.getDataService( 
							WikiDataDataSource.WIKIDATA_DATASOURCE_ID,
							WikiDataDataSource.WD_CDS_5_ID 
					);
		
		runComplexQuery( ds, args, handler );
	}
	
	/*
	 * Retrieve the URL of the Wikipedia article describing a Concept 
	 */
	private void getWikiArticle( 
			String input, 
			Function<ResultSet,Void> handler 
	)  throws Exception {
		
		DataService ds = dataSourceRegistry.getDataService( 
							WikiDataDataSource.WIKIDATA_DATASOURCE_ID,
							WikiDataDataSource.WD_SDS_7_ID 
					);
		
		runSimpleQuery( ds, input, handler );
	}
	
	/**
	 * This method retrieves the description ('summary') 
	 * of the currently selected concept, from a suitable data service
	 * 
	 * @param cst
	 * @param handler
	 */
	public void getDescription( Function<ResultSet,Void> handler ) throws Exception {
		
		Optional<IdentifiedConcept> cscOpt = query.getCurrentSelectedConcept();
		
		if (!cscOpt.isPresent()) return;
		
		IdentifiedConcept concept = cscOpt.get();
		String name = concept.getName();
		
		ConceptType conceptType = concept.getType() ;
		
		// Ready just in case I have a gene...
		Object geneId = null ;
		
		// Indirectly ascertain that this UMLS Concept is a gene! 
		// Need to match any blank in front of 'gene' 
		// to avoid matching words with embedded 'gene', e.g. degeneration!
		int geneIdx = name.indexOf(" gene") ;
		if(geneIdx!=-1) {
			// prefix of the name, before ' gene' label, 
			// is assumed to be a gene symbol?
			name = name.substring(0, geneIdx).trim().toUpperCase() ;
			
			// NCBI ("Entrez") GeneId to be remotely
			// retrieved remotely from WikiData
			DataService ds = dataSourceRegistry.getDataService( 
							WikiDataDataSource.WIKIDATA_DATASOURCE_ID,  
							WikiDataDataSource.WD_SDS_1_ID 
					) ;
				
			if( !ds.isSimple() ) 
				throw new DataSourceException( 
						"ConceptService.getDescription() error: "+
						"SimpleDataSource expected?" );

			@SuppressWarnings("unchecked")
			SimpleDataService<String> sds = (SimpleDataService<String>)ds ;
			
			CompletableFuture<ResultSet> futureMyGeneResultSet = sds.query(name);
			try {
				ResultSet resultSet = futureMyGeneResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);

				if ( resultSet != null && !resultSet.isEmpty() ) {

					Result result = resultSet.get(0);

					geneId = result.get("entrezgene");
					if(geneId != null) conceptType = ConceptType.GENE ;
				}
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				futureMyGeneResultSet.completeExceptionally(e);
				throw e;
			}
		}
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		switch (conceptType) {
		
			case GENE:
			
				if(geneId == null) { // not already set above?
					geneId = concept.getId() ;
				}
				
				// otherwise, get the full gene annotation requested
				args.put("geneid", geneId);
				
				getGeneData( args, handler ) ;
				
				/*	Handler is now assumed to be
				 *  able to "add" additional meta-data
				 *  every time it is called, so I
				 *  fall through here to add the 
				 *  UMLS definition for a given 
				 *  gene concept, if available
				 */
				// break; 
	
			default:
				// TODO: Concept will likely be WikiData item 100% of the time in KB 3.0?
				if( concept.getId().startsWith("wd:") ) {
					
					// WikiData item (most of the time!) - try to retrieve details

					args.put("wikiDataId", concept.getId());
					getWikiData( args, handler ) ;
					
					// for article_uri, wikipedia link of concept
					getWikiArticle(concept.getId(), handler);
					
				} else {
					
					ResultSet resultSet = new SimpleResultSet() ;
					
					Result result = new SimpleResult() ;
					resultSet.add(result) ;
		
					String description = null;
					if(concept!=null)
						description = getCanonicalDescription( concept ) ;
					
					if(description!=null) 
						result.put("summary", description ) ;
					else {
						_logger.warn("Empty description for concept '"+concept.getName()+"'?");
						result.put("summary", "Unknown" ) ;
					}
					// Run the handler immediately since you are 
		
					// defaulting to the local concept description...
					handler.apply(resultSet);
				}
				
				break ;
		}
	}
	
	/**
	 * Method to return the details associated with currentCUI cached in the KBQuery context
	 * @param handler
	 * @throws Exception 
	 */
	public void getFullDetails( Function<ResultSet,Void> handler ) throws Exception {
		
		Optional<IdentifiedConcept> optCSC = query.getCurrentSelectedConcept();
		if (!optCSC.isPresent()) return;
		
		IdentifiedConcept concept = optCSC.get() ;
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		switch( concept.getType() ) {
		
			case GENE:
				// I assume that that associated Concept CUI 
				// is an Gene (formerly Entrez) id)
				// Details are to be remotely retrieved, 
				// e.g. hard coded DataService accessing MyGene.info? 
				args.put("geneid", concept.getId());
				
				getGeneData( args, handler ) ;
				
				break ;
				
			default:
				
				if( concept.getId().startsWith("wd:") ) {
					
					// WikiData item (most of the time!) - try to retrieve details

					args.put("wikiDataId", concept.getId());
					getWikiData( args, handler ) ;
					
				} else {
					
					// Some other kind of data - details may come from UMLS or be unknown?
					
					ResultSet resultSet = new SimpleResultSet() ;
					
					Result result = new SimpleResult() ;
					resultSet.add(result) ;
					
					String description = getCanonicalDescription( concept ) ;
					if(description != null) 
						result.put("description", description ) ;
					else {
						_logger.warn("Empty description for concept '"+concept.getName()+"'?");
						result.put("description", "Unknown" ) ;
					}
					// Run the handler immediately since you are 
					// defaulting to the local concept description...
					handler.apply(resultSet);
				}
				break ;
		}
		
		// reset the current selected concept
		query.setCurrentSelectedConcept(null);
	}

	/**
	 * @param concept
	 */
	@Deprecated
	public IdentifiedConcept save(IdentifiedConcept concept) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
    /**
     * 
     * @param predicate
     * @return
     */
    public IdentifiedConcept annotate( String id ) {

    	if(id.isEmpty()) {
    		_logger.warn(
    				"ConceptService.annotate() warning: cannot return "
    				+ "an annotated Concept without an Accession Id!?"
    		);
    		return null; 
    	}    
	    
    	// check first for a cached (presumed annotated) version of the Predicate
		String[] idPart  = id.split("\\:") ;
		String nameSpace = idPart[0];
		String objectId  = idPart[1];
		
		IdentifiedConcept concept = null;
		
		CacheLocation cacheLocation = 
				cache.searchForEntity( "Concept", nameSpace, new String[] {objectId} );
		
		IdentifiedConcept cachedConcept = (IdentifiedConcept) cacheLocation.getEntity();
		
		if (cachedConcept == null) {

			// Not cached... then first, attempt to retrieve it from the local database
			Optional<IdentifiedConcept> databaseConceptOpt = getDetailsByCliqueId(id);
			
			if(databaseConceptOpt.isPresent()) {
				concept = databaseConceptOpt.get();
			}
			
			/* 
			 * If the database concept is not found or 
			 * if the concept lacks a semantic group... 
			 * then consult wikidata?
			 */
			if(	
					concept==null ||
					concept.getType()==null ||
					concept.getType().equals(ConceptType.ANY)
			){
			
				// Assume that you need to retrieve the Concept description from WikiData
		    	DataService ds = dataSourceRegistry.getDataService( 
								WikiDataDataSource.WIKIDATA_DATASOURCE_ID,  
								WikiDataDataSource.WD_SDS_8_ID 
						) ;
		    	
				if( !ds.isSimple() ) 
					throw new DataSourceException( "ConceptService.annotate() error: SimpleDataSource expected?" );
		
				@SuppressWarnings("unchecked")
				SimpleDataService<String> sds = (SimpleDataService<String>)ds ;
				
				CompletableFuture<ResultSet> futureMyGeneResultSet = sds.query(id);
				try {
					ResultSet resultSet = 
							futureMyGeneResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
		
					if ( resultSet != null && !resultSet.isEmpty() ) {
						
						if(concept==null) {
							/* 
							 * If concept is not yet in the database, 
							 * we are better to create one now, albeit, 
							 * with a default SemanticGroup "Concepts & Ideas"
							 * and an empty name field 
							 */
							concept = createInstance( id, ConceptType.CONC, "" );
						}
						
						for(Result result:resultSet) {
		
							String nameLiteral = (String)result.get("name");
							String[] nameLiteralPart = nameLiteral.split("\\@");
							
							String valueUri = (String)result.get("value");
							String valueObjectId = RdfUtil.getObjectId(valueUri);
							
							String valueLabelLiteral = (String)result.get("valueLabel");
							String[] valueLabelPart = valueLabelLiteral.split("\\@");
							
							_logger.info(""+valueObjectId+":"+valueLabelPart[0]);
							
							// Set the name if empty...
							if(concept.getName().isEmpty()) concept.setName(nameLiteralPart[0]);
							
							// ...then attempt to discover the actual SemanticGroup
							ConceptType type = ConceptType.lookUpByWikiClass(valueObjectId);
							if(type!=null) {
								concept.setType(type);
								/* 
								 * mission accomplished? 
								 * Heuristic is to take first recognized 
								 * SemanticGroup... don't to look any further?
								 */
								break; 
							}
						}
						
						/*
						 * Save whatever concept with a newly 
						 * discovered name and semantic group 
						 */
						
						//TODO: June 8 2017
						//		We no longer have a ConceptRepository, we may want to
						//  	bring it back if we wish to save concepts like this.
//						concept = conceptRepository.save((Neo4jConcept) concept) ; 
					}
					
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					futureMyGeneResultSet.completeExceptionally(e);
					return null ; // Concept unknown in WikiData?
				}
			}
			
			/* ... then, cache the available non-null  
			 * databased Concept into the user's session, 
			 * annotated as best as it may be at this point(?)
			 */
			if(concept!=null) cacheLocation.setEntity(concept);
			
		} else {
			// Found a cached, presumed completely annotated version of the Predicate... reuse!
			concept = cachedConcept;
		}
		
    	return concept ;
    }

}
