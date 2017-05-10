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
import bio.knowledge.service.beacon.GenericKnowledgeService;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

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
		fail();
//		KnowledgeBeacon ks = new KnowledgeBeacon(serverUrl);
//		GenericKnowledgeService service = new GenericKnowledgeService(ks);
//		
//		CompletableFuture<List<ConceptImpl>> future =
//				service.query(filters, semanticGroups, pageNumber, pageSize);
//		
//		try {
//			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
//			
//			assertTrue(concepts.size() == expectedNumberOfResults);
//		} catch (InterruptedException | ExecutionException e) {
//			fail("Unexpected Exception");
//		} catch (TimeoutException e) {
//			fail("Timeout");
//		}
	}
	
	@Test
	public void testDuplicateKnowledgeSources() {
		fail();
//		KnowledgeBeacon ks1 = new KnowledgeBeacon(serverUrl);
//		KnowledgeBeacon ks2 = new KnowledgeBeacon(serverUrl);
//		KnowledgeBeaconService ksPool = new KnowledgeBeaconService();
//		
//		ksPool.add(ks1);
//		ksPool.add(ks2);
//		
//		GenericKnowledgeService service = new GenericKnowledgeService(ksPool);
//		
//		CompletableFuture<List<ConceptImpl>> future =
//				service.query(filters, semanticGroups, pageNumber, pageSize);
//		
//		try {
//			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
//			
//			assertTrue(concepts.size() == 2 * expectedNumberOfResults);
//		} catch (InterruptedException | ExecutionException e) {
//			fail("Unexpected Exception");
//		} catch (TimeoutException e) {
//			fail("Timeout");
//		}
	}
	
	@Test
	public void testBrokenKnowledgeSources() {
		fail();
//		KnowledgeBeacon ks1 = new KnowledgeBeacon(serverUrl);
//		KnowledgeBeacon ks2 = new KnowledgeBeacon("Broken URL");
//		KnowledgeBeaconService ksPool = new KnowledgeBeaconService();
//		
//		ksPool.add(ks1);
//		ksPool.add(ks2);
//		
//		GenericKnowledgeService service = new GenericKnowledgeService(ksPool);
//		
//		CompletableFuture<List<ConceptImpl>> future =
//				service.query(filters, semanticGroups, pageNumber, pageSize);
//		
//		try {
//			List<ConceptImpl> concepts = future.get(timeDuration, timeUnit);
//			
//			assertTrue(concepts.size() == expectedNumberOfResults);
//		} catch (InterruptedException | ExecutionException e) {
//			fail("Unexpected Exception");
//		} catch (TimeoutException e) {
//			fail("Timeout");
//		}
	}

}
