package bio.knowledge.datasource;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import bio.knowledge.datasource.GetConceptDataService.ConceptImpl;

public class GetConceptDataServiceTest {
	
	private final List<String> filters = Arrays.asList(new String[]{"chocolate", "milk"});
	private final List<String> semanticGroups = Arrays.asList(new String[]{"GENE", "OBJC"});
	private final int pageNumber = 0;
	private final int pageSize = 50;
	private final int timeDuration = 60;
	private final TimeUnit timeUnit = TimeUnit.SECONDS;
	
	private final int expectedNumberOfResults = 18;
	
	@Test
	public void testSingleKnowledgeSource() {
		KnowledgeSource ks = new KnowledgeSource("tkbio", "knowledge.bio", "http://localhost:8080/api/");
		GetConceptDataService service = new GetConceptDataService(ks);
		
		CompletableFuture<List<ConceptImpl>> future =
				service.query(filters, semanticGroups, pageNumber, pageSize);
		
		try {
			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
			
			assertTrue(concepts.size() == expectedNumberOfResults);
		} catch (InterruptedException | ExecutionException e) {
			fail("Unexpected Exception");
		} catch (TimeoutException e) {
			fail("Timeout");
		}
	}
	
	@Test
	public void testDuplicateKnowledgeSources() {
		KnowledgeSource ks1 = new KnowledgeSource("tkbio1", "knowledge.bio1", "http://localhost:8080/api/");
		KnowledgeSource ks2 = new KnowledgeSource("tkbio2", "knowledge.bio2", "http://localhost:8080/api/");
		KnowledgeSourcePool ksPool = new KnowledgeSourcePool("pool", "pool");
		
		ksPool.addKnowledgeSource(ks1);
		ksPool.addKnowledgeSource(ks2);
		
		GetConceptDataService service = new GetConceptDataService(ksPool);
		
		CompletableFuture<List<ConceptImpl>> future =
				service.query(filters, semanticGroups, pageNumber, pageSize);
		
		try {
			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
			
			assertTrue(concepts.size() == 2 * expectedNumberOfResults);
		} catch (InterruptedException | ExecutionException e) {
			fail("Unexpected Exception");
		} catch (TimeoutException e) {
			fail("Timeout");
		}
	}
	
	@Test
	public void testBrokenKnowledgeSources() {
		KnowledgeSource ks1 = new KnowledgeSource("tkbio1", "knowledge.bio1", "http://localhost:8080/api/");
		KnowledgeSource ks2 = new KnowledgeSource("broken", "broken source", "broken URI");
		KnowledgeSourcePool ksPool = new KnowledgeSourcePool("pool", "pool");
		
		ksPool.addKnowledgeSource(ks1);
		ksPool.addKnowledgeSource(ks2);
		
		GetConceptDataService service = new GetConceptDataService(ksPool);
		
		CompletableFuture<List<ConceptImpl>> future =
				service.query(filters, semanticGroups, pageNumber, pageSize);
		
		try {
			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
			
			assertTrue(concepts.size() == expectedNumberOfResults);
		} catch (InterruptedException | ExecutionException e) {
			fail("Unexpected Exception");
		} catch (TimeoutException e) {
			fail("Timeout");
		}
	}

}
