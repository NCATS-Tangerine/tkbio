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

package bio.knowledge.datasource.gene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import bio.knowledge.datasource.AbstractComplexDataService;
import bio.knowledge.datasource.AbstractDataSource;
import bio.knowledge.datasource.AbstractDescribed;
import bio.knowledge.datasource.AbstractSimpleDataService;
import bio.knowledge.datasource.DataService;
import bio.knowledge.datasource.DataSource;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;

/**
 * @author Richard
 */
@Component
public class MyGeneInfoDataSource extends AbstractDataSource {
	
	private Logger _logger = LoggerFactory.getLogger(MyGeneInfoDataSource.class);

	public static final String MYGENE_INFO_DATASOURCE = "MyGeneInfoDataSource";
	
	public static final String MGI_SDS_1_ID = "MyGeneInfo Simple Data Service 1" ;
	public static final String MGI_SDS_1_NAME = "MyGeneInfo: first simple query by gene "
												+ "symbol string to retrieve gene identifiers" ;
	
	public static final String MGI_SDS_2_ID = "MyGeneInfo Simple Data Service 2" ;
	public static final String MGI_SDS_2_NAME = "MyGeneInfo: second simple query by gene "
												+ "symbol string to retrieve gene identifiers" ;
	
	public static final String MGI_CDS_1_ID = "MyGeneInfo Complex Data Service 1" ;
	public static final String MGI_CDS_1_NAME = "MyGeneInfo: first complex query by "
												+ "multiple input parameters to "
												+ "retrieve gene annotation" ;
	
	public static final String MGI_CDS_2_ID = "MyGeneInfo Complex Data Service 2" ;
	public static final String MGI_CDS_2_NAME = "MyGeneInfo: second complex query by "
												+ "multiple input parameters to "
												+ "retrieve gene annotation" ;
	/**
	 * 
	 */
	public MyGeneInfoDataSource() {
		super(MYGENE_INFO_DATASOURCE) ;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see bio.knowledge.datasource.Described#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return MYGENE_INFO_DATASOURCE;
	}
	
	/**
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	protected String readAll(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	/*
	 * Shared service attributes and methods
	 */
	interface BaseDataService extends DataService {

		static final String queryUrlPrefix = "http://mygene.info/v2/query?q=";
		static final String geneAnnotationUrlPrefix = "http://mygene.info/v2/gene/";
		
		default void addGeneralMetaData( AbstractDescribed self) {
			final String author = "Authors: ";
			self.addMetaData(author, "Spencer Joel, Ryan Stoppler & Richard Bruskiewich");
		}
	}
	
