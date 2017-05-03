package bio.knowledge.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import bio.knowledge.client.ApiClient;
import bio.knowledge.model.ConceptImpl;

public class KnowledgeBeacon {
	
	private ApiClient apiClient;
	
	private final String NAME;
	private final String BASE_URL;

	public KnowledgeBeacon(String baseUrl, String name) {
		this.apiClient = new ApiClient();
		this.apiClient.setBasePath(baseUrl);
		NAME = name;
		BASE_URL = baseUrl;
	}
	
	public KnowledgeBeacon(String baseUrl) {
		this(baseUrl, null);
	}

	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		set.add(apiClient);
		return set;
	}


}
