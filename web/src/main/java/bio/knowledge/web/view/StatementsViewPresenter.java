package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SingleSelectionModel;

import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconStatement;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.view.components.QueryPollingListener;
import bio.knowledge.web.view.components.StatementsQueryListener;

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
	private Map<BeaconConcept, List<BeaconStatement>> cachedStatements = new ConcurrentHashMap<>();
	private List<QueryPollingListener> listeners = Collections.synchronizedList(new ArrayList<>());
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean hasJobStarted = false;
	private List<Integer> beacons;
	
	public StatementsViewPresenter(StatementsView statementsView, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		this.statementsView = statementsView;
		this.kbService = kbService;
		this.kbQuery = kbQuery;
		
		initConceptsSelect();
		initStatementsSelect();
	}

	public BeaconConcept getCurrentConcept() {
		return currentConcept;
	}
	
	public void addStatements(BeaconConcept sourceConcept, List<BeaconStatement> statements) {
		cachedStatements.put(sourceConcept, statements);
		if (currentConcept.equals(sourceConcept)) {
			statementsView.getUI().access(() -> {				
				setStatementsDataSource(statements);
			});
		}
	}
	
	public void setConceptsDataSource(List<BeaconConcept> results) {
		/**
		 * Getting a potentially new results, so removed the old cached ones.
		 */
		cachedConcepts.clear();
		cachedStatements.clear();
		listeners.clear();
		
		BeanItemContainer<BeaconConcept> container = new BeanItemContainer<>(BeaconConcept.class, results);
		statementsView.getConceptsGrid().setContainerDataSource(container);
	}
	
	public KnowledgeBeaconService getKbService() {
		return kbService;
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
			/**
			 * Check the cached concepts first; if one is found, then immediately use the cached statements
			 * instead of making another http request.
			 */
			if (cachedConcepts.contains(currentConcept)) {
				if (cachedStatements.containsKey(currentConcept)) {
					List<BeaconStatement> statements = cachedStatements.get(currentConcept);
					setStatementsDataSource(statements);
				} else {					
					statementsView.showProgress();
				}
			} else {
				/**
				 * Not found in cache, so make a new request.
				 */
				statementsView.showProgress();
				cachedConcepts.add(currentConcept);
				BeaconStatementsQuery statementsQuery = kbService.postStatementsQuery(currentConcept.getClique(), null, null, null, kbQuery.getTypes(), kbQuery.getCustomBeacons());
				QueryPollingListener listener = new StatementsQueryListener(statementsQuery, kbQuery.getCustomBeacons(), this);
				listeners.add(listener);
				if (!hasJobStarted) {
					hasJobStarted = true;
					initPolling();
				}
			}
		});
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
		
		if (listeners.isEmpty()) {
			System.out.println("cancelling job!");
			job.cancel(true);
			hasJobStarted = false;
		}
		
		synchronized(listeners) {
			List<QueryPollingListener> completedListeners = new ArrayList<>();
			for (QueryPollingListener listener : this.listeners) {
				if (listener.isDone()) {
					completedListeners.add(listener);
				} else {
					listener.update();
				}
			}
			listeners.removeAll(completedListeners);
		}
	}	
}