	// Wrapper for implementing MyGeneInfo SimpleDataService
	class BaseSimpleDataService 
		extends AbstractSimpleDataService<String>
		implements BaseDataService {
		
		private final String required = "Required: ";
		private final String q = "Gene Symbol";
		private final String url = "URL: ";
		private final String api = "http://mygene.info/v2/api/#MyGene.info-gene-query-service-GET-Gene-query-service";

		private Boolean summaryOnly ;
		
		/**
		 * 
		 * @param dataSource
		 * @param serviceId
		 * @param name
		 * @param summaryOnly
		 */
		protected BaseSimpleDataService(  
				DataSource dataSource, 
				String serviceId, 
				String name,
				Boolean summaryOnly
		) {
			super( dataSource, serviceId, ConceptType.GENE, name );
			
			addGeneralMetaData(this) ;
			
			addMetaData(required, q);
			addMetaData(url, api);
			
			this.summaryOnly = summaryOnly ;
		}

		private static final String queryUrlSuffix = "&species=human&limit=1";

		private JSONObject jsonObj = null;

		public JSONObject sendQuery( String query ) {
			
			// Remember to URL encode spaces in names!
			String queryString = "" ;
			query = query.trim().replaceAll("\\s", "+");
			if (!query.isEmpty())
				queryString = queryUrlPrefix + query + queryUrlSuffix;
			
			BufferedReader in = null;
			try {
				URL myURL = new URL(queryString);
				
				_logger.trace(queryString);
				
				URLConnection yc = myURL.openConnection();
				in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String temp = readAll(in);
				jsonObj = new JSONObject(temp);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			return jsonObj;
		}

		public ResultSet secondQuery( JSONObject data ) {
			BufferedReader in = null;
			String geneID = null;
			ResultSet resultSet = new SimpleResultSet();
			Result result = new SimpleResult();
			JSONObject jsonObj;

			JSONArray holder = data.getJSONArray("hits");
			jsonObj = (JSONObject) holder.get(0);
			geneID = jsonObj.get("entrezgene").toString();

			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				result.put(key, jsonObj.get(key));
			}

			try {
				if (geneID == null) {
					throw new Exception("entrezegene is null");
				}
				URL myURL;
				if (this.summaryOnly) {
					myURL = new URL(geneAnnotationUrlPrefix + geneID + "?fields=summary");
				} else {
					myURL = new URL(geneAnnotationUrlPrefix + geneID );
				}
				URLConnection yc = myURL.openConnection();
				in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

				String temp = readAll(in);
				jsonObj = new JSONObject(temp);

				in.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

			if (jsonObj.has("summary")) {
				String value = jsonObj.getString("summary");
				result.put("summary", value);
			}
			resultSet.add(result);

			return resultSet;
		}

		/*
		 * (non-Javadoc)
		 * @see bio.knowledge.datasource.SimpleDataService#query(java.lang.Object)
		 */
		@Override
		public CompletableFuture<ResultSet> query(String geneSymbol) throws IllegalArgumentException {
			CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync( () -> sendQuery(geneSymbol) )
					.thenApplyAsync( jsonObj -> secondQuery(jsonObj) );
			return future;
		}
		
	}
	
	// Wrapper for implementing MyGeneInfo ComplexDataService
	class BaseComplexDataService 
		extends AbstractComplexDataService
		implements BaseDataService  {
		
		// True when Complex query has a geneId as input
		private Boolean geneQuery ;
		
		/**
		 * 
		 * @param dataSource
		 * @param serviceId
		 * @param name
		 * @param geneQuery
		 */
		protected BaseComplexDataService( 
				DataSource dataSource, 
				String serviceId, 
				String name,
				Boolean geneQuery
		) {
			super( dataSource, serviceId, ConceptType.GENE, name );
			this.geneQuery = geneQuery ;
			
			addGeneralMetaData(this) ;
			
			addDescription() ;
			addParameters() ;
			addQualifiers() ;
		}

		// Metadata Constants set globally
		private static final String required = "Required Parameter: ";
		private static final String optional = "Optional Parameter: ";
		private static final String url = "URL: ";
				
		private static final String geneQueryApi = "http://mygene.info/v2/api/#MyGene.info-gene-query-service-GET-Gene-query-service";

		private static final String q = "query string. Examples: \"CDK2\", \"NM_052827\", \"204639_at\". "
				+ "The detailed query syntax can be found at http://docs.mygene.info/en/latest/doc/query_service.html";

		private static final String geneAnnotationApi = "http://mygene.info/v2/api/#MyGene.info-gene-annotation-services-GET-Gene-annotation-service";

		private static final String geneId = "GeneID: NCBI or Ensembl Gene identifier, e,g., 1017, ENSG00000170248. A retired Entrez Gene "
				+ "id works too if it is replaced by a new one, e.g., 245794";
		
		private static final String fields = "Fields: a comma-separated fields to limit the fields returned from the matching gene "
				+ "hits. The supported field names can be found from any gene object (e.g. http://mygene.info/v2/gene/1017)."
				+ " Note that it supports dot notation as well, e.g., you can pass 'refseq.rna'. If \"fields=all\", all available"
				+ " fields will be returned. Default: \"symbol,name,taxid,entrezgene,ensemblgene\".";
		
		private static final String callback = "you can pass a \"callback\" parameter to make a JSONP call.";
		
		private static final String filter = "Filter: alias for \"fields\" parameter.";
		
		private static final String dotField = "Dotfield: control the format of the returned fields when passed \"fields\" parameter contains "
				+ "dot notation, e.g. \"fields=refseq.rna\". If \"true\" or \"1\", the returned data object contains a single "
				+ "\"refseq.rna\" field, otherwise (\"false\" or \"0\"), a single \"refseq\" field with a sub-field of \"rna\". Default: true.";
		
		private static final String email = "Email Address: If you are regular users of our services, we encourage you to provide us an email, "
				+ "so that we can better track the usage or follow up with you.";
		
		private static final String species = "can be used to limit the gene hits from given species. You "
				+ "can use \"common names\" for nine common species (human, mouse, rat, fruitfly, nematode, "
				+ "zebrafish, thale-cress, frog and pig). All other species, you can provide their taxonomy "
				+ "ids. Multiple species can be passed using comma as a separator. Passing Ã¢â‚¬Å“allÃ¢â‚¬ï¿½ will query "
				+ "against all available species. Default: human,mouse,rat.";
		
		private static final String size = "the maximum number of matching gene hits to return (with a cap of 1000 at"
				+ " the moment). Default: 10.";
		
		private static final String from = "the number of matching gene hits to skip, starting from 0. Combining with"
				+ " \"size\" parameter, this can be useful for paging. Default: 0.";
		
		private static final String sort = "the comma-separated fields to sort on. Prefix with \"-\" for descending "
				+ "order, otherwise in ascending order. Default: sort by matching scores in decending order.";
		
		private static final String facets = "a single field or comma-separated fields to return facets, "
				+ "for example, \"facets=taxid\", \"facets=taxid,type_of_gene\".";
		
		private static final String speciesFacetFilter = "relevant when faceting on species (i.e., \"facets=taxid\" are passed). "
				+ "It's used to pass species filter without changing the scope of faceting, so that the "
				+ "returned facet counts won't change. Either species name or taxonomy id can be used, "
				+ "just like the \"species\" parameter above.";
		
		private static final String entrezOnly = "when passed as \"true\" or \"1\", the query returns only the hits"
				+ " with valid NCBI Gene Identifiers. Default: false.";
		
		private static final String ensemblOnly = "when passed as \"true\" or \"1\", the query returns only the hits "
				+ "with valid Ensembl gene ids. Default: false.";

		private static final String limit = "alias for \"size\" parameter.";
		
		private static final String skip = "alias for \"from\" parameter.";

		private void addDescription() {
			
			// Optional fields shared between two complex api types
			addMetaData(optional + "fields", fields);
			addMetaData(optional + "callback", callback);
			addMetaData(optional + "filter", filter);
			addMetaData(optional + "dotfield", dotField);
			addMetaData(optional + "email", email);
			
			if(geneQuery) {
				addMetaData(required + "q", q);
				addMetaData(url + "url", geneQueryApi);
				
				// Gene query has several extra optional fields
				addMetaData(optional + "species", species);
				addMetaData(optional + "size", size);
				addMetaData(optional + "from", from);
				addMetaData(optional + "sort", sort);
				addMetaData(optional + "facets", facets);
				addMetaData(optional + "speciesFacetFilter", speciesFacetFilter);
				addMetaData(optional + "entrezonly", entrezOnly);
				addMetaData(optional + "ensemblonly", ensemblOnly);
				addMetaData(optional + "limit", limit);
				addMetaData(optional + "skip", skip);
				
			} else { // gene annotation
				addMetaData(required + "geneid", geneId);
				addMetaData(url + "url", geneAnnotationApi );
			} 
		}
		
		private void addQualifiers() {
			// qualifiers for both modes of complex query
			addQualifier("fields", String.class);
			addQualifier("callback", Integer.class);
			addQualifier("filter", Integer.class);
			addQualifier("dotfield", String.class);
			addQualifier("email", String.class);
			
			// extra qualifiers for "full" gene query mode
			if(geneQuery) {
				addQualifier("species", String.class);
				addQualifier("size", Integer.class);
				addQualifier("from", Integer.class);
				addQualifier("sort", String.class);
				addQualifier("facets", String.class);
				addQualifier("species_facet_filter", String.class);
				addQualifier("entrezonly", String.class);
				addQualifier("ensemblonly", String.class);
				addQualifier("limit", Integer.class);
				addQualifier("skip", Integer.class);
			}
		}

		private void addParameters() {
			if(geneQuery)
				addParameter("q", String.class);
			else // gene annotation
				addParameter("geneid", String.class);
		}

		public ResultSet sendQuery( Map<String, Object> parameters ) {
			
			String queryString = "" ;
			ResultSet resultSet = new SimpleResultSet();
			Result result = new SimpleResult();
			
			if (parameters.containsKey("q")) {
				// Remember to URL encode spaces in names!
				queryString = (String)parameters.get("q") ;
				queryString = queryString.trim().replaceAll("\\s", "+");
				queryString = queryUrlPrefix + queryString;
				if (parameters.size() > 1) {
					for (Map.Entry<String, Object> entry : parameters.entrySet()) {
						if (!entry.getKey().equals("q")) {
							String value = entry.getValue().toString();
							queryString += "&" + entry.getKey() + "=" + value;
						}
					}
				}

			} else if (parameters.containsKey("geneid")) {
				queryString = geneAnnotationUrlPrefix + parameters.get("geneid");
				if (parameters.size() > 1) {
					int count = 0;
					for (Map.Entry<String, Object> entry : parameters.entrySet()) {
						// annotation service does not have & before first modifier
						// like the gene query service does, had to add in a
						// condition to handle this
						// i.e. mygene.info/v2/gene/7486?fields=all&callback=jsoncallback
						if (!entry.getKey().equals("geneid")) {
							if (count == 0) {
								String value = entry.getValue().toString();
								queryString += "?" + entry.getKey() + "=" + value;
								count++;
							} else {
								String value = (String) entry.getValue();
								queryString += "&" + entry.getKey() + "=" + value;
							}
						}
					}
				}
			} else return resultSet ; // empty set returned?
			
			BufferedReader in = null;
			JSONObject jsonObj;

			try {
				URL myURL = new URL( queryString );
				
				_logger.trace( queryString );
				
				URLConnection yc = myURL.openConnection();
				in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String temp = readAll(in);
				if (temp.startsWith("json")) {
					temp = temp.substring(temp.indexOf("(") + 1, temp.lastIndexOf(")"));
				}
				jsonObj = new JSONObject(temp);
				in.close();
				if (jsonObj.equals(null)) {
					throw new Exception("JSONObject is null");
				}
				// adds the return data to the result
				Iterator<String> keys = jsonObj.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					result.put(key, jsonObj.get(key));
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

			resultSet.add(result);

			return resultSet;
		}

		@Override
		public CompletableFuture<ResultSet> query( Map<String, Object> parameters ) throws IllegalArgumentException {
			CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync( ()->sendQuery( parameters ) );
			return future;
		}
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.datasource.AbstractDataSource#initialize()
	 */
	@Override
	protected void initialize() {
		addDataService( new BaseSimpleDataService(  this, MGI_SDS_1_ID, MGI_SDS_1_NAME, true ) );
		addDataService( new BaseSimpleDataService(  this, MGI_SDS_2_ID, MGI_SDS_2_NAME, false ) );
		addDataService( new BaseComplexDataService( this, MGI_CDS_1_ID, MGI_CDS_1_NAME, true ) );
		addDataService( new BaseComplexDataService( this, MGI_CDS_2_ID, MGI_CDS_2_NAME, false ) );
	}
}
