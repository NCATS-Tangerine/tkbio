package bio.knowledge.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiClient;
import bio.knowledge.model.ConceptImpl;

@Service
public class KnowledgeBeaconService {
	
	List<KnowledgeBeacon> dataSources = new ArrayList<KnowledgeBeacon>();
	
	public KnowledgeBeaconService(String name) {

		KnowledgeBeacon ks1 = new KnowledgeBeacon("http://localhost:8080/api/");
		KnowledgeBeacon ks2 = new KnowledgeBeacon("broken link");
		this.add(ks1);
		this.add(ks2);
	}
	
	public KnowledgeBeaconService() {

	}
	
	/*
	 * @Override

	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		
		for (KnowledgeBeacon dataSource : dataSources) {
			set.addAll(dataSource.getApiClients());
		}
		
		return set;
	}
		 */
	
	public void add(KnowledgeBeacon dataSource) {
		dataSources.add(dataSource);
	}

	public GetConceptDataService getConceptDataService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public CompletableFuture<List<ConceptImpl>> getConcepts(
			String filters,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {return null;}
	
	

}
