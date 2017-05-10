package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.client.ApiClient;
import bio.knowledge.database.repository.beacon.BeaconRepository;
import bio.knowledge.model.beacon.KnowledgeBeacon;
import bio.knowledge.model.beacon.neo4j.Neo4jKnowledgeBeacon;

//@Service
//@PropertySource("classpath:application.properties")
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KnowledgeBeaconRegistry {
	
	private Map<String, ApiClient> apiClients = new HashMap<String,ApiClient>();

	/**
	 * TODO: Make it return an unmodifiable list? Maybe unnecessary, since this
	 * method cannot be used outside this package.
	 * 
	 * @return a list of the API clients currently added to the knowledge source
	 *         pool. Some knowledge sources will be added by default.
	 */
	protected List<ApiClient> getApiClients() {
		return new ArrayList<ApiClient>(apiClients.values());
	}

	// RMB May 10 - deprecated use of local CSV file(?)... won't delete the code for now
	//@Value("${knowledgeBeacon.table.filename}")
	//private String knowledgeBeaconTableFileName;
	
	@Autowired
	BeaconRepository beaconRepository;
	
	@PostConstruct
	public void init() {
		
		/* // RMB May 10 - deprecated use of local CSV file(?)... won't delete the code for now
		ApiClient api = new ApiClient();
		// TODO: Once we've got our own knowledge.bio beacon online, change the
		// default URL. We always want our own knowledge source in the pool
		// (though in the future we could give the user the opportunity to
		// remove a knowledge source).
		assert (knowledgeBeaconTableFileName != null);
		System.out.println(knowledgeBeaconTableFileName);
		
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource beaconTableResource = loader.getResource("classpath:"+knowledgeBeaconTableFileName);

		CSVReader knowledgeSourceReader = null ;
		try {
			File knowledgeBeaconTableFile = beaconTableResource.getFile();
			knowledgeSourceReader = new CSVReader(new FileReader(knowledgeBeaconTableFile));
			List<String[]> knoweldgeSourceUrls = knowledgeSourceReader.readAll();
			Iterator<String[]> ksuIterator = knoweldgeSourceUrls.iterator();
			while (ksuIterator.hasNext()) {
				String url = ksuIterator.next()[0];
				addKnowledgeSource(url);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(knowledgeSourceReader!=null)
				try {
					knowledgeSourceReader.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					//e.printStackTrace();
				}
		}
		*/
		
		// Sanity check: add in the reference knowledge beacon
		// Since the method doesn't create a beacon with a URL that is already there
		// this is idempotent for future startup sessions
		createBeacon(
				"Knowledge.Bio Beacon", 
				"KB 3.0 reference implementation", 
				"beacon.medgeninformatics.net/api" 
		);
		
		List<Neo4jKnowledgeBeacon> beacons = findAllBeacons();
		
		for(KnowledgeBeacon beacon : beacons) {
			System.out.println("\nInitializing Beacon\nId: "+beacon.getDbId().toString());
			System.out.println("Name: "+beacon.getName());
			System.out.println("Description: "+beacon.getDescription());
			
			String url = beacon.getUri();
			System.out.println("URL: "+url);

			// during initialization, I add knowledge sources I have previously persisted
			addKnowledgeSource(url);
		}

	}

	/*
	private boolean check(String url) {		
		return true;
	}
	*/

	/**
	 * Adds a knowledge source with the given URL to the knowledge source pool
	 * that will be queried by the methods in {@code KnowledgeBeaconService}.
	 * 
	 * @param url
	 */
	private void addKnowledgeSource(String url) {
		if(!apiClients.containsKey(url)) {
			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(url);
			apiClients.put(url,apiClient);
		}
	}

	@Transactional
	private boolean createBeacon( String name, String description, String url ) {
		
		// Sanity check - make sure that url's have an http or https protocol prefix
		if(!(url.startsWith("http://") || url.startsWith("https://"))) url = "http://"+url;
		
		Neo4jKnowledgeBeacon beacon = beaconRepository.findByUri(url) ;
		if(beacon==null) {
			beacon = new Neo4jKnowledgeBeacon( name, description, url );
			beaconRepository.save(beacon) ;
			return true;
		} else
			return false;
	}
	
	/**
	 * Adds a Knowledge Beacon specification to the application Registry.
	 * This is idempotent with respect to the url argument as identity:
	 * Beacons are only added if not there already
	 *  
	 * @param name
	 * @param description
	 * @param url
	 */
	public void addKnowledgeBeacon( String name, String description, String url ) {
		
		if(createBeacon(name, description, url))
			// if a new beacon, add it to the current session?
			addKnowledgeSource(url) ;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Neo4jKnowledgeBeacon> findAllBeacons() {
		List<Neo4jKnowledgeBeacon> beacons = beaconRepository.findAllBeacons();
		return beacons ;
	}

}
