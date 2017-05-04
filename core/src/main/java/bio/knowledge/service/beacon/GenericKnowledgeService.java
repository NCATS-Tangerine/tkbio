package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import bio.knowledge.client.ApiClient;

public class GenericKnowledgeService {

	// This works because {@code GenericKnowledgeService} is extended by {@code
	// KnowledgeBeaconService}, which is a Spring service.
	@Autowired
	KnowledgeBeaconRegistry registry;

	protected <T> CompletableFuture<List<T>> query(ListSupplier<T> supplier) {
		@SuppressWarnings("unchecked")
		CompletableFuture<List<T>>[] futures = new CompletableFuture[registry.getApiClients().size()];

		int i = 0;
		for (ApiClient apiClient : registry.getApiClients()) {
			supplier.setApiClient(apiClient);

			CompletableFuture<List<T>> future = CompletableFuture.supplyAsync(supplier);

			futures[i++] = future;
		}

		CompletableFuture<List<T>> combinedFuture = combineFutures(futures);

		return combinedFuture;
	}

	/**
	 * Here we take all of the CompletableFuture objects in futures, and combine
	 * them into a single CompletableFuture object. This combined future is of
	 * type Void, so we need thenApply() to get the proper sort of
	 * CompletableFuture. Also this combinedFuture completes exceptionally if
	 * any of the items in {@code futures} completes exceptionally. Because of
	 * this, we also need to tell it what to do if it completes exceptionally,
	 * which is done with exceptionally().
	 * 
	 * @param <T>
	 * @param futures
	 * @return
	 */
	private <T> CompletableFuture<List<T>> combineFutures(CompletableFuture<List<T>>[] futures) {
		return CompletableFuture.allOf(futures).thenApply(x -> {

			List<T> concepts = new ArrayList<T>();

			for (CompletableFuture<List<T>> f : futures) {
				List<T> result = f.join();
				if (result != null) {
					concepts.addAll(result);
				}
			}

			return concepts;
		}).exceptionally((error) -> {
			List<T> concepts = new ArrayList<T>();

			for (CompletableFuture<List<T>> f : futures) {
				if (!f.isCompletedExceptionally()) {
					List<T> result = f.join();
					if (result != null) {
						concepts.addAll(result);
					}
				}
			}
			return concepts;
		});
	}

	public abstract class ListSupplier<T> implements Supplier<List<T>> {
		private ApiClient apiClient;

		private void setApiClient(ApiClient apiClient) {
			this.apiClient = apiClient;
		}

		protected ApiClient getApiClient() {
			return this.apiClient;
		}

		@Override
		public abstract List<T> get();
	}
}
