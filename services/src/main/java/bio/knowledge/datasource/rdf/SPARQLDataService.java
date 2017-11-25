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

package bio.knowledge.datasource.rdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.model.RdfUtil;

public class SPARQLDataService {
	
	private static Logger _logger = LoggerFactory.getLogger(SPARQLDataService.class);

	public final static String NAME       = "name" ;
	public final static String DEFINITION = "definition" ;		
	
	private String SPARQLEndpoint = "" ;
	private String queryTemplate    = "" ;

	private Model model = null ;
	
	/**
	 * Constructor
	 * @throws IOException 
	 *
	 */
	public SPARQLDataService( Model model, String queryString ) {
		this.model = model ;
		this.queryTemplate = queryString ;
	}
	
	/**
	 * Constructor
	 * @throws IOException 
	 *
	 */
	public SPARQLDataService( String endpoint, String queryString ) {
		if(endpoint == null || endpoint.isEmpty())
			throw new DataSourceException("Null or empty SPARQLDataService endpoint?") ;
		this.SPARQLEndpoint = endpoint ;
		this.queryTemplate    = queryString ;
	}
	
	private String language_code ;
	private String language_name ;

	/**
	 * 
	 * Method
	 *
	 * @param language
	 */
	private void setLanguage(String language)
		throws DataSourceException {
		
		if(language == null) {
			
			language_code = Language.getDefaultLanguageCode();
			language_name = Language.getDefaultLanguage();
			
		} else {
			
			language_code = language.trim() ;
			language_name = Language.getLanguage(language_code) ;
			if(language_name == null) {
				throw new DataSourceException("SPARQLDataService.setLanguage(): language code '"+
										  language_code+"' is unknown?") ;
			}
		}
	}
	
	protected String getLanguageCode() 
			throws DataSourceException {
		if(language_code == null) {
			setLanguage(null) ;
		}
		return language_code ;
	}
	
	protected String getLanguageName()  
			throws DataSourceException {
		if(language_code == null) {
			setLanguage(null) ;
		}
		return language_name ;
	}

	public void setQueryTemplate(String queryTemplate) {
		this.queryTemplate = queryTemplate ;
	}
	
	private String getQueryString(Map<String,Object> parameters) {
		String queryString = queryTemplate ;
		for(String key : parameters.keySet()) {
			
			Object query = parameters.get(key) ;
			
			_logger.debug("Replacing '?"+key+"' with '"+query+"' in queryTemplate") ;

			String parameter = RdfUtil.encodeResource(query) ;

			if(!parameter.isEmpty())
				queryString = queryString.replaceAll("\\?"+key, parameter ) ;
		}
		
		_logger.debug("getQueryString(): returns:\n"+queryString ) ;
		
		return queryString ;
	}
	

	private String getQueryString( String language_code ) {
		// not sure how to use the language_code here...
		return queryTemplate ;
	}
	
	private Query query = null ;
	private QueryExecution qe = null;

