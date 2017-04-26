package bio.knowledge.datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bio.knowledge.client.ApiClient;

public class KnowledgeSourcePool extends KnowledgeSource {
	List<KnowledgeSource> dataSources = new ArrayList<KnowledgeSource>();
	
	public KnowledgeSourcePool(String name) {
		super(null, name);
	}
	
	public KnowledgeSourcePool() {
		super(null, null);
	}
	
	@Override
	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		
		for (KnowledgeSource dataSource : dataSources) {
			set.addAll(dataSource.getApiClients());
		}
		
		return set;
	}
	
	public void add(KnowledgeSource dataSource) {
		dataSources.add(dataSource);
	}

}
