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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bio.knowledge.datasource.ComplexDataService;
import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataServiceUtility;
import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.Concept;
import bio.knowledge.model.RdfUtil;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Statement;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;
import bio.knowledge.model.neo4j.Neo4jPredicate;
import bio.knowledge.model.wikidata.WikiDataPropertySemanticType;
import bio.knowledge.service.Cache.CacheLocation;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.service.core.IdentifiedEntityServiceImpl;
import bio.knowledge.service.core.TableSorter;

/**
 * StatementService evolved from KB2 PredicationService
 * 
 * @author Richard - initial SemMedDb functionality; Wikidata statement data
 * @author Chandan Mishra - significant elaboration of SemMedDb queries, caching, etc.
 */
@Service
public class StatementService 
	extends IdentifiedEntityServiceImpl<Statement>
	implements DataServiceUtility {

	private Logger _logger = LoggerFactory.getLogger(StatementService.class);

	@Autowired
	private KBQuery query;

	@Autowired
	private Cache cache;
	
	@Autowired
	private KnowledgeBeaconService kbService;
	
	@Override
	public List<Statement> getDataPage(
			int pageIndex, 
			int pageSize, 
			String filter, 
			TableSorter sorter, 
			boolean isAscending
	) {

		/**
		 * We are not using the {@code filter} field, since here it refers to the main keywords search text
		 */
		String extraFilter   = query.getSimpleTextFilter();
		String sourceClique  = query.getCurrentQueryConceptId();
		
		// TODO: implement the targetClique specification in TKBIO
		String targetClique  = "";
		
		// Sometimes this function gets call without any conceptId's (e.g. during table refresh)
		if(sourceClique==null) return new ArrayList<Statement>();
		
		Optional<Set<SemanticGroup>> optionalSemanticGroupSet = query.getConceptTypes();
		
		String semgroups = "";
		
		if (optionalSemanticGroupSet.isPresent()) {
			Set<SemanticGroup> semanticGroupSet = optionalSemanticGroupSet.get();
			for (SemanticGroup semanticGroup : semanticGroupSet) {
				semgroups += semanticGroup.name() + " ";
			}
			semgroups = semgroups.trim();
		}
		
		Optional<Predicate> optionalPredicateFilter = query.getPredicateFilterValue();
		
		Predicate predicateFilter = null;
		
		if (optionalPredicateFilter.isPresent()) {
			predicateFilter = optionalPredicateFilter.get();
		}

		List<String> beacons = query.getCustomBeacons();
		String sessionId = query.getUserSessionId();
		
		CompletableFuture<List<Statement>> future = 
				kbService.getStatements( sourceClique, predicateFilter, targetClique, extraFilter, semgroups, pageIndex, pageSize,beacons,sessionId);
		
		try {
			List<Statement> statements = 
					future.get(
							kbService.weightedTimeout(pageSize), 
							KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
			);
			
			return statements;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return new ArrayList<Statement>();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.IdentifiedEntityService#createInstance(java.
	 * lang.Object[])
	 */
	public Neo4jGeneralStatement createInstance(Object... args) {

		if (args.length == 1)
			return new Neo4jGeneralStatement(
					(String) args[0]  		  // Statement AccessionId
			);
		else if (args.length == 2)
			if (args[1] instanceof Neo4jPredicate) {
				return new Neo4jGeneralStatement(
						(String) args[0],   // Statement AccessionId
						(Neo4jPredicate) args[1] // Predicate by object
				);
			} else if (args[1] instanceof String) {
				return new Neo4jGeneralStatement(
						(String) args[0],   // Statement AccessionId
						(Neo4jPredicate) args[1] // Predicate by object
				);
			} else
				throw new RuntimeException("Invalid argument to StatementService.createInstance() ?");
		
		else if (args.length == 4)
			return new Neo4jGeneralStatement(
					(String) args[0],  		  // Statement AccessionId
					(Concept) args[1],        // Subject
					(Neo4jPredicate) args[2],      // Predicate
					(Concept) args[3]         // Object
			);
		else
			throw new RuntimeException("Invalid StatementService.createInstance() arguments?");
	}

	private List<Statement> statementList = new ArrayList<Statement>();

	private Stream<Statement> getStatementStream() {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	public List<Statement> getStatements() {
		if (statementList.isEmpty()) {
			statementList = getStatementStream().sorted().collect(toList());
		}
		return statementList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers()
	 */
	@Override
	public List<Statement> getIdentifiers() {
		return getStatements();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.IdentifiedEntityService#getIdentifiers(org.
	 * springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Statement> getIdentifiers(Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.IdentifiedEntityServiceImpl#findByNameLike(
	 * java.lang.String, org.springframework.data.domain.Pageable)
	 */
	private Page<Statement> findByFilter(String filter, Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	@Override
	public Page<Statement> findByNameLike(String filter, Pageable pageable) {
		switch (query.getRelationSearchMode()) {
			case PMID:
				return findByPMID(filter, pageable);
			case RELATIONS:
				return findByFilter(filter, pageable);
			case WIKIDATA:
				return findWikiDataByFilter(filter, pageable);
			default:
				throw new DataServiceException("StatementService.findByNameLike(): Invalid RelationSearchMode()?");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	@SuppressWarnings({})
	public Page<Statement> findAll(Pageable pageable) {
		switch (query.getRelationSearchMode()) {
			case PMID:
				return findByPMID("", pageable);
			case RELATIONS:
				return findByFilter("", pageable);
			case WIKIDATA:
				return findWikiDataByFilter("", pageable);
			default:
				throw new DataServiceException("StatementService.findAll(): Invalid RelationSearchMode()?");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.IdentifiedEntityServiceImpl#countEntries()
	 */
	@Override
	public long countEntries() {
		switch (query.getRelationSearchMode()) {
			case PMID:
				return countByPMID("");
			case RELATIONS:
				return countHelper("");
			case WIKIDATA:
				return countByWikiData("");
			default:
				throw new DataServiceException("StatementService.countEntries(): Invalid RelationSearchMode()?");
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.service.core.IdentifiedEntityServiceImpl#
	 * countHitsByNameLike(java.lang.String)
	 */
	@Override
	public long countHitsByNameLike(String filter) {
		switch (query.getRelationSearchMode()) {
			case PMID:
				return countByPMID(filter);
			case RELATIONS:
				return countHelper(filter);
			case WIKIDATA:
				return countByWikiData(filter);
			default:
				throw new DataServiceException("SentenceService.countHitsByNameLike(): Invalid RelationSearchMode()?");
		} 
	}
	
	@Deprecated 
	public Statement findbySourceAndTargetId(String sourceId, String targetId, String relationName){
		throw new NotImplementedException("This method no longer makes sense within the context of distributed knowledge sources");
	}

	/**
	 * Helper method for countEntries and countHitsByNameLike
	 * 
	 * @param filter
	 * @return
	 */
	private long countHelper(String filter) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	/***
	 * Helper for PMID based find by.
	 * @param filter
	 * @param pageable
	 * @return
	 */
	private Page<Statement> findByPMID(String filter, Pageable pageable) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
	/**
	 * Helper method for countEntries and countHitsByNameLike based on PMID
	 * 
	 * @param filter
	 * @return
	 */
	private long countByPMID(String filter) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}

	/*
	 * Observations from Knowledge.Bio Release 1.0
	 * 
	 * SemMedDb statement_argument entries should normally be found in pairs, 
	 * of subject and object concepts; However, for whatever reason, this cardinality 
	 * is not always honored: the so-called PGAP2 anomaly (KB2.1 Issue #44)
	 * 
	 * Here, we attempt to compensate for this issue by comparing 
	 * the currently selected "seed" concept against the list of items.
	 * 
	 * The heuristic algorithm is:
	 * 
	 * 1) if the given Statement doesn't have the required (subject or object) item, then send back null
	 * 2) if the current query "seed" concept is NOT set, then just send back the first item in the list
	 * 3) if the current query "seed" concept IS set, then if it is in the given list of (subject or object) items, then send it back
	 * 4) otherwise, (again) just send back the first item in the list
	 * 
	 * This will often work fine if there is a unique subject or object asserted 
	 * for the currently selected "seed" concept; otherwise, it may fail to return the expected item.
	 * 
	 */
	/**
	 * @return
	 */
	public Concept getCanonicalSubject(Neo4jGeneralStatement p) {
		
		List<Concept> subjects = p.getSubjects() ;
		
		// might trigger a NPE in caller?
		if( subjects==null || subjects.size()==0 ) return null ; 
		
		Optional<Concept> currentConcept = query.getCurrentQueryConcept();
		if (!currentConcept.isPresent()) return subjects.get(0) ;

		// else, heuristic?
		if( subjects.contains(currentConcept) ) 
			return currentConcept.get() ;
		else
			return subjects.get(0) ;
	}

	/**
	 * @param p 
	 * @return
	 */
	public Concept getCanonicalObject(Neo4jGeneralStatement p) {
		
		List<Concept> objects = p.getObjects() ;
		
		// might trigger a NPE in caller?
		if( objects==null || objects.size()==0 ) return null ; 
		
		Optional<Concept> currentConceptOpt = query.getCurrentQueryConcept();
		if (!currentConceptOpt.isPresent()) return objects.get(0) ;

		// else, heuristic?
		if( objects.contains(currentConceptOpt) ) 
			return currentConceptOpt.get() ;
		else
			return objects.get(0) ;
	}

	@Autowired
	private DataSourceRegistry dataSourceRegistry ;
	
	private void runQuery(
			String serviceName,
			Concept concept, 
			Function<? super ResultSet, ? extends Void> resultHandler 
	) {
		DataService dataService = 
				dataSourceRegistry.getDataService( 
						WikiDataDataSource.WIKIDATA_DATASOURCE_ID, 
						serviceName 
				) ;
		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = (SimpleDataService<String>) dataService ;
		CompletableFuture< ResultSet > futureResult = sds.query(concept.getName()) ;
		
		processResults(futureResult,resultHandler) ;
	}
	
	// simple runQuery with paging of results
	private void runQuery(
			String serviceName,
			Concept concept, String filter, Pageable pageable, 
			Function<? super ResultSet, ? extends Void> resultHandler 
	) {
		DataService dataService = 
				dataSourceRegistry.getDataService( 
						WikiDataDataSource.WIKIDATA_DATASOURCE_ID, 
						serviceName 
				) ;

		ComplexDataService cds = (ComplexDataService) dataService ;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put( "input",  concept.getName() );
		parameters.put( "filter", filter );
		parameters.put( "limit",  pageable.getPageSize() );     
		parameters.put( "offset", pageable.getOffset() );  

		CompletableFuture< ResultSet > futureResult = cds.query(parameters) ;
		
		processResults(futureResult,resultHandler) ;

	}
	
	private Void loadWikiDataResults( 
			ResultSet rs, 
			Concept subject, 
			List<Statement> statements 
	) {
		rs.stream().forEach(r->{
			
			_logger.debug("loadWikiDataResults() item: "+r.toString());
			
			try {
				// Not sure how efficient it is here to create afresh...
				// TODO: Need perhaps to retrieve a 
				// properly recorded Predicate from the database?
				String propUri = (String) r.get("prop") ;
				String propLabel = (String) r.get("propLabel") ;
				String[] plPart = propLabel.split("\\@") ; // ignore language?
				String propDescription = (String) r.get("propDescription") ;
				String[] pdPart = propDescription.split("\\@") ; // ignore language?

				String propId ;
				
				propId = RdfUtil.getObjectId(propUri) ;
				
				Neo4jPredicate property = new Neo4jPredicate( propUri, plPart[0], pdPart[0] ) ;
				String propValue = (String) r.get("propValue") ;
								
				String statementId = subject.getId()+"-"+propId+"-"+propValue ;
				Neo4jGeneralStatement p = new Neo4jGeneralStatement( 
						statementId, // not yet sure what unique id to put here...
			    		property 
			    ) ;
				
				// Subject is the focus of attention!
				p.setSubject(subject);
			
				// Object extracted from 'propValue' query field
				WikiDataPropertySemanticType wikiDataType = 
						WikiDataPropertySemanticType.lookUpByPropertyId(propId);

				String propValueId = RdfUtil.getObjectId(propValue) ;
				String qualifiedPropValueId = wikiDataType.getDefaultQualifier()+propValueId ;

				Optional<Class<? extends Neo4jConcept>> nodeTypeOpt = 
						wikiDataType.getNodeType() ;
				
				Neo4jConcept wikiItem ;
				if(nodeTypeOpt.isPresent()) {
					Class<? extends Neo4jConcept> nodeType = nodeTypeOpt.get() ;
					wikiItem = nodeType.newInstance() ;
					wikiItem.setName(propValueId);
				} else
					wikiItem = 
						new Neo4jConcept( 
								qualifiedPropValueId,
								SemanticGroup.PHEN,
								propValueId 
						) ;
				
				wikiItem.setId(qualifiedPropValueId);
				
				// Important: non-SemMedDb, WikiData or other CST's must have
				// a XML namespace qualifier prefix added to their identifiers
				// which are, by default, the propValue
				// TODO: What about propValues that are already full URI's?
				wikiItem.setId(qualifiedPropValueId);
				
				p.addObject(wikiItem);
				
				statements.add(p) ;
				
			} catch(Exception e) {
				_logger.error("... parsing error: "+e.getMessage());
			}
		});
		return (Void)null ;
	}
	
	private Void countWikiDataResults( ResultSet rs, Long[] count ) {
		Result result = rs.get(0);
		if(result.containsKey("count")) {
			String countLiteral = (String) result.get("count") ;
			String[] parts = countLiteral.split("\\^\\^") ;
			try {
				count[0] = Long.parseLong(parts[0]) ;
			} catch(Exception e) {
				// some kind of error ? return zero
				_logger.error("countResults() error: "+e.getMessage());
				count[0] = 0L ;
			}
		} else
			count[0] = 0L ;
		return (Void)null ;
	}
	
	private Concept getCurrentConcept() {
		Optional<Concept> selectedConceptOpt = query.getCurrentSelectedConcept();
		if (!selectedConceptOpt.isPresent()) return null;
		return selectedConceptOpt.get();
	}	
	
	@SuppressWarnings({ "unchecked" })
	private Page<Statement> findWikiDataByFilter( String filter, Pageable pageable ) {

		List<Statement> statements = new ArrayList<>();

		Concept concept = getCurrentConcept();

		if(concept!=null) {

			String accessionId = concept.getId();
			
			// this is key used for caching purpose,("WikiData" + conceptId + textFilter + pageable)
			//String cacheKey = ("WikiData" + "#" + accessionId + "#" + filter + "#" + pageable.hashCode());
			//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());
			
			String pageKey = new Integer(pageable.hashCode()).toString();
			CacheLocation cacheLocation = 
					cache.searchForResultSet("WikiData", accessionId, new String[] { filter, pageKey });
			
			// Is key present ? then fetch it from cache
			//List<Statement> cachedResult = (List<Statement>) cache.getResultSetCache().get(cacheKey);
			List<Statement> cachedResult = (List<Statement>)cacheLocation.getResultSet();
			
			if (cachedResult == null) {

				// Go afresh to WikiData and load properties into Statements

				switch( concept.getSemanticGroup() ) {

				case GENE:
					// Retrieve Paged WikiData properties for the Gene Name == Gene Symbol?
					final List<Statement> newStatements = new ArrayList<>();
					runQuery( 
							WikiDataDataSource.WD_CDS_3_ID, 
							(Neo4jConcept) concept,
							filter,
							pageable,
							(rs)->loadWikiDataResults(rs, (Concept) concept,newStatements) 
							);
					statements = newStatements ;
					break ;

				default:
					// do nothing for now - return empty Statement List
					break ;
				}

				// putting fetched result to cache
				//cache.getResultSetCache().put(cacheKey, statements);
				cacheLocation.setResultSet(statements);
				
				_logger.trace("Fetched from database");
				
			} else {
				
				_logger.trace("Fetched from cached data");
				statements = cachedResult;
			} 
		} else // no concept currently selected?
			statements = new ArrayList<>(); // send back empty Statement List...

		return new PageImpl<Statement>(statements, pageable, statements.size());
	}

	/**
	 * @param filter String that WikiData property names should match
	 * @return
	 */
	private long countByWikiData(String filter) {
		
		Concept concept = getCurrentConcept() ;
		
		// Access WikiData here and count properties matched by filter
		
		Long conceptId = concept.getDbId();
		
		// creating cache key using ("WikiData" + conceptId + textFilter)
		//String cacheKey = ("WikiData" + "#" + conceptId + "#" + filter);
		//cacheKey = Base64.getEncoder().encodeToString(cacheKey.getBytes());
		CacheLocation cacheLocation = 
				cache.searchForCounter(
						"Statement", 
						conceptId.toString(), 
						new String[] { filter }
				);
		
		// Is key present ? then fetch it from cache
		Long[] count = new Long[1] ;
		//count[0] = cache.getCountCache().get(cacheKey);
		count[0] = cacheLocation.getCounter();
		
		if(count[0] == null) {
			
			count[0] = 0L ;
			
			switch( concept.getSemanticGroup() ) {
			
				case GENE:
					// Count WikiData properties for the Gene Name == Gene Symbol?
					runQuery( 
							WikiDataDataSource.WD_SDS_3_COUNTING_ID, 
							concept, 
							(rs)->countWikiDataResults(rs,count) 
					);
					break ;
					
				default:
					// do nothing for now - return empty count
					break ;
			}
		}
		
		// put fetched result to map before returning
		//cache.getCountCache().put(cacheKey, count[0]);
		cacheLocation.setCounter(count[0]);
		
		return count[0];
	}

	/**
	 * @param statement
	 * @return
	 */
	public Neo4jGeneralStatement save(Neo4jGeneralStatement statement) {
		throw new NotImplementedException("Removed all reference to neo4j");
	}
	
}
