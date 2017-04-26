package bio.knowledge.datasource;

import java.util.HashSet;
import java.util.Set;

import bio.knowledge.client.ApiClient;

public class KnowledgeSource {
	
	private ApiClient apiClient;
	
	private final String NAME;
	private final String BASE_URL;

	public KnowledgeSource(String baseUrl, String name) {
		this.apiClient = new ApiClient();
		this.apiClient.setBasePath(baseUrl);
		NAME = name;
		BASE_URL = baseUrl;
	}
	
	public KnowledgeSource(String baseUrl) {
		this(baseUrl, null);
	}

	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		set.add(apiClient);
		return set;
	}
}
