package bio.knowledge.service.beacon;

import java.util.HashSet;
import java.util.Set;

import bio.knowledge.client.ApiClient;

/**
 * This was originally going to wrap a {@code ApiClient} object, but I'm not
 * sure if that's very useful anymore.
 * 
 * @author lance
 *
 */
@Deprecated
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