	/**
	 * 
	 */
	public void setupQuery( Map<String,Object> parameters )  {
		try {
			
			create(getQueryString(parameters)) ;

		} catch (DataSourceException e) {
			// Fails non-fatally here...
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param language
	 */
	public void defaultQueryInLanguage(String language)  {
		try {
			
			setLanguage(language);
			
			create(getQueryString(language_code)) ;
			
		} catch (DataSourceException e) {
			// Fails non-fatally here...
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Method
	 */
	public void defaultQuery() {
		this.defaultQueryInLanguage(null); 
	}

	/**
	 * 
	 * Method
	 *
	 * @param queryTemplate
	 */
	public void create(String queryString) {
		
		query = QueryFactory.create(queryString);
		
		if(model != null) {
			qe = QueryExecutionFactory.create(query,model);
		} else {
			qe = 
				QueryExecutionFactory.sparqlService(
						SPARQLEndpoint,
						query
					);
		}
	}
	
	protected ResultSet results = null ;
	
	public ResultSet getResults() {
		return results ;
	}
	
	/**
	 * 
	 * Method
	 *
	 */
	public void execSelect() {
		if(qe != null) { 
			results = qe.execSelect();
		}
	}
	
	/**
	 * 
	 * Method
	 *
	 */
	public Model execDescribe() {
		if(qe != null) { 
			return qe.execDescribe();
		} else {
			return null ;
		}
	}
	
	/**
	 * 
	 * Method
	 *
	 */
	public Model execConstruct() {
		if(qe != null) { 
			return qe.execConstruct();
		} else {
			return null ;
		}
	}
	
	/**
	 * 
	 * Method
	 *
	 */
	public Boolean execAsk() {
		if(qe != null) { 
			return qe.execAsk();
		} else {
			return false ;
		}
			
	}
	
	public void close() {
		if(qe != null) { qe.close(); }
	}

	protected String getNode(QuerySolution soln, String tag) {
		String nodeValue = null ;
		if(soln != null) {
			RDFNode node = soln.get(tag) ;
			if( node != null) {
			    if ( node.isLiteral() ) {
			    	nodeValue = ((Literal)node).getLexicalForm() ;
			    	_logger.trace( "\t"+tag+": "+nodeValue) ; 
			    } else if ( node.isResource() ) {
			    	Resource res = (Resource)node;
			    	if ( res.isAnon() == true ) {
			    		nodeValue = res.getLocalName() ;
			    		_logger.trace(  "\t"+tag+": <" + nodeValue + ">"); 
			    	} else {
			    		nodeValue = res.getURI() ;
			    		_logger.trace( "\t"+tag+": <" + nodeValue + ">"); 
			    	}
			    }
			}
		}
		return nodeValue ;
	}

	
	protected Map<String,Map<String,String>> resultMap = null ;
	
	protected void getNodes(QuerySolution soln, Map<String,String> nodeMap) {
		
		String name = getNode(soln, NAME) ;
		if(name != null) { nodeMap.put(NAME,name); }
		
		String definition = getNode(soln, DEFINITION) ;
		if(definition != null) { nodeMap.put(DEFINITION,definition); }
		
	}
	
	public Map<String,Map<String,String>> getResultMap() {
		if(resultMap != null) {
			return resultMap ;
		} else {
			resultMap = new HashMap<String,Map<String,String>>() ;
		}
		
		if (results != null) {
			while ( results.hasNext() ) {
				QuerySolution soln = results.nextSolution() ;
				Resource res = (Resource)(soln.get("resource"));
				String resName = res.getURI() ;
				resultMap.put(resName, new HashMap<String,String>()) ;
				
				_logger.trace( " - <" + resName + "> : " ); 
				
				this.getNodes(soln, resultMap.get(resName));    
	        }
		}
		return resultMap ;
	}
	
	public List<Map<String,String>> getResultList() {
		
		List<Map<String,String>> resultList = 
				new ArrayList<Map<String,String>>() ;
		
		if (results != null) {
			List<String> variables = results.getResultVars() ;
			while ( results.hasNext() ) {
				
				QuerySolution soln = results.nextSolution() ;
				Map<String,String> result = new HashMap<String,String>() ;
				
				for(String variable : variables ) {
					RDFNode value = soln.get(variable);
					result.put(variable, value.toString()) ;
				}
				resultList.add(result) ;
	        }
		}
		return resultList ;
	}
	
	/**
	 * Dumps current results to System.out
	 */
	public void dumpResults() {
		Map<String,Map<String,String>> results = getResultMap();
		for(String key : results.keySet()) {
			System.out.println(key+":"); 
			Map<String,String> annotation = results.get(key) ;
			for(String tag : annotation.keySet()) {
				System.out.printf( "\t%20s: %-255s\n",tag,annotation.get(tag)); 
			}
			System.out.println() ;
		}
	}

}
