package bio.knowledge.datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bio.knowledge.client.ApiClient;

public class KnowledgeSourcePool extends KnowledgeSource {
	// Maybe change this to a linked list for O(1) add operation,
	// since we will probably never need indexing.
	List<KnowledgeSource> dataSources = new ArrayList<KnowledgeSource>();

	public KnowledgeSourcePool(String dataSourceId, String name) {
		super(dataSourceId, name, null);
	}
	
	@Override
	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		
		for (KnowledgeSource dataSource : dataSources) {
			set.addAll(dataSource.getApiClients());
		}
		
		return set;
	}

	@Override
	protected void initialize() {
		
	}
	
	public void addKnowledgeSource(KnowledgeSource dataSource) {
		dataSources.add(dataSource);
	}

}
