package bio.knowledge.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import bio.knowledge.model.ConceptImpl;
import bio.knowledge.service.GetConceptDataService;
import bio.knowledge.service.KnowledgeBeacon;
import bio.knowledge.service.KnowledgeBeaconService;

public class GetConceptDataServiceTest {
	
	private final String filters = "chocolate milk";
	private final String semanticGroups = "GENE OBJC";
	private final int pageNumber = 0;
	private final int pageSize = 50;
	private final int timeDuration = 60;
	private final TimeUnit timeUnit = TimeUnit.SECONDS;
	private final String serverUrl = "http://localhost:8080/api/";
	
	private final int expectedNumberOfResults = 18;
	
	@Test
	public void testSingleKnowledgeSource() {
		KnowledgeBeacon ks = new KnowledgeBeacon(serverUrl);
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
		KnowledgeBeacon ks1 = new KnowledgeBeacon(serverUrl);
		KnowledgeBeacon ks2 = new KnowledgeBeacon(serverUrl);
		KnowledgeBeaconService ksPool = new KnowledgeBeaconService();
		
		ksPool.add(ks1);
		ksPool.add(ks2);
		
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
		KnowledgeBeacon ks1 = new KnowledgeBeacon(serverUrl);
		KnowledgeBeacon ks2 = new KnowledgeBeacon("Broken URL");
		KnowledgeBeaconService ksPool = new KnowledgeBeaconService();
		
		ksPool.add(ks1);
		ksPool.add(ks2);
		
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
