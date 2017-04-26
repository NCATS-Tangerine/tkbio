package bio.knowledge.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
		
		@SuppressWarnings("unchecked")
		CompletableFuture<List<ConceptImpl>>[] futures = new CompletableFuture[apiClients.size()];
		
		int i = 0;
		for (ApiClient apiClient : apiClients) {
			CompletableFuture<List<ConceptImpl>> future = CompletableFuture.supplyAsync(
					buildSupplier(apiClient, filters, semanticGroups, pageNumber, pageSize)
			);
			
			futures[i++] = future;
		}
		
		CompletableFuture<List<ConceptImpl>> combinedFuture = combineFutures(futures);
		
		return combinedFuture;
	}

	/**
	 * Here we take all of the CompletableFuture objects in futures, and combine them into
	 * a single CompletableFuture object. This combined future is of type Void, so we need
	 * thenApply() to get the proper sort of CompletableFuture. Also this combinedFuture
	 * completes exceptionally if any of the items in {@code futures} completes exceptionally.
	 * Because of this, we also need to tell it what to do if it completes exceptionally,
	 * which is done with exceptionally().
	 * @param futures
	 * @return
	 */
	private CompletableFuture<List<ConceptImpl>> combineFutures(CompletableFuture<List<ConceptImpl>>[] futures) {
		return CompletableFuture.allOf(futures)
				.thenApply(x -> {
					
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					
					for (CompletableFuture<List<ConceptImpl>> f : futures) {
						List<ConceptImpl> result = f.join();
						if (result != null) {
							concepts.addAll(result);
						}
					}
					
					return concepts;
				})
				.exceptionally((error) -> {
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					
					for (CompletableFuture<List<ConceptImpl>> f : futures) {
						if (!f.isCompletedExceptionally()) {
							List<ConceptImpl> result = f.join();
							if (result != null) {
								concepts.addAll(result);
							}
						}
					}
					return concepts;
				});
	}

	private Supplier<List<ConceptImpl>> buildSupplier(
			ApiClient apiClient,
			List<String> filters,
			List<String> semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return new Supplier<List<ConceptImpl>>() {

			@Override
			public List<ConceptImpl> get() {
				ConceptsApi conceptsApi = new ConceptsApi(apiClient);
				InlineResponse2001 response;
				try {
					System.out.println(conceptsApi.getApiClient().getBasePath());
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
