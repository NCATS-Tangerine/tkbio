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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.PredicateRepository;
import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.neo4j.Neo4jPredicate;
import bio.knowledge.service.Cache.CacheLocation;

/**
 * @author Richard
 *
 */
@Service
public class PredicateService {
	
	private Logger _logger = LoggerFactory.getLogger(PredicateService.class);

	@Autowired
	private Cache cache;
	
	@Autowired
	private DataSourceRegistry dataSourceRegistry ;
	
    @Autowired
	private PredicateRepository predicateRepository ;
	
    /**
     * 
     * @param name
     * @return
     */
    public Predicate findPredicateByName(String name) {
    	// Normalize to upper case
    	name = name.toUpperCase() ;
    	return predicateRepository.findPredicateByName(name) ;
    }
    
    /**
     * 
     * @return
     */
    public List<Predicate> findAllPredicates() {
    	return (List<Predicate>) (List) predicateRepository.findAllPredicates() ;
    }
    
    /**
     * 
     * @param predicate
     * @return
     */
    public Predicate annotate(Predicate predicate) {
    	
    	String accId = predicate.getAccessionId();
    	
    	// NOP - can't do any better here without an accession id
    	if(accId.isEmpty()) {
    		_logger.warn("PredicateService.annotate() warning: cannot annotate a Predicate without an Accession Id!?");
    		return predicate; 
    	}    
	    
    	// check first for a cached (presumed annotated) version of the Predicate
		String[] idPart  = accId.split("\\:") ;
		String nameSpace = idPart[0];
		String objectId  = idPart[1];
		
		CacheLocation cacheLocation = 
				cache.searchForEntity( "Predicate", nameSpace, new String[] {objectId} );
		
		Predicate cachedPredicate = (Predicate) cacheLocation.getEntity();
		
		if (cachedPredicate == null) {
			
			// Not cached... then first, attempt to retrieve it from the local database
			Predicate databasePredicate = 
					predicateRepository.findPredicateByAccessionId(accId);
			
			if( databasePredicate == null ) {
				// Predicate is not yet even registered in the database
				// Maybe create it although, at this point, its name may be missing?
				databasePredicate = predicate;
				
				// Perform an initial save of the new 
				// Predicate in the local database
				// just in case it already has a name set
				databasePredicate = predicateRepository.save((Neo4jPredicate) databasePredicate) ;
			}
			
			String dbPredicateName = databasePredicate.getName();
			if( dbPredicateName.isEmpty() ) {
			
				// Assume that you need to retrieve the Predicate label from WikiData
		    	DataService ds = dataSourceRegistry.getDataService( 
								WikiDataDataSource.WIKIDATA_DATASOURCE_ID,  
								WikiDataDataSource.WD_SDS_6_ID 
						) ;
		    	
				if( !ds.isSimple() ) 
					throw new DataSourceException( 
							"ConceptService.getDescription() error: "+
							"SimpleDataSource expected?" );
		
				@SuppressWarnings("unchecked")
				SimpleDataService<String> sds = (SimpleDataService<String>)ds ;
				
				CompletableFuture<ResultSet> futureMyGeneResultSet = 
						sds.query(predicate.getAccessionId());
				try {
					ResultSet resultSet = 
							futureMyGeneResultSet.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
		
					if ( resultSet != null && !resultSet.isEmpty() ) {
						
						Result result = resultSet.get(0);
						
						// TODO need to figure out how to get WikiData 
						// to return a Predicate description too?
						String literal = (String)result.get("name");
						String[] literalPart = literal.split("\\@");
						databasePredicate.setName(literalPart[0]);
						
						// merge re-save the annotated Predicate in the local database?
						databasePredicate = predicateRepository.save((Neo4jPredicate) databasePredicate) ;
					}
					
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					futureMyGeneResultSet.completeExceptionally(e);
					// NOP - the current databasePredicate is sent back unaltered?
				}
			}
			
			// ... then, cache the available databased Predicate into the user's session, 
			//     annotated as best as it may be at this point(?)
			cacheLocation.setEntity(databasePredicate);
			
			//... then return it to the caller!
			predicate = databasePredicate ;
			
		} else {
			// Found a cached, presumed completely annotated version of the Predicate... reuse!
			predicate = cachedPredicate;
		}
		
    	return predicate ;
    }
}
