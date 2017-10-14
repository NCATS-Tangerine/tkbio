package bio.knowledge.service.beacon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.AggregatorApi;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.PredicatesApi;
import bio.knowledge.client.api.StatementsApi;

/**
 * This base class encapsulates the functionality required to set up KnowledgeBeaconService
 * after Spring has built the instance.
 * 
 * @author lance
 *
 */
public class KnowledgeBeaconServiceBase {
	
	@Value( "${beaconAggregator.url}" )
	private String AGGREGATOR_BASE_URL;
	
	public String getAggregatorBaseUrl() {
		if (!AGGREGATOR_BASE_URL.startsWith("http://") && !AGGREGATOR_BASE_URL.startsWith("https://")) {
			AGGREGATOR_BASE_URL = "http://" + AGGREGATOR_BASE_URL;
		}
		
		return AGGREGATOR_BASE_URL;
	}
	
	private Map<String, String> beaconIdMap = null;
	private List<bio.knowledge.client.model.KnowledgeBeacon> beacons = null;
	
	private ApiClient apiClient;
	private ConceptsApi conceptsApi;
	private PredicatesApi predicatesApi;
	private StatementsApi statementsApi;
	private EvidenceApi evidenceApi;
	private AggregatorApi aggregatorApi;
	
	public ConceptsApi getConceptsApi() {
		return this.conceptsApi;
	}
	
	public PredicatesApi getPredicatesApi() {
		return this.predicatesApi;
	}
	
	public StatementsApi getStatementsApi() {
		return this.statementsApi;
	}
	
	public EvidenceApi getEvidenceApi() {
		return this.evidenceApi;
	}
	
	public AggregatorApi getAggregatorApi() {
		return this.aggregatorApi;
	}
	
	@PostConstruct
	private void initBeacons() {
		apiClient = new ApiClient();
		apiClient.setBasePath(AGGREGATOR_BASE_URL);
		conceptsApi = new ConceptsApi(apiClient);
		predicatesApi = new PredicatesApi(apiClient);
		statementsApi = new StatementsApi(apiClient);
		evidenceApi = new EvidenceApi(apiClient);
		aggregatorApi = new AggregatorApi(apiClient);
	}
	
	public List<bio.knowledge.client.model.KnowledgeBeacon> getKnowledgeBeacons() {
		if (beacons == null) {
			setupBeacons();
		}
		
		return beacons;
	}
	
	public Map<String, String> getBeaconIdMap() {
		if (beaconIdMap == null) {
			setupBeacons();
		}
		
		return this.beaconIdMap;
	}
	
	private void setupBeacons() {
		try {
			beaconIdMap = new HashMap<String, String>();
			beacons = aggregatorApi.getBeacons();
			for (bio.knowledge.client.model.KnowledgeBeacon b : beacons) {
				beaconIdMap.put(b.getId(), b.getName());
			}
		} catch (ApiException e) {
			beaconIdMap = null;
			throw new RuntimeException("Could not connect to the Beacon Aggregator. Make sure that it is online and that you have set up application.properties properly");
		}
	}

}

