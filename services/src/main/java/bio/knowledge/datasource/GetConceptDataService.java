package bio.knowledge.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.apache.commons.lang3.NotImplementedException;

import com.squareup.okhttp.Call;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2001DataPage;
import bio.knowledge.model.Concept;
import bio.knowledge.model.Library;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.core.Feature;
import bio.knowledge.model.core.IdentifiedEntity;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;

@SuppressWarnings("unused")
public class GetConceptDataService extends AbstractComplexDataService {
	public static final String ID = "conceptDataService";

	private ConceptsApi conceptsApi = new ConceptsApi();

	public GetConceptDataService(KnowledgeSource knowledgeSource) {
		super(knowledgeSource, ID, SemanticGroup.ANY, knowledgeSource.getName() + "." + ID);
	}

	public CompletableFuture<List<ConceptImpl>> query(
			List<String> filters,
			List<String> semanticGroups,
			int pageNumber,
			int pageSize
	) {
		Set<ApiClient> apiClients = getDataSource().getApiClients();
		
		CompletableFuture<List<ConceptImpl>> priorFuture = null;
		
		for (ApiClient apiClient : apiClients) {
			conceptsApi.setApiClient(apiClient);
			
			CompletableFuture<List<ConceptImpl>> future = CompletableFuture.supplyAsync(buildSupplier(filters, semanticGroups, pageNumber, pageSize));
			
			if (priorFuture != null) {
				priorFuture = future.thenCombine(priorFuture, (list1, list2) -> combineLists(list1, list2));
			} else {
				priorFuture = future;
			}
		}
		
		return priorFuture;
	}

	private Supplier<List<ConceptImpl>> buildSupplier(List<String> filters, List<String> semanticGroups, int pageNumber,
			int pageSize) {
		return new Supplier<List<ConceptImpl>>() {

			@Override
			public List<ConceptImpl> get() {
				InlineResponse2001 response;
				try {
					response = conceptsApi.getConcepts(filters, semanticGroups, pageNumber, pageSize);
					List<InlineResponse2001DataPage> dataPages = response.getDataPage();
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					
					for (InlineResponse2001DataPage dataPage : dataPages) {
						ConceptImpl concept = new ConceptImpl(dataPage.getName(), Long.decode(dataPage.getId()));
						concepts.add(concept);
					}
					return concepts;
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
			
		};
	}
	
	private List<ConceptImpl> combineLists(List<ConceptImpl> list1, List<ConceptImpl> list2) {
		list1.addAll(list2);
		return list1;
	}

	@Override
	public KnowledgeSource getDataSource() {
		return (KnowledgeSource) super.getDataSource();
	}

	@Override
	public CompletableFuture<ResultSet> query(Map<String, Object> parameters) throws IllegalArgumentException {
		throw new NotImplementedException("");
	}

	@Override
	public Boolean isSimple() {
		return false;
	}
	
	public class ConceptImpl implements Concept {
		private String name;
		private Long id;
		
		public ConceptImpl(String name, Long id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public void setUri(String uri) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setAccessionId(String accessionId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setName(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDescription(String description) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setSynonyms(String synonyms) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Long getId() {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public void setId(Long id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Integer getVersion() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setVersion(Integer version) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public long getVersionDate() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setVersionDate(long versionDate) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getUri() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getSynonyms() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int compareTo(IdentifiedEntity o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setFeatures(Set<Feature> features) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Set<Feature> getFeatures() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setSemanticGroup(SemanticGroup semgroup) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public SemanticGroup getSemanticGroup() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Long getUsage() {
			// TODO Auto-generated method stub
			return this.id;
		}

		@Override
		public void setUsage(Long usage) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void incrementUsage(Long increment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void incrementUsage() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setLibrary(Library library) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Library getLibrary() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getGhr() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setGhr(String ghr) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getHmdbId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setHmdbId(String hmdbId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getChebi() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setChebi(String chebi) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Set<String> getCrossReferences() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<String> getTerms() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return this.name;
		}

		@Override
		public String getAccessionId() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
