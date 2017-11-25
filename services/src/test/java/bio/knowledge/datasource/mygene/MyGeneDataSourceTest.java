/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
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

package bio.knowledge.datasource.mygene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import bio.knowledge.datasource.gene.MyGeneInfoDataSource;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;

/**
 * JUnit tests for MyGeneDataSource
 * 
 * @author Richard
 *
 */
public class MyGeneDataSourceTest implements DataSourceTestUtil {
	
	private static Logger _logger = LoggerFactory.getLogger(MyGeneDataSourceTest.class);
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_logger.info("Running MyGeneDataSourceTest suite...");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_logger.info("Completed MyGeneDataSourceTest suite...");
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
		dataSource = new MyGeneInfoDataSource() ; 
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Target MyGene.info test query:
	 * http://mygene.info/v2/query?q=symbol%3AWRN&species=human ;
	 * 
	 * As of February 3rd, 2016, MyGenes.info, a search with the above query
	 * object, should return at least one result encoding the the semantic
	 * content implied in the following JSON:
	 * 
	 * { "hits": [ { "_id": "7486", "entrezgene": 7486, "name":
	 * "Werner syndrome, RecQ helicase-like", "symbol": "WRN", "taxid": 9606 //
	 * NCBI Taxonomic id for 'human'? } ], "max_score": 96.421906, "took": 2,
	 * "total": 1 }
	 */
	final String TEST_SPECIES = "human";
	final String TEST_GENESYMBOL = "WRN";
	final String TEST_RESULTNAME = "Werner syndrome, RecQ helicase-like";

