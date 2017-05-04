package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiClient;

@Service
public class KnowledgeBeaconRegistry {
	private List<ApiClient> apiClients = new ArrayList<ApiClient>();

	@PostConstruct
	public void init() {
		// TODO: Once we've got our own knowledge.bio beacon online, change the
		// default URL. We always want our own knowledge source in the pool
		// (though in the future we could give the user the opportunity to
		// remove a knowledge source).
		addKnowledgeSource("http://localhost:8090/api/");
	}

	/**
	 * TODO: Make it return an unmodifiable list? Maybe unnecessary, since this
	 * method cannot be used outside this package.
	 * 
	 * @return a list of the API clients currently added to the knowledge source
	 *         pool. Some knowledge sources will be added by default.
	 */
	protected List<ApiClient> getApiClients() {
		return apiClients;
	}

	/**
	 * Adds a knowledge source with the given URL to the knowledge source pool
	 * that will be queried by the methods in {@code KnowledgeBeaconService}.
	 * 
	 * @param url
	 */
	public void addKnowledgeSource(String url) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(url);
		apiClients.add(apiClient);
	}
}
