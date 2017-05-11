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
import bio.knowledge.model.beacon.neo4j.Neo4jKnowledgeBeacon;

//@Service
//@PropertySource("classpath:application.properties")
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KnowledgeBeaconRegistry {
	
	@Autowired
	BeaconRepository beaconRepository;
	
	private List<KnowledgeBeacon> knowledgeBeacons = new ArrayList<KnowledgeBeacon>();
	
	public List<KnowledgeBeacon> getKnowledgeBeacons() {
		return this.knowledgeBeacons;
	}
	
	@PostConstruct
	public void init() {
		createBeacon(
				"Knowledge.Bio Beacon", 
				"KB 3.0 reference implementation", 
				"beacon.medgeninformatics.net/api" 
		);
		
		List<Neo4jKnowledgeBeacon> beacons = findAllBeacons();
		
		for(Neo4jKnowledgeBeacon beacon : beacons) {
			System.out.println("\nInitializing Beacon\nId: "+beacon.getDbId().toString());
			System.out.println("Name: "+beacon.getName());
			System.out.println("Description: "+beacon.getDescription());
			
			String url = beacon.getUri();
			System.out.println("URL: "+url);

			// during initialization, I add knowledge sources I have previously persisted
			addKnowledgeSource(url, beacon.getName(), beacon.getDescription());
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
	private void addKnowledgeSource(String url, String name, String description) {
		KnowledgeBeacon kb = new KnowledgeBeacon(url, name, description);
		if (!knowledgeBeacons.contains(kb)) {
			this.knowledgeBeacons.add(kb);
		}
	}

	@Transactional
	private boolean createBeacon( String name, String description, String url ) {
		
		// Sanity check - make sure that url's have an http or https protocol prefix
		if(!(url.startsWith("http://") || url.startsWith("https://"))) url = "http://"+url;
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		
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
			addKnowledgeSource(url, name, description) ;
	}
	
	public List<Neo4jKnowledgeBeacon> findAllBeacons() {
		List<Neo4jKnowledgeBeacon> beacons = beaconRepository.findAllBeacons();
		return beacons ;
	}

}