	@Test // disable non-working test for now
	public void testMyGeneSimpleQuery1() {

		_logger.info("MyGeneDataSourceTest.testMyGeneQuery()");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( MyGeneInfoDataSource.MGI_SDS_1_ID, true );

		assertEquals(String.class, sds.getInputType());

		CompletableFuture<ResultSet> futureResult = sds.query(TEST_GENESYMBOL);

		assertNotNull(futureResult);

		ResultSet resultSet = null;
		
		/*
		 * This is probably a fragile test in that it relies on the perpetual
		 * conservation of this definition within the MyGene.info DataSource
		 */
		String geneSummary = "This gene encodes a member of the RecQ subfamily and the DEAH (Asp-Glu-Ala-His) subfamily of DNA and RNA helicases. "
				+ "DNA helicases are involved in many aspects of DNA metabolism, including transcription, replication, recombination, and repair. "
				+ "This protein contains a nuclear localization signal in the C-terminus and shows a predominant nucleolar localization. It possesses an intrinsic 3' "
				+ "to 5' DNA helicase activity, and is also a 3' to 5' exonuclease. Based on interactions between this protein and Ku70/80 heterodimer in DNA end"
				+ " processing, this protein may be involved in the repair of double strand DNA breaks. Defects in this gene are the cause of Werner syndrome, an autosomal"
				+ " recessive disorder characterized by premature aging.";

		int n = 0;
		do {
			try {
				if (futureResult.isDone()) {
					resultSet = futureResult.get();

					if (resultSet != null) {
						assertFalse(resultSet.isEmpty());
						assertEquals(resultSet.size(), 1);
						Result result = resultSet.get(0);
						_logger.info(result.toString());

						assertEquals(geneSummary, result.get("summary"));
						_logger.info(result.get("summary").toString());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}

			n += 1;

		} while (n < 10); // iterate for 10 seconds, if necessary?

		assertNotNull(resultSet);

		_logger.info("Retrieval took less than " + (n * 10.00) / 10.00 + " second(s) to run!");
	}

	@Test
	public void testMyGeneSimpleQuery2() {

		_logger.info("MyGeneSecondSimpleDataServiceTest.testMyGeneQuery()");

		@SuppressWarnings("unchecked")
		SimpleDataService<String> sds = 
			(SimpleDataService<String>) retrieveDataService( MyGeneInfoDataSource.MGI_SDS_2_ID, true );

		assertEquals(String.class, sds.getInputType());
		_logger.info("Describe:");
		_logger.info(sds.describe().toString());
		
		_logger.info(sds.getName());
		CompletableFuture<ResultSet> futureResult = sds.query(TEST_GENESYMBOL);

		assertNotNull(futureResult);

		ResultSet resultSet = null;
		/*
		 * This is probably a fragile test in that it relies on the perpetual
		 * conservation of this definition within the MyGene.info DataSource
		 */
		String geneSummary = "This gene encodes a member of the RecQ subfamily and the DEAH (Asp-Glu-Ala-His) subfamily of DNA and RNA helicases. "
				+ "DNA helicases are involved in many aspects of DNA metabolism, including transcription, replication, recombination, and repair. "
				+ "This protein contains a nuclear localization signal in the C-terminus and shows a predominant nucleolar localization. It possesses an intrinsic 3' "
				+ "to 5' DNA helicase activity, and is also a 3' to 5' exonuclease. Based on interactions between this protein and Ku70/80 heterodimer in DNA end"
				+ " processing, this protein may be involved in the repair of double strand DNA breaks. Defects in this gene are the cause of Werner syndrome, an autosomal"
				+ " recessive disorder characterized by premature aging.";

		Integer n = 0;
		do {
			try {
				if (futureResult.isDone()) {
					resultSet = futureResult.get();

					if (resultSet != null) {
						assertFalse(resultSet.isEmpty());
						assertEquals(resultSet.size(), 1);
						Result result = resultSet.get(0);
						_logger.info(result.toString());

						assertEquals(geneSummary, result.get("summary"));
						_logger.info(result.get("summary").toString());
						break;
					}
				}
			} catch (Exception e) {
				fail(e.getMessage());
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}

			n += 1;

		} while (n < 10); // iterate for 10 seconds, if necessary?

		assertNotNull(resultSet);

		_logger.info("Retrieval took less than " + (n * 10.00) / 10.00 + " second(s) to run!");
	}

	private static final long TIMEOUT_DURATION = 1;
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.MINUTES;
	
	private Void dumpResults( ResultSet rs ) {
		if(!rs.isEmpty()) {

			assertFalse(rs.isEmpty());
			assertEquals(rs.size(), 1);
			
			Result result = rs.get(0);
			
			_logger.info(result.toString());

			assertEquals(10116, result.get("taxid"));

		}
		return (Void)null ;
	}
	
	@Test 
	public void testMyGeneComplexQuery1() {
		
		ComplexDataService cds = 
				(ComplexDataService) retrieveDataService( MyGeneInfoDataSource.MGI_CDS_1_ID, false );
		
		Map<String,Object> args = new HashMap<String, Object>();
		
		// mygene.info/v2/query?q=symbol%3Awrn&fields=all&species=rat&size=10&from=0&facets=facets%3Dtaxid&species_facet_filter=rat&entrezonly=true&ensemblonly=true&callback=jsonpcallback&dotfield=true&filter=all&limit=10&skip=0
		args.put("q", "WRN");
		args.put("species", "rat");
		args.put("size", 10);
		args.put("from", 0);
		args.put("facets", "facets=taxid");
		args.put("species_facet_filter", "rat");
		args.put("entrezonly", "true");
		args.put("ensemblonly", "true");
		args.put("callback", "jsonpcallback");
		args.put("dotfield", "true");
		args.put("from", 0);
		args.put("filter", "all");
		args.put("from", 0);
		args.put("limit", 10);
		args.put("skip", 0);

		_logger.info("MyGeneComplexDataServiceTest.testMyGeneQuery()");

		CompletableFuture<ResultSet> futureResult = cds.query(args);

		assertNotNull(futureResult);

		ResultSet resultSet = null;

		int n = 0;
		do {
			try {
				resultSet = futureResult.get( TIMEOUT_DURATION, TIMEOUT_UNIT );
				futureResult.thenApply(this::dumpResults);
				break;

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				futureResult.completeExceptionally(e);
				fail(e.getMessage());
			} catch (Exception e) {
				fail(e.getMessage());
			}

			n += 1;

		} while (n < 3); // iterate a few times, if necessary?

		assertNotNull(resultSet);

		_logger.info("Retrieval took less than " + (n * 10.00) / 10.00 + " second(s) to run!");
	}
	
	@Test 
	public void testMyGeneComplexQuery2() {

		Map<String, Object> args = new HashMap<String, Object>();

		// this currently tests this query
		// mygene.info/v2/gene/290805?fields=all&callback=jsonpcallback&filter=all&dotfield=true
		args.put("geneid", 290805);
		args.put("fields", "all");
		args.put("callback", "jsonpcallback");
		args.put("filter", "all");
		args.put("dotfield", "true");

		_logger.info("MyGeneSecondComplexDataServiceTest.testMyGeneQuery()");
		
		ComplexDataService cds = 
				(ComplexDataService) retrieveDataService( MyGeneInfoDataSource.MGI_CDS_2_ID, false );
		
		_logger.info("Describe:");
		_logger.info(cds.describe().toString());

		CompletableFuture<ResultSet> futureResult = cds.query(args);

		assertNotNull(futureResult);

		ResultSet resultSet = null;

		int n = 0;
		do {
			try {

				if (futureResult.isDone()) {
					resultSet = futureResult.get();
					if (resultSet != null) {
						assertFalse(resultSet.isEmpty());
						assertEquals(resultSet.size(), 1);
						Result result = resultSet.get(0);
						_logger.info(result.toString());

						assertEquals("290805", result.get("_id"));
						break;
					}
				}

			} catch (Exception e) {
				fail(e.getMessage());
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}

			n += 1;

		} while (n < 10); // iterate for 10 seconds, if necessary?

		assertNotNull(resultSet);

		_logger.info("Retrieval took less than " + (n * 10.00) / 10.00 + " second(s) to run!");
	}

}
