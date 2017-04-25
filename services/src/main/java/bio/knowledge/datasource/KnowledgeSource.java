package bio.knowledge.datasource;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.client.model.InlineResponse2001DataPage;
import bio.knowledge.datasource.rdf.SPARQLDataService;
import bio.knowledge.datasource.wikidata.WikiDataDataSource;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.datasource.Result;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.model.datasource.SimpleResult;
import bio.knowledge.model.datasource.SimpleResultSet;

public class KnowledgeSource extends AbstractDataSource {
	public final String BASE_URL;
	
	private static Logger _logger = LoggerFactory.getLogger(KnowledgeSource.class);
	
	private ApiClient apiClient;
	
	private ConceptsApi conceptsApi = new ConceptsApi();

	public KnowledgeSource(String id, String name, String baseUrl) {
		super(id, name);
		
		BASE_URL = baseUrl;

		apiClient = new ApiClient();
		apiClient.setBasePath(baseUrl);
		
		conceptsApi.setApiClient(apiClient);
	}

	protected Set<ApiClient> getApiClients() {
		Set<ApiClient> set = new HashSet<ApiClient>();
		set.add(apiClient);
		return set;
	}

	interface BaseDataService extends DataService {

		default void addGeneralMetaData(AbstractDescribed self) {
			final String author = "Authors: ";
			self.addMetaData(author, "Richard Bruskiewich");
		}

		default ResultSet runQuery(SPARQLDataService sparqlService, Map<String, Object> parameters) {
			ResultSet resultSet = new SimpleResultSet();
			try {
				sparqlService.setupQuery(parameters);

				sparqlService.execSelect();

				List<Map<String, String>> results = sparqlService.getResultList();

				for (Map<String, String> item : results) {
					Result result = new SimpleResult();
					result.putAll(item);
					resultSet.add(result);
				}
			} catch (Exception e) {
				_logger.error(e.getMessage());
			} finally {
				if (sparqlService != null) {
					sparqlService.close();
				}
			}
			return resultSet;
		}
	}

	// Wrapper for implementing WikiData ComplexDataService
	class GetConceptDataService extends AbstractComplexDataService implements BaseDataService {
		public static final String ID = "conceptDataService";
		/**
		 * 
		 * @param dataSource
		 * @param serviceId
		 * @param targetSemanticGroup
		 * @param name
		 * @param sparqlQuery
		 */
		protected GetConceptDataService(DataSource dataSource) {
			super(dataSource, ID, SemanticGroup.ANY, dataSource.getName() + "." + ID);

			addGeneralMetaData(this);
		}
		
		@Override
		public CompletableFuture<ResultSet> query(Map<String, Object> parameters) throws IllegalArgumentException {
			throw new RuntimeException("Not implemented");
		}
		
		public CompletableFuture<ResultSet> query(
				List<String> filters,
				List<String> semanticGroups,
				int pageNumber,
				int pageSize
			) throws IllegalArgumentException {
			ResultSet resultSet = new SimpleResultSet();
			
			ApiCallback<InlineResponse2001> conceptCallback = new ApiCallback<InlineResponse2001>() {

				@Override
				public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(InlineResponse2001 result, int statusCode,
						Map<String, List<String>> responseHeaders) {
					List<InlineResponse2001DataPage> dataPages = result.getDataPage();
					for (InlineResponse2001DataPage dataPage : dataPages) {
						Result item = new SimpleResult();
						item.put("concept id", dataPage.getId());
						item.put("concept name", dataPage.getName());
						item.put("concept description", dataPage.getDefinition());
						item.put("semantic group", dataPage.getSemanticGroup());
						item.put("concept synonoms", dataPage.getSynonyms());
						
						resultSet.add(item);
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
			};
			
			try {
				conceptsApi.getConceptsAsync(filters, semanticGroups, pageNumber, pageSize, conceptCallback);
			} catch (ApiException e) {
				e.printStackTrace();
			}
			
			CompletableFuture<ResultSet> future;
			future = CompletableFuture.supplyAsync(new Supplier<ResultSet>() {

				@Override
				public ResultSet get() {
					
					return resultSet;
				}
				
			});
			
			return future;
		}
		
		
	}

	@Override
	protected void initialize() {
		addDataService(new GetConceptDataService(this));
	}
}
