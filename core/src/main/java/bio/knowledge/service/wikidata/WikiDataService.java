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

package bio.knowledge.service.wikidata;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataServiceUtility;
import bio.knowledge.datasource.DataSourceRegistry;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.datasource.wikidata.ConceptDescriptor;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.neo4j.Neo4jConcept;

/**
 * @author Richard
 *
 */
@Service
public class WikiDataService
	implements DataServiceUtility {
	

	@Autowired
	private DataSourceRegistry dataSourceRegistry ;

	private void runQuery(
			String serviceName,
			String query, 
			Function<? super ResultSet, ? extends Void> resultHandler 
	) {
		DataService dataService = 
				dataSourceRegistry.getDataService( 
						WikiDataDataSource.WIKIDATA_DATASOURCE_ID, 
						serviceName 
				) ;
		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = (SimpleDataService<String>) dataService ;
		CompletableFuture< ResultSet > futureResult = sds.query(query) ;
		
		processResults(futureResult,resultHandler) ;
	}

	private Void getUrl( ResultSet rs, String[] url ) {
		Result result = rs.get(0);
		if(result.containsKey("url")) {
			url[0] = (String) result.get("url") ;
		} else
			url[0] = "" ;
		return (Void)null ;
	}
	
	private static Map<String,String> formattedUrlCache = new HashMap<String,String>() ;
	
	public String getFormattedUrl( String property, String[] parameters ) {
		
		_logger.debug("Entering getFormattedUrl("+property+")...");
		
		String retrievedUrl = null ; 
		if(formattedUrlCache.containsKey(property)) {
			retrievedUrl = formattedUrlCache.get(property);
			_logger.debug("retrieved locally cached url '"+retrievedUrl+"'!");			
			
		} else {
			
			/*
			 *  Hacky patch for Gene Atlas (would be better to 
			 *  update WikiData with a formattedUrl property?
			 */
			if(property.equals("wd:P692")) {
				retrievedUrl = "http://commons.wikimedia.org/wiki/Special:FilePath/$1";
				
			} else {
					
				// Check if current property resolves directly to a WikiData Entity
				ConceptDescriptor descriptor = 
						ConceptDescriptor.lookUpById(property);
				
				// My heuristic for this is the resource qualifier
				if(descriptor.isWikiDatum()) {
					retrievedUrl = "http://www.wikidata.org/entity/$1" ;
				} else {
					// retrieve by WikiData SPARQL query
					// TODO: check if this works for regular WikiData items too (probably not?)
					String[] url = new String[1] ;
					runQuery( 
							WikiDataDataSource.WD_SDS_4_ID, 
							property, 
							(rs)->getUrl(rs,url) 
					);
					retrievedUrl = url[0] ;
				}
			}
			
			if(!(retrievedUrl==null || retrievedUrl.isEmpty())) {
				// Cache for the future
				formattedUrlCache.put(property,retrievedUrl);
			}
		}
		
		// customize using current parameters
		if(!(retrievedUrl==null || retrievedUrl.isEmpty())) {
			
			_logger.debug("getFormattedUrl(property:"+property+") returned '"+retrievedUrl+"'");
			for(int i=0;i<parameters.length;i++) {
				// substitute $# URL parameters here?
				String target = "\\$"+(new Integer(i+1).toString()) ;
				_logger.debug("\tReplacing '"+target+"' with '"+parameters[i]+"'");
				retrievedUrl = retrievedUrl.replaceAll(target, parameters[i]) ;
			}
			
			_logger.debug("remotely retrieved url '"+retrievedUrl+"'!");
		} else {
			_logger.debug("url unknown?");
		}
		
		// return the full URL (or null)
		return retrievedUrl ;
    }
	
	public Neo4jConcept createWikiDataItem(ResultSet resultSet) {
		dumpResults(resultSet) ;
		return new Neo4jConcept("wd:testconcept",SemanticGroup.PHEN,"dummyConcept") ;
	}
}
