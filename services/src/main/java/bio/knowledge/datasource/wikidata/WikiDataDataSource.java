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

package bio.knowledge.datasource.wikidata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import bio.knowledge.datasource.AbstractComplexDataService;
import bio.knowledge.datasource.AbstractDataSource;
import bio.knowledge.datasource.AbstractDescribed;
import bio.knowledge.datasource.AbstractSimpleDataService;
import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataSource;
import bio.knowledge.datasource.rdf.SPARQLDataService;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;

/**
 * @author Richard
 *
 */
@Component
public class WikiDataDataSource extends AbstractDataSource implements WikiDataServicesDefinitions {
	
	private static Logger _logger = LoggerFactory.getLogger(WikiDataDataSource.class);

	public static final String WIKIDATA_DATASOURCE_ID   = "WikiData";
	public static final String WIKIDATA_DATASOURCE_NAME = "WikiData Data Source";

	/**
	 * 
	 */
	public WikiDataDataSource() {
		super( WIKIDATA_DATASOURCE_ID,WIKIDATA_DATASOURCE_NAME ) ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.Described#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return WIKIDATA_DATASOURCE_ID;
	}

	@Override
	public String getName() {
		return WIKIDATA_DATASOURCE_NAME ;
	}

	private Map<String, String> metaData = new HashMap<String, String>();
	
	static {
		// initialize metaData here?
	}

	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.Described#describe()
	 */
	@Override
	public Map<String, String> describe() {
		return new HashMap<String,String>(metaData);
	}
	
	/*
	 * Shared service attributes and methods
	 */
	interface BaseDataService extends DataService {
		
		default void addGeneralMetaData( AbstractDescribed self) {
			final String author = "Authors: ";
			self.addMetaData(author, "Richard Bruskiewich");
		}
		
		default ResultSet runQuery( 
				SPARQLDataService sparqlService, 
				Map<String,Object> parameters 
		) {
			ResultSet resultSet = new SimpleResultSet();
			try {
				sparqlService.setupQuery(parameters);
	
				sparqlService.execSelect();
	
				List<Map<String,String>> results = 
						sparqlService.getResultList() ;
				
				for( Map<String,String> item : results ) {
					Result result = new SimpleResult() ;
					result.putAll(item);
					resultSet.add(result) ;
				}
			} catch (Exception e) {
				_logger.error(e.getMessage());
			} finally {
				if(sparqlService != null) {
					sparqlService.close() ;
				}
			}
			return resultSet ;
		}
	}
	
	// Wrapper for implementing WikiData SimpleDataService
	class BaseSimpleDataService 
		extends AbstractSimpleDataService<String>
		implements BaseDataService {
		
		private SPARQLDataService sparqlService = null ;
		

		/**
		 * 
		 * @param dataSource
		 * @param serviceId
		 * @param targetSemanticGroup
		 * @param name
		 * @param sparqlQuery
		 */
		protected BaseSimpleDataService(  
				DataSource dataSource, 
				String serviceId,
				SemanticGroup targetSemanticGroup,
				String name,
				String sparqlQuery
		) {
			super( dataSource, serviceId, targetSemanticGroup, name );
			
			addGeneralMetaData(this) ;
			
			//addMetaData(required, q);
			//addMetaData(url, api);
			
			sparqlService = new SPARQLDataService(WIKIDATA_SPARQL_ENDPOINT,sparqlQuery) ;
		}

		/**
		 * 
		 * @param query
		 * @return
		 */
		public ResultSet runQuery( String input ) {
			
			Map<String,Object> parameters = new HashMap<String,Object>() ;
			parameters.put("input", input) ;
			
			return runQuery( sparqlService, parameters ) ;
		}

		
		/*
		 * (non-Javadoc)
		 * @see bio.knowledge.datasource.SimpleDataService#query(java.lang.Object)
		 */
		@Override
		public CompletableFuture<ResultSet> query(String queryString) throws IllegalArgumentException {
			CompletableFuture<ResultSet> future = 
					CompletableFuture.supplyAsync( () -> runQuery(queryString) ) ;
			return future;
		}
	}
	
	
	// Wrapper for implementing WikiData ComplexDataService
	class BaseComplexDataService 
		extends AbstractComplexDataService
		implements BaseDataService  {
		
		private SPARQLDataService sparqlService = null ;

		/**
		 * 
		 * @param dataSource
		 * @param serviceId
		 * @param targetSemanticGroup
		 * @param name
		 * @param sparqlQuery
		 */
		protected BaseComplexDataService( 
				DataSource dataSource, 
				String serviceId,
				SemanticGroup targetSemanticGroup, 
				String name,
				String sparqlQuery
		) {
			super( dataSource, serviceId, targetSemanticGroup, name );
			
			addGeneralMetaData(this) ;
			
			//addDescription() ;
			//addParameters() ;
			//addQualifiers() ;
			
			sparqlService = new SPARQLDataService(WIKIDATA_SPARQL_ENDPOINT,sparqlQuery) ;
		}

		@Override
		public CompletableFuture<ResultSet> query( Map<String, Object> parameters ) throws IllegalArgumentException {
			CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync( ()->runQuery( sparqlService, parameters ) );
			return future;
		}		
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.AbstractDataSource#initialize()
	 */
	@Override
	protected void initialize() {
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_1_ID, 
					SemanticGroup.GENE, 
					WD_SDS_1_NAME, 
					WD_SDS_1_SPARQL_QUERY
					 
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_2_ID, 
					SemanticGroup.ANY, 
					WD_SDS_2_NAME, 
					WD_SDS_2_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_3_ID, 
					SemanticGroup.GENE, 
					WD_SDS_3_NAME, 
					WD_SDS_3_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_3_COUNTING_ID, 
					SemanticGroup.GENE, 
					WD_SDS_3_COUNTING_NAME, 
					WD_SDS_3_COUNTING_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_4_ID, 
					SemanticGroup.ANY, 
					WD_SDS_4_NAME, 
					WD_SDS_4_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_5_ID, 
					SemanticGroup.ANY, 
					WD_SDS_5_NAME, 
					WD_SDS_5_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_6_ID, 
					SemanticGroup.ANY, 
					WD_SDS_6_NAME, 
					WD_SDS_6_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_7_ID, 
					SemanticGroup.ANY, 
					WD_SDS_7_NAME, 
					WD_SDS_7_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseSimpleDataService(  
					this, 
					WD_SDS_8_ID, 
					SemanticGroup.ANY, 
					WD_SDS_8_NAME, 
					WD_SDS_8_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseComplexDataService(  
					this, 
					WD_CDS_1_ID, 
					SemanticGroup.ANY, 
					WD_CDS_1_NAME, 
					WD_CDS_1_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseComplexDataService(  
					this, 
					WD_CDS_3_ID, 
					SemanticGroup.GENE, 
					WD_CDS_3_NAME, 
					WD_CDS_3_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseComplexDataService(  
					this, 
					WD_CDS_4_ID, 
					SemanticGroup.GENE, 
					WD_CDS_4_NAME, 
					WD_CDS_4_SPARQL_QUERY
				) 
			);
		addDataService( 
				new BaseComplexDataService(  
					this, 
					WD_CDS_5_ID, 
					SemanticGroup.ANY, 
					WD_CDS_5_NAME, 
					WD_CDS_5_SPARQL_QUERY
				) 
			);
	}

}
