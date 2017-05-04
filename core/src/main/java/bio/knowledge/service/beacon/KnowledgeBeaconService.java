package bio.knowledge.service.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.client.ApiException;
import bio.knowledge.client.api.ConceptsApi;
import bio.knowledge.client.api.EvidenceApi;
import bio.knowledge.client.api.ExactmatchesApi;
import bio.knowledge.client.api.StatementsApi;
import bio.knowledge.client.model.InlineResponse200;
import bio.knowledge.client.model.InlineResponse2001;
import bio.knowledge.model.ConceptImpl;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.Statement;

/**
 * 
 * @author Lance Hannestad
 * 
 *         It may seem wasteful to instantiate a new {@code ConceptApi} (or
 *         other API classes) within each {@code ListSupplier<T>}, but in fact
 *         it is necessary because we're asynchrounously setting their ApiClient
 *         objects (which encapsulate the URI to be queried) in
 *         {@code GenericDataService}.
 *
 */
@Service
public class KnowledgeBeaconService extends GenericKnowledgeService {

	public CompletableFuture<List<ConceptImpl>> getConcepts(
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) {
		return query(buildConceptSupplier(
				keywords,
				semanticGroups,
				pageNumber,
				pageSize
		));
	}
	
	public CompletableFuture<List<Statement>> getStatements() {
		null;
	}
	
	public CompletableFuture<List<ConceptImpl>> getConceptDetails(String conceptId) {
		return query(buildConceptDetailsSupplier(conceptId));
	}
	
	private ListSupplier<ConceptImpl> buildConceptSupplier(
			String keywords,
			String semanticGroups,
			int pageNumber,
			int pageSize
		) {
		return new ListSupplier<ConceptImpl>() {

			@Override
			public List<ConceptImpl> get() {
				ConceptsApi conceptsApi = new ConceptsApi(getApiClient());
				
				try {
					List<InlineResponse2001> responses =
							conceptsApi.getConcepts(keywords, semanticGroups, pageNumber, pageSize);
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					for (InlineResponse2001 response : responses) {
						SemanticGroup semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						ConceptImpl concept = new ConceptImpl(
								response.getId(),
								semgroup,
								response.getName()
						);
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						
						concepts.add(concept);
					}
					return concepts;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
	
	private ListSupplier<ConceptImpl> buildConceptDetailsSupplier(String conceptId) {
		return new ListSupplier<ConceptImpl>() {

			@Override
			public List<ConceptImpl> get() {
				ConceptsApi conceptsApi = new ConceptsApi(getApiClient());
				
				try {
					List<InlineResponse200> responses = conceptsApi.getConceptDetails(conceptId);
					List<ConceptImpl> concepts = new ArrayList<ConceptImpl>();
					
					for (InlineResponse200 response : responses) {
						SemanticGroup semgroup = SemanticGroup.valueOf(response.getSemanticGroup());
						ConceptImpl concept = new ConceptImpl(
								response.getId(),
								semgroup,
								response.getName()
						);
						
						concept.setSynonyms(String.join(" ", response.getSynonyms()));
						concept.setDescription(response.getDefinition());
						
						concepts.add(concept);
					}
					
					return concepts;
					
				} catch (ApiException e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		};
	}
	
//	private ListSupplier<Statement> buildStatementsSupplier(String emci, ) {
//		return new ListSupplier<Statement>() {
//
//			@Override
//			public List<Statement> get() {
//				StatementsApi statementsApi = new StatementsApi(getApiClient());
//				
//				statementsApi.getStatements(emci, pageNumber, pageSize, keywords, semgroups);
//				return null;
//			}
//			
//		};
//	}
}
