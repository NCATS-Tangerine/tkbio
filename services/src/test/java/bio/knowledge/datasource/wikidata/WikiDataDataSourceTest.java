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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.knowledge.datasource.ComplexDataService;
import bio.knowledge.datasource.DataSource;
import bio.knowledge.datasource.DataSourceTestUtil;
import bio.knowledge.datasource.SimpleDataService;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;


/**
 * @author Richard
 *
 */
public class WikiDataDataSourceTest  implements DataSourceTestUtil {
	
	private static Logger _logger = LoggerFactory.getLogger(WikiDataDataSourceTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_logger.info("Running WikiDataDataSourceTest suite...");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_logger.info("Completed WikiDataDataSourceTest suite...");
	}

	private DataSource dataSource ; 
	
	/**
	 * 
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource ;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dataSource = new WikiDataDataSource() ; 
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private static Set<ConceptType> dstypes = new HashSet<ConceptType>();
	
	static {
		dstypes.add(ConceptType.GENE) ;
		dstypes.add(ConceptType.ANY) ;
	}

	@Test 
	public void testDataSourceCreation() {
		
		DataSource ds = getDataSource() ;

		assertEquals( ds.getIdentifier(), WikiDataDataSource.WIKIDATA_DATASOURCE_ID);
        
		assertEquals( ds.getName(), WikiDataDataSource.WIKIDATA_DATASOURCE_NAME);
        
		assertEquals( ds.getTargetSemanticGroups(), dstypes ) ;
	}

	private static final long TIMEOUT_DURATION = 1;
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MINUTES;
	
	// Literals need to be internally quoted?
	private static final String TEST_GENESYMBOL    = "WRN";
	private static final String TEST_FILTER        = "X|NM|EN";
	private static final String TEST_GENE_RESOURCE = "http://www.wikidata.org/entity/Q14883734";
	private static final String TEST_ENTREZID      = "7486";
	
	// the colon in front of the property id, 
	// signals a straight resource string substitute without quotes?
	private static final String TEST_WIKIDATA_QUALIFIED_PROPERTY_ID = "wd:P353"; 
	private static final String TEST_WIKIDATA_PROPERTY_URI = "http://www.wikidata.org/entity/P353"; 
	private static final String TEST2_WIKIDATA_ID = "wd:Q13561329";
	
	private Void dumpResults( ResultSet rs ) {
		rs.stream().forEach(r->{
			_logger.info(r.toString());
		});
		return (Void)null ;
	}

	private void runQuery( 
			CompletableFuture< ResultSet > futureResult, 
			Function<? super ResultSet, ? extends Void> dumpResults 
	) {
		
		assertNotNull(futureResult);

		ResultSet rs = null;
		
		int n = 0;
		do {
			try {
				rs = futureResult.get( TIMEOUT_DURATION, TIMEOUT_UNIT );
				
				assertFalse("ResultSet is empty?",rs.isEmpty());
				
				futureResult.thenApply(dumpResults);
				
				break;

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				futureResult.completeExceptionally(e);
				fail(e.getMessage());
			} catch (Exception e) {
				fail(e.getMessage());
			}

			n += 1;

		} while (n < 3); // iterate a few times, if necessary?

		assertNotNull(rs);

		_logger.info("Retrieval took less than " + (n * 10.00) / 10.00 + " second(s) to run!");
		
	}
	
	private Void dumpGeneResults( ResultSet rs ) {
		
		Result result = rs.get(0);

		assertEquals("Gene Resource is not correct?",    TEST_GENE_RESOURCE, result.get("gene"));
		assertEquals("Gene (Entrez) ID is not correct?", TEST_ENTREZID,      result.get("entrezID"));
		
		_logger.info("dumpGeneResults() Query Result: "+result.toString());
		
		return (Void)null ;
	}

	@Test
	// WikiData: first simple query by gene symbol string to retrieve NCBI gene identifiers
	public void wikiDataSimpleDataServiceTest1() {

		_logger.info("wikiDataSimpleDataServiceTest1("+TEST_GENESYMBOL+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_1_ID, true );
		
		CompletableFuture< ResultSet > futureResult = sds.query(TEST_GENESYMBOL) ;
		
		runQuery(futureResult,this::dumpGeneResults);
		
	}
	
	@Test
	public void wikiDataSimpleDataServiceTest2() {

		_logger.info("wikiDataSimpleDataServiceTest2("+TEST2_WIKIDATA_ID+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_2_ID, true );

		CompletableFuture< ResultSet > futureResult = sds.query(TEST2_WIKIDATA_ID) ;
		
		runQuery(futureResult,this::dumpResults);
	}
	
	@Test
	public void wikiDataSimpleDataServiceTest3() {

		_logger.info("wikiDataSimpleDataServiceTest3("+TEST_GENESYMBOL+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_3_ID, true );

		CompletableFuture< ResultSet > futureResult = sds.query(TEST_GENESYMBOL) ;
		
		runQuery(futureResult,this::dumpResults);
	}
	
	@Test
	public void wikiDataSimpleDataServiceCountingTest3() {

		_logger.info("wikiDataSimpleDataServiceCountingTest3("+TEST_GENESYMBOL+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_3_COUNTING_ID, true );

		CompletableFuture< ResultSet > futureResult = sds.query(TEST_GENESYMBOL) ;
		
		runQuery(futureResult,this::dumpResults);
	}
	
	@Test
	public void wikiDataSimpleDataServiceTest4a() {

		_logger.info("wikiDataSimpleDataServiceTest4a("+TEST_WIKIDATA_QUALIFIED_PROPERTY_ID+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_4_ID, true );

		CompletableFuture< ResultSet > futureResult = sds.query(TEST_WIKIDATA_QUALIFIED_PROPERTY_ID) ;
		
		runQuery(futureResult,this::dumpResults);
	}
	
	@Test
	public void wikiDataSimpleDataServiceTest4b() {

		_logger.info("wikiDataSimpleDataServiceTest4b("+TEST_WIKIDATA_PROPERTY_URI+")");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( WikiDataDataSource.WD_SDS_4_ID, true );

		CompletableFuture< ResultSet > futureResult = sds.query(TEST_WIKIDATA_PROPERTY_URI) ;
		
		runQuery(futureResult,this::dumpResults);
	}
	
	@Test
	public void wikiDataComplexDataServiceTest1() {

		_logger.info("wikiDataComplexDataServiceTest1("+
					  TEST2_WIKIDATA_ID+"):"+
					  WikiDataDataSource.WD_CDS_1_ID);

		ComplexDataService cds = 
			(ComplexDataService) retrieveDataService( WikiDataDataSource.WD_CDS_1_ID, false );

		Map<String, Object> args = new HashMap<String, Object>();

		args.put("input", TEST2_WIKIDATA_ID);
		args.put("language", "en");

		CompletableFuture<ResultSet> futureResult = cds.query(args);

		runQuery(futureResult,this::dumpResults);
		
	}
	
	@Test
	public void wikiDataComplexDataServiceTest3() {
		
		_logger.info("wikiDataComplexDataServiceTest3("+
				TEST_GENESYMBOL+"):"+
				  WikiDataDataSource.WD_CDS_3_ID);

		ComplexDataService cds =
			(ComplexDataService) retrieveDataService( WikiDataDataSource.WD_CDS_3_ID, false );
		
		Map<String, Object> args = new HashMap<String, Object>();

		args.put("input", TEST_GENESYMBOL);
		args.put("filter", TEST_FILTER);
		
		/*
		 *  Unfortunately, the underlying SPARQL queries don't facilitate
		 *  directly in situ computations for paging like Neo4j Cypher queries, 
		 *  so these values need to be precomputed, and actual LIMIT and OFFSET
		 *  applied to the SPARQL query as direct integer variable substitution
		 */
		int pageSize = 5 ;
		int pageNumber = 2 ;
		args.put("limit",  pageSize);     
		args.put("offset", (pageNumber-1)*pageSize);  

		CompletableFuture<ResultSet> futureResult = cds.query(args);

		runQuery(futureResult,this::dumpResults);
	}

	@Test
	public void wikiDataComplexDataServiceTest4() {

		_logger.info("wikiDataComplexDataServiceTest4("+TEST_ENTREZID+")");

		ComplexDataService cds = 
			(ComplexDataService) retrieveDataService( WikiDataDataSource.WD_CDS_4_ID, false );
		
		Map<String, Object> args = new HashMap<String, Object>();

		args.put("geneid", TEST_ENTREZID);
		CompletableFuture< ResultSet > futureResult = cds.query(args) ;
		
		runQuery(futureResult,this::dumpResults);
	}

}
