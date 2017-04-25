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

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2001DataPage;
import bio.knowledge.model.SemanticGroup;
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

	public CompletableFuture<ResultSet> query(
			List<String> filters,
			List<String> semanticGroups,
			int pageNumber,
			int pageSize
	) {
		
		Set<ApiClient> apiClients = getDataSource().getApiClients();
		
		CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(new Supplier<ResultSet>() {

			@Override
			public ResultSet get() {
				ResultSet resultSet = new SimpleResultSet();
				
				for (ApiClient apiClient : apiClients) {
					conceptsApi.setApiClient(apiClient);
					
					try {
						conceptsApi.getConceptsAsync(filters, semanticGroups, pageNumber, pageSize, new Callback(resultSet));
					} catch (ApiException e) {
						e.printStackTrace();
					}
				}
				
				return resultSet;
			}
			
		});
		
		return future;
	}
	
	public CompletableFuture<ResultSet> query2(List<String> filters, List<String> semanticGroups, int pageNumber,
			int pageSize) {
		Set<ApiClient> apiClients = getDataSource().getApiClients();
		
		List<CompletableFuture<ResultSet>> futures = new ArrayList<CompletableFuture<ResultSet>>();

		for (ApiClient apiClient : apiClients) {
			CompletableFuture<ResultSet> future;
			future = runQuery(apiClient, filters, semanticGroups, pageNumber, pageSize);
			futures.add(future);
		}
		
		CompletableFuture<ResultSet> combinedFuture = CompletableFuture.supplyAsync(new Supplier<ResultSet>() {

			@Override
			public ResultSet get() {
				ResultSet resultSet = new SimpleResultSet();
				
				for (CompletableFuture<ResultSet> future : futures) {
					try {
						ResultSet results = future.get(DataService.TIMEOUT_DURATION, DataService.TIMEOUT_UNIT);
						resultSet.addAll(results);
						
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						future.completeExceptionally(e);
					}
				}
				
				return resultSet;
			}
			
		});

		return combinedFuture;
	}
	
	private CompletableFuture<ResultSet> runQuery(ApiClient apiClient, List<String> filters,
			List<String> semanticGroups, int pageNumber, int pageSize) {
		CompletableFuture<ResultSet> future = CompletableFuture.supplyAsync(new Supplier<ResultSet>() {

			@Override
			public ResultSet get() {
				conceptsApi.setApiClient(apiClient);

				ResultSet resultSet = new SimpleResultSet();

				try {
					InlineResponse2001 response = conceptsApi.getConcepts(filters, semanticGroups, pageNumber,
							pageSize);
					List<InlineResponse2001DataPage> dataPages = response.getDataPage();

					for (InlineResponse2001DataPage dataPage : dataPages) {
						Result result = new SimpleResult();

						result.put("concept id", dataPage.getId());
						result.put("concept name", dataPage.getName());
						result.put("concept description", dataPage.getDefinition());
						result.put("semantic group", dataPage.getSemanticGroup());
						result.put("concept synonoms", dataPage.getSynonyms());

						resultSet.add(result);
					}

				} catch (ApiException e) {
					e.printStackTrace();
				}

				return resultSet;
			}

		});

		return future;
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
		// TODO Auto-generated method stub
		return false;
	}
	
	private class Callback implements ApiCallback<InlineResponse2001> {
		
		private final ResultSet resultSet;
		
		public Callback(ResultSet resultSet) {
			this.resultSet = resultSet;
		}

		@Override
		public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(InlineResponse2001 response, int statusCode, Map<String, List<String>> responseHeaders) {
			List<InlineResponse2001DataPage> dataPages = response.getDataPage();

			for (InlineResponse2001DataPage dataPage : dataPages) {
				Result result = new SimpleResult();

				result.put("concept id", dataPage.getId());
				result.put("concept name", dataPage.getName());
				result.put("concept description", dataPage.getDefinition());
				result.put("semantic group", dataPage.getSemanticGroup());
				result.put("concept synonoms", dataPage.getSynonyms());

				resultSet.add(result);
			}
		}

		@Override
		public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
			// TODO Auto-generated method stub
			
		}

	}
}
