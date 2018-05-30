package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconStatement;
import bio.knowledge.client.model.BeaconStatementObject;
import bio.knowledge.client.model.BeaconStatementPredicate;
import bio.knowledge.client.model.BeaconStatementSubject;
import bio.knowledge.client.model.BeaconStatementsQuery;
import bio.knowledge.model.GeneralStatement;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.IdentifiedConceptImpl;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.PredicateImpl;
import bio.knowledge.model.Statement;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.components.QueryPollingListener;
import bio.knowledge.web.view.components.StatementsQueryListener;

/**
 * The behavior logic for StatementsView.
 * @author Colin
 *
 */

public class StatementsViewPresenter {

	private static final long serialVersionUID = -6383744406771442514L;

	private KnowledgeBeaconService kbService;
	private KBQuery kbQuery;
	
	private final String SUBJECT_ID = "Subject";
	private final String PREDICATE_ID = "Predicate";
	private final String OBJECT_ID = "Object";
	private final String BEACON_ID = "Beacon";
	
	private StatementsView statementsView;
	private Collection<Object> selectedItemIds;
	private BeaconConcept currentConcept;
	
	private List<BeaconConcept> cachedConcepts = new ArrayList<>();
	private Map<BeaconConcept, List<BeaconStatement>> cachedStatements = new ConcurrentHashMap<>();
	private List<QueryPollingListener> listeners = Collections.synchronizedList(new ArrayList<>());
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean hasJobStarted = false;
	
	private VerticalLayout statementsTab;

	public StatementsViewPresenter(VerticalLayout statementsTab, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		this.statementsTab = statementsTab;
		this.kbService = kbService;
		this.kbQuery = kbQuery;
	}
	
	public BeaconConcept getCurrentConcept() {
		return currentConcept;
	}
	
	public void addStatements(BeaconConcept sourceConcept, List<BeaconStatement> statements) {
		cachedStatements.put(sourceConcept, statements);
		if (currentConcept.equals(sourceConcept)) {
			UI.getCurrent().access(() -> {				
				setStatementsDataSource(statements);
			});
		}
	}
	
	public void setConceptsDataSource(List<BeaconConcept> results) {
		/**
		 * Getting a potentially new results, so removed the old cached data.
		 */
		clearCache();
		BeanItemContainer<BeaconConcept> container = new BeanItemContainer<>(BeaconConcept.class, results);
		statementsView = new StatementsView(container);
		replaceViewContent(statementsView);
		initGrid();
	}

	private void replaceViewContent(VerticalLayout newContent) {
		statementsTab.removeAllComponents();
		statementsTab.addComponent(newContent);
	}

	private void clearCache() {
		cachedConcepts.clear();
		cachedStatements.clear();
		listeners.clear();
	}
	
	public KnowledgeBeaconService getKbService() {
		return kbService;
	}

	private void initGrid() {
		statementsView.getConceptsGrid().addSelectionListener(e -> {
			Set<Object> selected = e.getAdded();
			if (selected == null) {
				return;
			}
			BeaconConcept selectedConcept = (BeaconConcept) selected.iterator().next();
			if (selectedConcept == currentConcept) {
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
		
		statementsView.getAddToGraphButton().addClickListener(e -> {
			Grid statemtsGrid = statementsView.getStatemtsGrid();
			Indexed container = statemtsGrid.getContainerDataSource();
			for (Object itemId : selectedItemIds) {
				IdentifiedConcept subject = (IdentifiedConcept) container.getContainerProperty(itemId, SUBJECT_ID).getValue();
				IdentifiedConcept object = (IdentifiedConcept) container.getContainerProperty(itemId, OBJECT_ID).getValue();
				Predicate predicate = (Predicate) container.getContainerProperty(itemId, PREDICATE_ID).getValue();
				Statement statement = new GeneralStatement("", subject, predicate, object);
				
				DesktopUI ui = (DesktopUI) statementsView.getUI();
				ui.addNodeToConceptMap(subject);
				ui.addNodeToConceptMap(object);
				ui.addEdgeToConceptMap(statement);
			}
			statemtsGrid.deselectAll();
		});
	}

	private void setStatementsDataSource(List<BeaconStatement> results) {
		statementsView.getStatemtsGrid().setContainerDataSource(getStatementsContainer(results));
		statementsView.hideProgress();
	}

	private IndexedContainer getStatementsContainer(List<BeaconStatement> results) {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(SUBJECT_ID, IdentifiedConcept.class, "");
		container.addContainerProperty(PREDICATE_ID, Predicate.class, "");
		container.addContainerProperty(OBJECT_ID, IdentifiedConcept.class, "");
		container.addContainerProperty(BEACON_ID, Integer.class, "");
		
		for (BeaconStatement beaconStatemt : results) {			
			BeaconStatementSubject beaconSubject = beaconStatemt.getSubject();
			IdentifiedConcept subject = new IdentifiedConceptImpl(beaconSubject.getClique(), beaconSubject.getName(), "");

			
			BeaconStatementPredicate beaconPredicate = beaconStatemt.getPredicate();
			Predicate predicate = new PredicateImpl(beaconPredicate.getEdgeLabel());
			
			BeaconStatementObject beaconObject = beaconStatemt.getObject();
			IdentifiedConcept object = new IdentifiedConceptImpl(beaconObject.getClique(), beaconObject.getName(), "");

			Object itemId = container.addItem();
			container.getContainerProperty(itemId, SUBJECT_ID).setValue(subject);
			container.getContainerProperty(itemId, PREDICATE_ID).setValue(predicate);
			container.getContainerProperty(itemId, OBJECT_ID).setValue(object);
			container.getContainerProperty(itemId, BEACON_ID).setValue(beaconStatemt.getBeacon());
		}
		
		return container;
	}
	
	private void initPolling() {
		Runnable task = () -> {
			update();
		};
		job = executor.scheduleWithFixedDelay(task, 0, 3, TimeUnit.SECONDS);
	}
	
	private void update() {		
		if (listeners.isEmpty()) {
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
	
	public void shutDown() {
		if (!job.isCancelled()) {			
			job.cancel(true);
		}
		executor.shutdown();
	}
}
