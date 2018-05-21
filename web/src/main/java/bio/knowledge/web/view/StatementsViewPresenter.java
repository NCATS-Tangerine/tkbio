package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.UI;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconConceptsQueryBeaconStatus;
import bio.knowledge.client.model.BeaconConceptsQueryResult;
import bio.knowledge.client.model.BeaconStatement;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.client.model.BeaconStatementsQueryBeaconStatus;
import bio.knowledge.client.model.BeaconStatementsQueryResult;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

/**
 * The behavior logic for StatementsView.
 * @author Colin
 *
 */
public class StatementsViewPresenter {

	private static final long serialVersionUID = -6383744406771442514L;

	private StatementsView statementsView;
	private Set<Object> selected;
	private BeaconConcept currentConcept;
	
	private KnowledgeBeaconService kbService;
	private KBQuery kbQuery;
	
	private List<BeaconConcept> cachedConcepts = new ArrayList<>();
	private Map<String, List<BeaconStatement>> cachedStatements = new ConcurrentHashMap<>();

	private List<BeaconStatementsQuery> cachedQueries = Collections.synchronizedList(new ArrayList<>());
	private List<BeaconStatementsQuery> finishedQueries = Collections.synchronizedList(new ArrayList<>());
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean hasJobStarted = false;
	private List<BeaconStatementsQueryBeaconStatus> beaconStatuses;
	private List<Integer> beacons;
	private ApiCallback<BeaconStatementsQueryResult> cb;
	
	public StatementsViewPresenter(StatementsView statementsView, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		this.statementsView = statementsView;
		this.kbService = kbService;
		this.kbQuery = kbQuery;
		cb = createStatementsCallback();
		
		initConceptsSelect();
		initStatementsSelect();
	}

	/**
	 * Initializes the selection logic for the statements grid
	 */
	private void initStatementsSelect() {
		Grid statementsGrid = statementsView.getStatemtsGrid();
		statementsGrid.addSelectionListener(e -> {
			Button addToGraphBtn = statementsView.getAddToGraphButton();
			selected = e.getSelected();
			if (selected.isEmpty()) {
				addToGraphBtn.setEnabled(false);
			} else {
				addToGraphBtn.setEnabled(true);
			}
		});
	}

	/**
	 * Initializes the selection logic for the search result grid
	 */
	private void initConceptsSelect() {
		Grid conceptsGrid = statementsView.getConceptsGrid();
		conceptsGrid.addSelectionListener(e -> {
			SingleSelectionModel selectModel = (SingleSelectionModel) conceptsGrid.getSelectionModel();
			BeaconConcept selectedConcept = (BeaconConcept) selectModel.getSelectedRow();
			
			/**
			 * Only do something useful when a different concept is selected
			 */
			if (selectedConcept == null || selectedConcept == currentConcept) {
				return;
			}
			
			currentConcept = selectedConcept;
			String sourceClique = currentConcept.getClique();
			/**
			 * Check the cached concepts first; if one is found, then immediately use the cached statements
			 * instead of making another http request.
			 */
			if (cachedConcepts.contains(currentConcept)) {
				if (cachedStatements.containsKey(sourceClique)) {
					List<BeaconStatement> statements = cachedStatements.get(sourceClique);
					setStatementsDataSource(statements);
				}
				statementsView.showProgress();
			} else {
				/**
				 * Not found in cache, so make a new request.
				 */
				statementsView.showProgress();
				cachedConcepts.add(currentConcept);
				BeaconStatementsQuery statementsQuery = kbService.postStatementsQuery(sourceClique, null, null, null, kbQuery.getTypes(), kbQuery.getCustomBeacons());
				cachedQueries.add(statementsQuery);
				if (!hasJobStarted) {
					hasJobStarted = true;
					initPolling();
				}
			}
		});
	}
	
	public void setConceptsDataSource(List<BeaconConcept> results) {
		BeanItemContainer<BeaconConcept> container = new BeanItemContainer<>(BeaconConcept.class, results);
		statementsView.getConceptsGrid().setContainerDataSource(container);
	}
	
	private void setStatementsDataSource(List<BeaconStatement> results) {
		BeanItemContainer<BeaconStatement> container = new BeanItemContainer<>(BeaconStatement.class, results);
		statementsView.getStatemtsGrid().setContainerDataSource(container);
		statementsView.hideProgress();
	}

	private void initPolling() {
		beacons = kbQuery.getCustomBeacons();
		Runnable task = () -> {
			update();
		};
		job = executor.scheduleWithFixedDelay(task, 0, 3, TimeUnit.SECONDS);
	}
	
	private void update() {
		System.out.println("[updating all listeners]");
		synchronized (cachedQueries) {
			for (BeaconStatementsQuery query : cachedQueries) {
				String queryId = query.getQueryId();
				boolean ready = checkStatus(queryId, beacons);
				if (ready) {
					beacons = getResultBeacons(beaconStatuses);
					kbService.getStatementsAsync(queryId, beacons, 1, 100, cb);
					finishedQueries.add(query);
				}
			}
			
			/**
			 * Remove the queries that are ready so requests will not be sent again.
			 */
			cachedQueries.removeAll(finishedQueries);
			if (cachedQueries.isEmpty()) {
				System.out.println("cancelling job!");
				job.cancel(false);
				hasJobStarted = false;
			}
		} 
	}
	
	private boolean checkStatus(String queryId, List<Integer> beacons) {
		beaconStatuses = kbService.getStatementsQueryStatus(queryId, beacons);
		boolean ready = true;
		for (BeaconStatementsQueryBeaconStatus status : beaconStatuses) {
			if (status.getStatus() == HttpStatus.PROCESSING.value()) {
				ready = false;
				break;
			}
		}
		return ready;
	}
	
	private List<Integer> getResultBeacons(List<BeaconStatementsQueryBeaconStatus> beaconStatuses) {
		List<Integer> beacons = new ArrayList<>();
		for (BeaconStatementsQueryBeaconStatus beaconStatus : beaconStatuses) {
			int status = beaconStatus.getStatus();
			if (status == HttpStatus.OK.value() || status == HttpStatus.CREATED.value()) {
				beacons.add(beaconStatus.getBeacon());
			}
		}
		return beacons;
	}
	
	private ApiCallback<BeaconStatementsQueryResult> createStatementsCallback() {
		return new ApiCallback<BeaconStatementsQueryResult>() {

			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				
			}

			@Override
			public void onSuccess(BeaconStatementsQueryResult result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				BeaconStatementsQuery query = null;
				synchronized (finishedQueries) {
					for (BeaconStatementsQuery q : finishedQueries) {
						if (q.getQueryId().equals(result.getQueryId())) {
							query = q;
							break;
						}
					}
				}
				String cliqueId = query.getSource();
				cachedStatements.put(cliqueId, result.getResults());
				finishedQueries.remove(query);
				
				if (cliqueId.equals(currentConcept.getClique())) {
					statementsView.getUI().access(() -> {
						setStatementsDataSource(result.getResults());
					});
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
	}
}
