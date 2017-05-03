package bio.knowledge.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.model.ConceptImpl;
import bio.knowledge.model.SemanticGroup;

public class GetConceptDataService {
	
	public static final String ID = "conceptDataService";

	private final KnowledgeSource knowledgeSource;
	
	public GetConceptDataService(KnowledgeSource knowledgeSource) {
		this.knowledgeSource = knowledgeSource;
	}

	public CompletableFuture<List<ConceptImpl>> query(
			String filters,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		Set<ApiClient> apiClients = knowledgeSource.getApiClients();
		
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
			String filters,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return new Supplier<List<ConceptImpl>>() {

			@Override
			public List<ConceptImpl> get() {
				ConceptsApi conceptsApi = new ConceptsApi(apiClient);
				
				try {
					List<InlineResponse2001> responses = conceptsApi.getConcepts(filters, semanticGroups, pageNumber, pageSize);
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					
					for (InlineResponse2001 response : responses) {
						SemanticGroup semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						ConceptImpl concept = new ConceptImpl(
								response.getId(),
								semgroup,
								response.getName()
						);
						concepts.add(concept);
					}
					return concepts;
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
			
		};
	}
}
