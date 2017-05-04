package bio.knowledge.service.beacon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;
import bio.knowledge.client.ApiClient;

@Service
@PropertySource("classpath:application.properties")
public class KnowledgeBeaconRegistry {
	private List<ApiClient> apiClients = new ArrayList<ApiClient>();

	@Value("${knowledgeBeacon.table.path}")
	private String knowledgeBeaconTablePath;
	
	@PostConstruct
	public void init() {
		// TODO: Once we've got our own knowledge.bio beacon online, change the
		// default URL. We always want our own knowledge source in the pool
		// (though in the future we could give the user the opportunity to
		// remove a knowledge source).
		assert (knowledgeBeaconTablePath != null);
		System.out.println(knowledgeBeaconTablePath);

		try {
			CSVReader knowledgeSourceReader = new CSVReader(new FileReader(knowledgeBeaconTablePath));
			List<String[]> knoweldgeSourceUrls = knowledgeSourceReader.readAll();
			Iterator<String[]> ksuIterator = knoweldgeSourceUrls.iterator();
			while (ksuIterator.hasNext()) {
				String url = ksuIterator.next()[0];
				addKnowledgeSource(url);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

	}

	private boolean check(String url) {		
		// TODO: Check if URL is satisfying syntax here
		return true;
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
