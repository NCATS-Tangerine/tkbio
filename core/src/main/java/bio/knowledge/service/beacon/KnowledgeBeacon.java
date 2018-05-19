package bio.knowledge.service.beacon;

import java.net.URI;
import java.net.URISyntaxException;

import bio.knowledge.client.ApiClient;

/**
 * Wraps an ApiClient
 * 
 * @author Lance Hannestad
 *
 */
public class KnowledgeBeacon {
	private String name;
	private String description;
	private boolean isEnabled;
	
	private final ApiClient apiClient;
	
	public KnowledgeBeacon(String url, String name, String description) {
		this(url, name, description, true);
	}
	
	public KnowledgeBeacon(String url, String name, String description, boolean isEnabled) {
		try {
			if(!(url.startsWith("http://") || url.startsWith("https://"))) url = "http://"+url;
			if (url.endsWith("/")) {
				url = url.substring(0, url.length() - 1);
			}
			new URI(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException("URL: " + url + " is not valid.");
		}
		
		this.name = name;
		this.description = description;
		this.isEnabled = isEnabled;
		
		this.apiClient = new ApiClient();
		this.apiClient.setBasePath(url);
	}
	
	public KnowledgeBeacon(String url) {
		this(url, null, null);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getUrl() {
		return this.apiClient.getBasePath();
	}
	
	protected ApiClient getApiClient() {
		return this.apiClient;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		} else if (! (other instanceof KnowledgeBeacon)) {
			return false;
		} else {
			KnowledgeBeacon otherKnowledgeBeacon = (KnowledgeBeacon) other;
			
			return this.apiClient.getBasePath().equals(otherKnowledgeBeacon.apiClient.getBasePath());
		}		
	}
	
	@Override
	public int hashCode() {
		return this.apiClient.getBasePath().hashCode();
	}
	
	@Override
	public String toString() {
		if (getName() != null) {
			return "KnowledgeBeacon[url=" + getUrl() + ", name=" + getName() + "]";
		} else {
			return "KnowledgeBeacon[url=" + getUrl() + "]";
		}
	}
}
