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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataServiceUtility;
import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.datasource.GetConceptDataService;
import bio.knowledge.datasource.GetConceptDataService.ConceptImpl;
import bio.knowledge.datasource.KnowledgeSource;
import bio.knowledge.datasource.KnowledgeSourcePool;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.Concept;
import bio.knowledge.model.RdfUtil;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.core.FeatureService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.wikidata.WikiDataService;

/**
 * @author Richard
 *
 */
@Service
public class ConceptService 
	extends IdentifiedEntityServiceImpl<Concept>
	implements DataServiceUtility {
	
	private Logger _logger = LoggerFactory.getLogger(ConceptService.class);
	
	private static final String SEPARATOR=" ";
	
	@Autowired
	private KBQuery query ;
	
	@Autowired
	private Cache cache;
	
	@Autowired
	private DataSourceRegistry dataSourceRegistry ;
	
	@Autowired
	WikiDataService wikiDataService ;
	
	@Autowired
	private FeatureService featureService ;

    @Autowired
	private ConceptRepository conceptRepository ;
    
    @Autowired
    private AuthenticationState authenticationState;

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.lang.Object[])
	 */
	
	public Neo4jConcept createInstance(Object... args) {
		if (args.length == 2)
			if (args[0] instanceof SemanticGroup) {
				return new Neo4jConcept(
						(SemanticGroup)   args[0], // SemanticGroup
						(String)          args[1]  // Concept.name
				);
			} else
				throw new RuntimeException("Invalid 1st argument to ConceptService.createInstance() ?");
		
		else if (args.length == 3)
			if (args[1] instanceof SemanticGroup) {
				return new Neo4jConcept(
						(String)        args[0], // Concept.accessionId
						(SemanticGroup) args[1], // SemanticGroup
						(String)        args[2]  // Concept.name
				);
			} else
				throw new RuntimeException("Invalid 2nd argument to ConceptService.createInstance() ?");
		else
			throw new RuntimeException("Invalid number of ConceptService.createInstance() arguments?");
	}

    private List<Concept> Concepts = new ArrayList<Concept>() ;
    
	//@Autowired
	//private GraphDatabaseService graphDb;
    
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
    
    /* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<Concept> getIdentifiers() {
		return getConcepts();
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.springframework.data.domain.Pageable)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Concept> getIdentifiers(Pageable pageable) {
		return (Page<Concept>)(Page)conceptRepository.findAll(pageable);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Concept> findByNameLike(String filter, Pageable pageable) {
		_logger.trace("Inside ConceptService.findByNameLike()");
		return findAllFiltered(filter,pageable);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	// TODO: I think this is where the refactoring faltered
	private Page<Concept> findAllFiltered(String filter, Pageable pageable) {
		KnowledgeSource ks1 = new KnowledgeSource("http://localhost:8080/api/");
		KnowledgeSource ks2 = new KnowledgeSource("broken link");
		KnowledgeSourcePool pool = new KnowledgeSourcePool();
		pool.add(ks1);
		pool.add(ks2);
		GetConceptDataService service = new GetConceptDataService(pool);
		CompletableFuture<List<ConceptImpl>> future = service.query(
				filter,
				null,
				pageable.getPageNumber(),
				pageable.getPageSize()
		);
		
		try {
			List<ConceptImpl> concepts = future.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
			return (Page<Concept>) (Page) new PageImpl(concepts);
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			future.completeExceptionally(e);
		}
		
		return (Page<Concept>) (Page) new PageImpl(new ArrayList<Concept>());
		
		
//		String searchString = query.getCurrentQueryText();
//		if ( 
//				searchString == null 
//				
//				// Empty filter is problematic if used in a search so we don't allow it 
//				|| searchString.isEmpty() 
//
//				/* DEPRECATED - we'll allow (helps user annotation discovery...)
//				 * We don't let people search against 
//				 * production with less than 
//				 * 3 characters... Too slow!
//				 */
////				|| searchString.trim().length()<3  
//			) {
//			return null;
//		} else {
//			searchString = filter + SEPARATOR + searchString;
//		}
//
//		// getting selected semanticSemanticGroups filter after initial concept seach 
//		Optional<Set<SemanticGroup>> initialConceptTypesOpt = query.getInitialConceptTypes();
//		ArrayList<String> conceptTypes = new ArrayList<String>();
//		String conceptCodes = new String();
//		if (initialConceptTypesOpt.isPresent()) {
//			Set<SemanticGroup> initialConceptTypes = initialConceptTypesOpt.get();
//			for (SemanticGroup type : initialConceptTypes) {
//				conceptTypes.add(type.name());
//				// appending all concept types for making cache key
//				conceptCodes = conceptCodes + type.name();
//			}
//		}
//		
//		String pageKey = new Integer(pageable.hashCode()).toString();
//		CacheLocation cacheLocation = 
//				cache.searchForResultSet(
//						"Concept", 
//						searchString, 
//						new String[] { searchString, conceptCodes, pageKey }
//				);
//		
//		List<Neo4jConcept> searchedConceptResult = new ArrayList<>();
//		// Is key present ? then fetch it from cache
//		// List<Concept> cachedResult = (List<Concept>) cache.getResultSetCache().get(cacheKey);
//		
//		List<Neo4jConcept> cachedResult = (List<Neo4jConcept>) cacheLocation.getResultSet();
//		
//		// Is key present ? then fetch it from cache
//		if (cachedResult == null) {
//			String[] words = searchString.split(SEPARATOR);
//			if(words.length==0) {
//				searchedConceptResult = conceptRepository.findAllByPage(
//						pageable,
//						authenticationState.getUserId(),
//						authenticationState.getGroupIds()
//				);
//			} else {
//				if(filter.trim().isEmpty() && !initialConceptTypesOpt.isPresent()){
//					searchedConceptResult = conceptRepository.findByInitialSearch(
//							words,
//							pageable,
//							authenticationState.getUserId(),
//							authenticationState.getGroupIds()
//					);
//				} else {
//					searchedConceptResult = conceptRepository.findByNameLikeIgnoreCase(
//							conceptTypes,
//							words,
//							pageable,
//							authenticationState.getUserId(),
//							authenticationState.getGroupIds()
//					);
//				}
//			}
//			
//			//cache.getResultSetCache().put(cacheKey, searchedConceptResult);
//			cacheLocation.setResultSet(searchedConceptResult);
//			
//		} else {
//			searchedConceptResult = cachedResult;
//		}
//		return new PageImpl(searchedConceptResult);
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	/* (non-Javadoc)
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Concept> findAll(Pageable pageable) {
		_logger.trace("Inside ConceptService.findAll()");
		/*
		 *  "findAll()" for the initial concept search is not really "findAll" of all concepts
		 *  but rather, a search on the currently active concept, thus I'll constrain the search
		 *  to the currently known currentQueryString; The problem here is how to perform the 
		 *  secondary text filtering on the resulting table of data?
		 */
		return findAllFiltered("",pageable);
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
	public long countHitsByNameLike(String filter) {
		if(
				filter==null 
				
				// Empty filter is problematic if used in a search so we don't allow it 
				|| filter.isEmpty() 
		) {
			return 0L;
		}
		// Slight redundancy here that cannot be avoided
		// Same cache check as countEntries() above
		// Cache may be tested in countEntries() before this call
		CacheLocation cacheLocation = 
				cache.searchForCounter(
						"Concept", 
						"SearchByText", 
						new String[] {filter}
				);
		Long count = cacheLocation.getCounter();
		
		if (count == null) {
			count = conceptRepository.countByNameLikeIgnoreCase(filter);
		}
		
		cacheLocation.setCounter(count);
		
		return count;
	}
	
	/* Specialized Concept Access Methods */
	
	/**
	 * Method to add a UMLS definition to a concept. Note: any  definition is returned
	 * @param source authority of the definition (e.g. UMLS SAB)
	 * @param id of the definition (i.e. UMLS Atom Unique Identifier)
	 * @param definition of the Concept indexed by the source and id
	 * @return previous definition if existing, or null
	 */
	public Feature addDefinition( Concept concept, String source, String id, String definition ) {
		Feature existingDefinition = getDefinition( concept, source ) ;
		featureService.createFeature( concept, id, source, definition ) ;
		return existingDefinition ;
	}

	/**
	 * @param source Abbreviation of authority
	 * @return definition Feature of the Concept, associated with the SAB
	 */
	public Feature getDefinition( Concept concept, String sab ) {
		List<Feature> features = featureService.findFeaturesByTagName( concept, sab ) ;
		if(features.isEmpty()) return null ;
		return features.get(0) ;
	}

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
	public String getCanonicalDescription( Concept concept ) {
		String description = "" ;
		return description;
	}

	/**
	 * @param cui of the Concept to match
	 * @return
	 */
	public Concept findByAccessionId(String accessionId) {
		return conceptRepository.findByAccessionId(accessionId);
	}
	
	/**
	 * @param ConceptId
	 * @return Concept
	 */
	public Optional<Concept> getDetailsByConceptAccessionId(String accessionId) {
		
		Concept concept = conceptRepository.findByAccessionId(accessionId) ;
		
		/*  // deprecated complexity!
		if(RdfUtil.getQualifier(conceptId).isEmpty()) {
			/* 
			 * No qualifier on CST Id: assume that this is a 
			 * SemMedDb recorded Concept or Concept 
			 * for look up in main database?
			 * /
			if(conceptId.startsWith("C")) {
				// this is a root concept node CUI?
				concept = conceptService.findByCui(conceptId) ;
			} else {
				concept = ConceptRepository.findByConceptId(conceptId);
			}
		} else {
			/* 
			 * This is a qualified URI identifying a non-SemMedDb node, 
			 * which must be resolved elsewhere (i.e. in WikiData?)
			 * Note that the system treats WikiData the same way as 'getDetails()'
			 * /
			concept = getQualifiedDataItem(conceptId) ;
		}
		*/
		
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
	public Concept processData( String nameSpace, ResultSet resultSet ) {
		return wikiDataService.createWikiDataItem(resultSet) ;
	}
	
	/*
	 * Method to retrieve remote data about a 
	 * node in a Qualified external namespace
	 */
	private Concept getQualifiedDataItem(String qualifiedId) {
		
		Concept dataItem = null ;
		
		String[] idPart  = qualifiedId.split("\\:") ;
		String nameSpace = idPart[0];
		String objectId  = idPart[1];
		
		//String cacheKey = "Concept" + "%" + nameSpace + "%" + objectId;
		//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());

		CacheLocation cacheLocation = 
				cache.searchForEntity( "Concept", nameSpace, new String[] {objectId} );
		
		// Is key present ? then fetch it from cache
		//Concept cachedResult = (Concept)cache.getEntityCache().get(cacheKey);
		Concept cachedResult = (Concept)cacheLocation.getEntity();
		
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
	 */
	public Optional<Concept> getDetailsByAccessionId(String accessionId) {
		
		// Try first to find this item in the local database(?)
		Concept concept = conceptRepository.findByAccessionId(accessionId);
		if(concept==null) {
			/* 
			 * This is a qualified URI identifying a local data item, 
			 * which must be resolved elsewhere (i.e. in WikiData?)
			 * Note that the system treats WikiData the same way as 'getDetails()'
			 */
			concept = getQualifiedDataItem(accessionId) ;
		}
		
		if(concept==null) {
			return Optional.empty();
		} else {
			return Optional.of(concept) ;
		}
	}

	
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
		
		Optional<Concept> cscOpt = query.getCurrentSelectedConcept();
		if (!cscOpt.isPresent()) return;
		
		Concept concept = cscOpt.get();
		String name = concept.getName();
		
		SemanticGroup conceptType = concept.getSemanticGroup() ;
		
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
					if(geneId != null) conceptType = SemanticGroup.GENE ;
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
					geneId = concept.getAccessionId() ;
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
				if( concept.getAccessionId().startsWith("wd:") ) {
					
					// WikiData item (most of the time!) - try to retrieve details

					args.put("wikiDataId", concept.getAccessionId());
					getWikiData( args, handler ) ;
					
					// for article_uri, wikipedia link of concept
					getWikiArticle(concept.getAccessionId(), handler);
					
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
		
		Optional<Concept> optCSC = query.getCurrentSelectedConcept();
		if (!optCSC.isPresent()) return;
		
		Concept concept = optCSC.get() ;
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		switch( concept.getSemanticGroup() ) {
		
			case GENE:
				// I assume that that associated Concept CUI 
				// is an Gene (formerly Entrez) id)
				// Details are to be remotely retrieved, 
				// e.g. hard coded DataService accessing MyGene.info? 
				args.put("geneid", concept.getAccessionId());
				
				getGeneData( args, handler ) ;
				
				break ;
				
			default:
				
				if( concept.getAccessionId().startsWith("wd:") ) {
					
					// WikiData item (most of the time!) - try to retrieve details

					args.put("wikiDataId", concept.getAccessionId());
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
	public Concept save(Concept concept) {
		return conceptRepository.save((Neo4jConcept) concept) ;
	}
	
    /**
     * 
     * @param predicate
     * @return
     */
    public Concept annotate(String accessionId) {

    	if(accessionId.isEmpty()) {
    		_logger.warn(
    				"ConceptService.annotate() warning: cannot return "
    				+ "an annotated Concept without an Accession Id!?"
    		);
    		return null; 
    	}    
	    
    	// check first for a cached (presumed annotated) version of the Predicate
		String[] idPart  = accessionId.split("\\:") ;
		String nameSpace = idPart[0];
		String objectId  = idPart[1];
		
		Concept concept = null;
		
		CacheLocation cacheLocation = 
				cache.searchForEntity( "Concept", nameSpace, new String[] {objectId} );
		
		Concept cachedConcept = (Concept) cacheLocation.getEntity();
		
		if (cachedConcept == null) {
			
			// Not cached... then first, attempt to retrieve it from the local database
			Optional<Concept> databaseConceptOpt = 
						getDetailsByConceptAccessionId(accessionId);
			
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
					concept.getSemanticGroup()==null ||
					concept.getSemanticGroup().equals(SemanticGroup.ANY)
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
				
				CompletableFuture<ResultSet> futureMyGeneResultSet = sds.query(accessionId);
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
							concept = createInstance( accessionId, SemanticGroup.CONC, "" );
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
							SemanticGroup group = SemanticGroup.lookUpByWikiClass(valueObjectId);
							if(group!=null) {
								concept.setSemanticGroup(group);
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
						concept = conceptRepository.save((Neo4jConcept) concept) ; 
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
