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

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

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

	private KnowledgeBeaconService kbService;
	private KBQuery kbQuery;
	
	private static final String SUBJECT_ID = "Subject";
	private static final String PREDICATE_ID = "Predicate";
	private static final String OBJECT_ID = "Object";
	private static final Object STMT_ID = "Id";
	private static final Object DETAILS_ID = "Relationship Details";
	
	private StatementsView statementsView;
	private Collection<Object> selectedItemIds;
	private BeaconConcept currentConcept;
	
	private List<BeaconConcept> cachedConcepts = new ArrayList<>();
	private Map<BeaconConcept, List<BeaconStatement>> cachedStatements = new ConcurrentHashMap<>();
	private List<QueryPollingListener> listeners = Collections.synchronizedList(new ArrayList<>());
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> job;
	private boolean hasJobStarted = false;
	
	private DesktopView view;
	
	public StatementsViewPresenter(DesktopView view, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		this.view = view;
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
		statementsView = new StatementsView(results);
		replaceViewContent(statementsView);
		view.setSelectedTab(view.getConceptsTab());
		initListeners();
	}

	private void replaceViewContent(StatementsView newContent) {
		view.getDataResults().removeAllComponents();
		view.getDataResults().addComponent(newContent);
	}

	private void clearCache() {
		currentConcept = null;
		cachedConcepts.clear();
		cachedStatements.clear();
		listeners.clear();
	}
	
	public KnowledgeBeaconService getKbService() {
		return kbService;
	}

	/**
	 * initializes click listeners for: tab switching, concepts grid details and statements columns, and the statements grid add to graph button
	 */
	private void initListeners() {
		initTabListener();
		initStatementsListener();
		initConceptDetailsListener();
		initAddToGraphListener();
	}
	
	/**
	 * Logic for switching tabs: moves horizontal split in appropriate position to make either the concepts or statements grid visible.
	 * Since the grids are not removed, the scroll position should be maintained. 
	 */
	private void initTabListener() {
		view.getTabSheet().addSelectedTabChangeListener(e -> {
			Component tab = view.getTabSheet().getSelectedTab();
			if (tab == view.getConceptsTab()) {
				statementsView.showConceptsResults();
			} else if (tab == view.getStatementsTab()) {
				statementsView.showStatementsResults();
			}
		});
	}

	/**
	 * Initiates POST /statements request on selected concept if concept is not the previously selected concept and not in cached concepts
	 */
	private void initStatementsListener() {
		ButtonRenderer statementsButton = new ButtonRenderer(e -> {
			Object selected = e.getItemId();
			BeaconConcept selectedConcept = (BeaconConcept) selected;
			if (selectedConcept == currentConcept) {
				statementsView.showStatementsResults();
				view.setSelectedTab(view.getStatementsTab());
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
				}
			} else {
				/**
				 * Not found in cache, so make a new request.
				 */
				statementsView.showProgress();
				view.setSelectedTab(view.getStatementsTab());
				cachedConcepts.add(currentConcept);
				BeaconStatementsQuery statementsQuery = kbService.postStatementsQuery(currentConcept.getClique(), null, null, null, kbQuery.getTypes(), kbQuery.getCustomBeacons());
				QueryPollingListener listener = new StatementsQueryListener(statementsQuery, kbQuery.getCustomBeacons(), this);
				listeners.add(listener);
				if (!hasJobStarted) {
					hasJobStarted = true;
					initPolling();
				}
			}
			statementsView.setStatementsTitle(currentConcept);
		});
		
		statementsView.getConceptsGrid().getColumn(StatementsView.STATEMENTS_ID).setRenderer(statementsButton);
	}
	private void initConceptDetailsListener() {
		Grid conceptsGrid = statementsView.getConceptsGrid();
		ButtonRenderer detailsButton = new ButtonRenderer(e -> {
			Window window = new ConceptDetailsWindow(e, kbService, kbQuery);
			conceptsGrid.getUI().addWindow(window);
		});

		conceptsGrid.getColumn(StatementsView.CONCEPTS_NAME_ID).setRenderer(detailsButton);		
	}
	
	/**
	 * Adds selected statements to graph on button click
	 */
	private void initAddToGraphListener() {
		Grid statemtsGrid = statementsView.getStatemtsGrid();
		statemtsGrid.addSelectionListener(e -> {
			selectedItemIds = statemtsGrid.getSelectedRows();
		});
		
		statementsView.getAddToGraphButton().addClickListener(e -> {
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

	/**
	 * Sets statements grid with given results, adding an additional column to initiate harvesting of 
	 * statement details and details pop-up window
	 * @param results
	 */
	private void setStatementsDataSource(List<BeaconStatement> results) {
		Grid grid = statementsView.getStatemtsGrid();
		grid.setContainerDataSource(getStatementsContainer(results));
		
		statementsView.hideProgress();
		if (!results.isEmpty()) {
			Indexed container = grid.getContainerDataSource();
			grid.setColumnOrder(DETAILS_ID);
			
			ButtonRenderer detailsButton = new ButtonRenderer(e -> {
				String id = (String) container.getContainerProperty(e.getItemId(), STMT_ID).getValue();
				Window window = new StatementDetailsWindow(id, kbService, kbQuery);
				statementsView.getUI().addWindow(window);
			});
			grid.getColumn(DETAILS_ID).setRenderer(detailsButton);
			
			IdentifiedConceptToStringConverter converter = new IdentifiedConceptToStringConverter();
			
			ButtonRenderer subjectButton = new ButtonRenderer(e -> {
				IdentifiedConcept concept = (IdentifiedConcept) container.getContainerProperty(e.getItemId(), SUBJECT_ID).getValue();
				Window window = new ConceptDetailsWindow(concept, kbService, kbQuery);
				statementsView.getUI().addWindow(window);
			});
			grid.getColumn(SUBJECT_ID).setConverter(converter);
			grid.getColumn(SUBJECT_ID).setRenderer(subjectButton);
			
			ButtonRenderer objectButton = new ButtonRenderer(e -> {
				IdentifiedConcept concept = (IdentifiedConcept) container.getContainerProperty(e.getItemId(), OBJECT_ID).getValue();
				Window window = new ConceptDetailsWindow(concept, kbService, kbQuery);
				statementsView.getUI().addWindow(window);
			});
			grid.getColumn(OBJECT_ID).setConverter(converter);
			grid.getColumn(OBJECT_ID).setRenderer(objectButton);
			

			grid.getColumn(STMT_ID).setHidden(true);			
		}
		
		view.setSelectedTab(view.getStatementsTab());		
	}

	/**
	 * Creates statements container with appropriate properties, from results
	 * @param results
	 * @return
	 */
	private IndexedContainer getStatementsContainer(List<BeaconStatement> results) {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(STMT_ID, String.class, "");
		container.addContainerProperty(SUBJECT_ID, IdentifiedConcept.class, "");
		container.addContainerProperty(PREDICATE_ID, Predicate.class, "");
		container.addContainerProperty(OBJECT_ID, IdentifiedConcept.class, "");
		container.addContainerProperty(DETAILS_ID, String.class, "show details");
		
		for (BeaconStatement beaconStatemt : results) {			
			BeaconStatementSubject beaconSubject = beaconStatemt.getSubject();
			IdentifiedConcept subject = new IdentifiedConceptImpl(beaconSubject.getClique(), beaconSubject.getName(), beaconSubject.getCategories());

			BeaconStatementPredicate beaconPredicate = beaconStatemt.getPredicate();
			Predicate predicate = new PredicateImpl(beaconPredicate.getEdgeLabel());
			
			BeaconStatementObject beaconObject = beaconStatemt.getObject();
			IdentifiedConcept object = new IdentifiedConceptImpl(beaconObject.getClique(), beaconObject.getName(), beaconObject.getCategories());

			Object itemId = container.addItem();
			container.getContainerProperty(itemId, STMT_ID).setValue(beaconStatemt.getId());
			container.getContainerProperty(itemId, SUBJECT_ID).setValue(subject);
			container.getContainerProperty(itemId, PREDICATE_ID).setValue(predicate);
			container.getContainerProperty(itemId, OBJECT_ID).setValue(object);
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
				listener.update();
				if (listener.isDone()) {
					completedListeners.add(listener);
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
	
	@SuppressWarnings("unused")
	@Deprecated
	// Old method for initializing statement harvest on click of whole row
	private void initStatementsListenerOnRow() {
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
	}
}