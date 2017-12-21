/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-17 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.web.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.AbstractSplitPanel.SplitPositionChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.graph.ConceptMapDisplay;
import bio.knowledge.graph.jsonmodels.Edge;
import bio.knowledge.graph.jsonmodels.EdgeData;
import bio.knowledge.graph.jsonmodels.Layout;
import bio.knowledge.graph.jsonmodels.Node;
import bio.knowledge.graph.jsonmodels.NodeData;
import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.ConceptType;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.Statement;
import bio.knowledge.model.user.User;
import bio.knowledge.model.util.Util;
import bio.knowledge.service.AuthenticationState;
import bio.knowledge.service.Cache;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.KBQuery.LibrarySearchMode;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.service.core.MessageService;
import bio.knowledge.service.user.UserService;
import bio.knowledge.web.KBUploader;
import bio.knowledge.web.view.AboutView;
import bio.knowledge.web.view.ApplicationLayout;
import bio.knowledge.web.view.ConceptSearchResults;
import bio.knowledge.web.view.ContactView;
import bio.knowledge.web.view.DesktopView;
import bio.knowledge.web.view.FaqView;
import bio.knowledge.web.view.LibrarySearchResults;
import bio.knowledge.web.view.ListView;
import bio.knowledge.web.view.LoginView;
import bio.knowledge.web.view.PasswordResetView;
import bio.knowledge.web.view.ReferenceView;
import bio.knowledge.web.view.Registry;
import bio.knowledge.web.view.ViewName;
import bio.knowledge.web.view.components.KnowledgeBeaconWindow;
import bio.knowledge.web.view.components.LibraryDetails;
import bio.knowledge.web.view.components.SaveWindow;
import bio.knowledge.web.view.components.UserDetails;

/**
 * The main UI (homepage)
 * 
 * @author Farzin Ahmed
 * @author Richard Bruskiewich
 * @author Lance Hannestead
 * @author Rudy Kong
 * @author Yinglun Colin Qiao
 * @author Chandan Mishra
 * @author Kenneth Bruskiewicz
 */
@SpringUI
@Title("Knowledge.Bio")
@Theme("kb2")
@com.vaadin.annotations.JavaScript({ "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0-beta1/jquery.min.js",
		"https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js" })
@PreserveOnRefresh
@Widgetset("bio.knowledge.renderer.ButtonRendererWidgetset")
public class DesktopUI extends UI implements MessageService, Util {

	// private static final int DAY_IN_SECOND = 86400;

	// private static final String INDEX_COOKIE = "indexCookie";

	private static final String FIELD_ERROR = "Unknown";

	private static final long serialVersionUID = -7147784018127550717L;

	private Logger _logger = LoggerFactory.getLogger(DesktopUI.class);
	
	@Autowired
	Registry registry;
	
	@Autowired
	UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private KBQuery query;

	@Autowired
	Cache cache;
	
	@Autowired
	KnowledgeBeaconService kbService;
	
	@Autowired
	ConceptService conceptService;
	
	/**
	 * 
	 * @return
	 */
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Autowired
	private AuthenticationState authenticationState;

	public AuthenticationState getAuthenticationState() {
		return authenticationState;
	}
	
	@ Autowired
	KnowledgeBeaconRegistry beaconRegistry;

	@Autowired
	private SpringViewProvider viewProvider;

	/**
	 * 
	 * @return
	 */
	public SpringViewProvider getViewProvider() {
		return viewProvider;
	}

	/**
	 * 
	 * @param navigationState
	 */
	public void navigateTo(String navigationState) {
		getNavigator().navigateTo(navigationState);
	}

	@Autowired
	private MessageSource messageSource;
	
	private boolean zoomEnabled;
	
	public static DesktopUI getCurrent() {
		return (DesktopUI) UI.getCurrent();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.MessageService#getMessage(java.lang.String)
	 */
	public String getMessage(String id) {

		if (nullOrEmpty(id))
			throw new NoSuchMessageException("ERROR: Null or empty getMessage id?");

		Locale locale = this.getLocale();
		String msg = "";
		try {
			msg = messageSource.getMessage(id, null, locale);
		} catch (NoSuchMessageException nsme) {
			msg = messageSource.getMessage(id, null, new Locale("en"));
		}
		return msg;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bio.knowledge.service.core.MessageService#getMessage(java.lang.String,
	 * java.lang.String)
	 */
	public String getMessage(String id, String tag) {

		if (nullOrEmpty(id))
			throw new NoSuchMessageException("ERROR: Null or empty getMessage id?");

		if (nullOrEmpty(tag))
			throw new NoSuchMessageException("ERROR: Null or empty getMessage tag?");

		Locale locale = getLocale();

		String msg = "";
		try {
			// Resolve on level of language only for now
			// System.err.println("### Getting '"+locale.getLanguage()+"'
			// version of message "+id);
			msg = messageSource.getMessage(id, new Object[] { tag }, locale);
		} catch (NoSuchMessageException nsme) {
			// System.err.println("### Getting 'en' version of message "+id);
			msg = messageSource.getMessage(id, new Object[] { tag }, new Locale("en"));
		}
		return msg;
	}

	private Optional<IdentifiedConcept> currentConcept;

	/**
	 * 
	 * @return
	 */
	public IdentifiedConcept getCurrentConcept() {
		return currentConcept.get();
	}

	private DesktopView desktopView = new DesktopView();

	/**
	 * 
	 * @return
	 */
	public DesktopView getDesktop() {
		return desktopView;
	}

	@Autowired
	KnowledgeBeaconRegistry kbRegistry;
	
	public void openKnowledgeBeaconWindow() {
		KnowledgeBeaconWindow kbWindow = new KnowledgeBeaconWindow(kbRegistry, query, kbService);
		this.addWindow(kbWindow);
	}
	
	/**
	 * 
	 * @return the current ConceptMapDisplay
	 */
	private ConceptMapDisplay cm = null;

	public ConceptMapDisplay getConceptMap() {
		if(cm == null)
			cm = new ConceptMapDisplay(conceptService);
		return cm;
	}

	/**
	 * 
	 * @return true if ConceptMap contains any elements
	 */
	public boolean conceptMapIsEmpty() {
		return getConceptMap().isEmpty();
	}

	static final String DEFAULT_CM_LAYOUT = "Breadth First";
	static final String DEFAULT_CM_COLOR = "Dark";
	public static final String MANUAL_CM_LAYOUT = "Manual";

	/**
	 * 
	 * @param concept
	 */
	public void addNodeToConceptMap(IdentifiedConcept concept) {
		getConceptMap().addNodeToConceptMap(concept);
	}

	/**
	 * 
	 * @param sourceNode
	 * @param targetNode
	 */
	public void addEdgeToConceptMap(Statement statement) {
		getConceptMap().getElements().getEdges()
				.addEdge(new Edge(statement));
	}

	@Autowired
	private ConceptMapPopupWindow popUp;

	/**
	 * 
	 * @return
	 */
	public ConceptMapPopupWindow getPredicatePopupWindow() {
		return this.popUp;
	}

	private Window conceptSearchWindow = null;

	/**
	 * 
	 * @return
	 */
	public Window getConceptSearchWindow() {
		return this.conceptSearchWindow;
	}

	/**
	 * 
	 * @param newConceptSearchWindow
	 */
	public void setConceptSearchWindow(Window newConceptSearchWindow) {
		this.conceptSearchWindow = newConceptSearchWindow;
	}

	/**
	 * 
	 */
	public void closeConceptSearchWindow() {
		if (conceptSearchWindow != null) {
			conceptSearchWindow.close();
			conceptSearchWindow = null;
		}
	}
	
	public void processConceptSearch(IdentifiedConcept concept) {
		
		addNodeToConceptMap(concept);

		queryUpdate(concept, RelationSearchMode.RELATIONS);

		// 27-Oct-2016: Hack to reverse a side-effect of
		// queryUpdate's setConceptInSession's highlighting code
		String accessionId = concept.getId();
		Node node = getConceptMap().getElements().getNodes().getNodeById(accessionId);
		if (node != null)
			node.getData().setState("add");	
	}

	private Window conceptMapLibraryWindow = null;

	/**
	 * 
	 */
	public void closeLibraryWindow() {
		if (conceptMapLibraryWindow != null) {
			conceptMapLibraryWindow.close();
			conceptMapLibraryWindow = null;
		}
	}

	private PopupWindow conceptSemanticWindow = null;

	/**
	 * 
	 */
	public void closeConceptSemanticWindow() {
		if (conceptSemanticWindow != null) {
			conceptSemanticWindow.close();
			conceptSemanticWindow = null;
		}
	}

	/**
	 * 
	 */
	public void gotoStatementsTable() {
		
		VerticalLayout relationsTab = desktopView.getRelationsTab();

		if (relationsTabNavigator == null) {
			relationsTabNavigator = new Navigator(UI.getCurrent(), relationsTab);
			relationsTabNavigator.addProvider(viewProvider);

		}

		relationsTabNavigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.RELATIONS_VIEW);

		TabSheet tabsheet = desktopView.getDataTabSheet();
		tabsheet.setSelectedTab(relationsTab);
	}
	
	public void displayStatements(String conceptId) {
		
		VerticalLayout relationsTab = desktopView.getRelationsTab();

		if (relationsTabNavigator == null) {
			relationsTabNavigator = new Navigator(UI.getCurrent(), relationsTab);
			relationsTabNavigator.addProvider(viewProvider);
		}

		relationsTabNavigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.RELATIONS_VIEW + "/" + conceptId);

		TabSheet tabsheet = desktopView.getDataTabSheet();
		tabsheet.setSelectedTab(relationsTab);
	}

	/**
	 * 
	 * @param conceptName
	 */
	public void setCurrentConceptTitle(String conceptName) {
		// desktopView.getViewingConceptsLabel()
		// .setValue("<span style=\"color:#dff0d8;\">" + "Viewing Relationships
		// for Concept: " + "</span>"
		// + "<span style=\"color:#ffffff;font-weight:bold;\">" + conceptName +
		// "</span>");
	}

	private String lastHighlightNodeId = null;

	private enum HighlightStatus {
		
		YES("yes"), NO("no");

		private String text;

		HighlightStatus(String text) {
			this.text = text;
		}
	}

	/**
	 * 
	 * updating the data of the node
	 * 
	 * @param yesOrNo
	 */
	private void highlightNode(HighlightStatus yesOrNo) {
		Node node = getConceptMap().getElements().getNodes().getNodeById(lastHighlightNodeId);
		if (node != null) {
			NodeData data = node.getData();
			if (data != null) {
				data.setActiveNode(yesOrNo.text);
				data.setState("update");
			}
		}
	}

	/**
	 * 
	 * highlight the seed node
	 * 
	 * @param currentConcept
	 */
	public void setHighlightedNode(IdentifiedConcept currentConcept) {
		// Removing highlights from previous concept node
		if (lastHighlightNodeId != null) {
			highlightNode(HighlightStatus.NO);
		}

		// 24 Oct 2016 - RB: don't always want to add new node to map at this
		// point

		// Highlighting new current concept node - identified by Clique name
		lastHighlightNodeId = currentConcept.getClique();
		highlightNode(HighlightStatus.YES);
	}

	/**
	 * 
	 * TODO: replace setCurrentConceptTitle with setConceptInSession
	 * 
	 * @param currentConcept
	 */
	private void setConceptInSession(IdentifiedConcept currentConcept) {
		setCurrentConceptTitle(currentConcept.getName());
		setHighlightedNode(currentConcept);
	}

	// private void setConceptInSession( Boolean concept ) {
	// initConceptLabel();
	// }

	private String lastHighlightEdgeId;

	/**
	 * 
	 * updating the data of the edge
	 * 
	 * @param yesOrNo
	 */	
	private void highlightEdge(String statementId, HighlightStatus yesOrNo) {
		Edge edge = getConceptMap().getElements().getEdges().getEdgeByStatementId(statementId);
		if (edge != null) {
			EdgeData data = edge.getData();
			if (data != null) {
				data.setActiveEdge(yesOrNo.text);
				data.setState("update");
				data.update();
			}
		}
	}

	/**
	 * 
	 * highlight the edge
	 * 
	 * @param source
	 * @param target
	 * @param label
	 */
	public void setHighlightedEdge(String statementId) {

		// Removing highlights from previous concept edge
		if (lastHighlightEdgeId != null) {
			highlightEdge(lastHighlightEdgeId, HighlightStatus.NO);
		}

		// Highlighting new current concept edge
		highlightEdge(statementId, HighlightStatus.YES);
		
		lastHighlightEdgeId = statementId;
	}

	/**
	 * 
	 */
	public void displayEvidence(String statementId) {

		query.setRelationSearchMode(RelationSearchMode.RELATIONS);

		// highlight the edge according to the predication
		setHighlightedEdge(statementId);

		VerticalLayout referenceTab = desktopView.getEvidenceTab();

		Navigator navigator = new Navigator(this, referenceTab);
		navigator.addProvider(viewProvider);
		navigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.EVIDENCE_VIEW);

		TabSheet tabsheet = desktopView.getDataTabSheet();
		tabsheet.setSelectedTab(referenceTab);
	}
	
	public void displayEvidence(Statement statement) {
		query.setCurrentStatement(statement);
		displayEvidence(statement.getId());
	}

	/**
	 * 
	 * @param annotation
	 */
	public void displayReference(Annotation annotation) {
		query.setCurrentAnnotation(annotation);
		String id = annotation.getId();
		if(!nullOrEmpty(id))
			displayReference(id);
		else
			ConfirmDialog.show(this,
					"<span style='text-align:center;'>No evidence reference available.</span>",
					cd -> {
					}).setContentMode(ConfirmDialog.ContentMode.HTML);
	}

	public void displayReference(String annotationId) {
		
		/* 
		 * Sometimes need to URL encode the 
		 * annotation identifier (e.g. if from WikiData)
		 */
		String encodedId = "";
		try {
			encodedId = URLEncoder.encode(annotationId, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			_logger.error("displayReference() ERROR for annotationId '"+annotationId+"': "+e.getMessage());
			return;
		}		
		Navigator navigator = new Navigator(this, desktopView.getReferenceLayout());

		navigator.addProvider(viewProvider);

		navigator.navigateTo(ReferenceView.NAME + "/" + encodedId);
		
		VerticalLayout referenceTab = desktopView.getReferenceTab();
		TabSheet tabsheet = desktopView.getDataTabSheet();
		tabsheet.setSelectedTab(referenceTab);
	}

	/**
	 * 
	 * @param concept
	 * @param mode
	 */
	public void queryUpdate(IdentifiedConcept concept, RelationSearchMode mode) {

		String conceptName = concept.getName();

		_logger.info("Selecting ListView item '" + conceptName + "' for processing?");

		query.setRelationSearchMode(mode);

		// I should probably only reset the current query concept when
		// I am intending to reset the current table of RELATIONS relations
		// TODO: review if any action is required with other
		// RelationSearchModes?
		switch (mode) {
		case RELATIONS:
			query.setCurrentQueryConceptById(concept.getClique()); 
			break;
		default:
			// do nothing?
		}

		setConceptInSession(concept);

		try {
			query.setCurrentSelectedConcept(concept);

			setConceptLabelDescription(concept);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	ConceptDetailsHandler detailsHandler;

	private String getCurrentConceptTitle(IdentifiedConcept concept) {
		String cct = concept.getName()+" ("+concept.getClique()+")";
		return cct;
	}
	
	/**
	 * 
	 * @param concept
	 */
	private void setConceptLabelDescription(IdentifiedConcept concept) {

		HorizontalLayout popupLayout = getDesktop().getPopUpLayout();

		if (concept == null) {
			popupLayout.setVisible(false);
			return;
		}

		popupLayout.setVisible(true);
		popupLayout.removeAllComponents();

		PopupView currentConceptPopupView = new PopupView(getCurrentConceptTitle(concept), null);
		currentConceptPopupView.setHideOnMouseOut(false);
		currentConceptPopupView.addStyleName("current-concept-label");

		popupLayout.addLayoutClickListener(
			new LayoutClickListener() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void layoutClick(LayoutClickEvent event) {
					currentConceptPopupView.setContent(new Content() {
						private static final long serialVersionUID = 1L;
	
						@Override
						public Component getPopupComponent() {
							VerticalLayout popupContent = detailsHandler.getDetails(concept);
							popupContent.setSpacing(true);
							popupContent.setMargin(true);
							popupContent.addStyleName("current-concept-popup-layout");
							return popupContent;
						}
	
						@Override
						public String getMinimizedValueAsHTML() {
							return getCurrentConceptTitle(concept);
						}
					});
				}
			}
		);

		popupLayout.addComponent(currentConceptPopupView);
		popupLayout.setComponentAlignment(currentConceptPopupView, Alignment.MIDDLE_CENTER);
	}

	/**
	 * clear the map
	 */
	public void clearMap() {
		getConceptMap().clearGraph();
		updateCurrentConceptMapName(null);
	}

	/**
	 * 
	 */
	public void clearSession() {
		initConceptLabelAndDescription();

		// Generally clear the query...
		query.resetQuery();

		/*
		 * clear a few more query variables not already cleared by resetQuery()
		 */
		query.setCurrentImportedMaps(null);
		query.setCurrentSelectedConcept(null);
		query.setRelationSearchMode(RelationSearchMode.RELATIONS);

		cache.resetCache();

		// Then go to empty data table
		gotoStatementsTable();

		TextField searchField = desktopView.getSearch();
		searchField.clear();
		searchField.setInputPrompt("Search Concepts");
		searchField.setImmediate(true);

		// Clear Concept Map too?
		getConceptMap().clearGraph();

		// Setting Default layout while clearing map
		desktopView.getCmLayoutSelect().setValue(DEFAULT_CM_LAYOUT);
	}

	/**
	 * 
	 */
	private void initConceptLabelAndDescription() {
		initConceptLabel();
		initConceptLabelDescription();
	}

	/**
	 * 
	 */
	private void initConceptLabel() {
		setCurrentConceptTitle(FIELD_ERROR);
	}

	/**
	 * 
	 * @param dialog
	 */
	private void saveAndClearMapHandler(ConfirmDialog dialog) {
		if (dialog.isConfirmed()) {
			saveMap(true);
		} else if (dialog.isCanceled()) {
			_logger.trace("User does not save his map?");
			clearMap();
		}
	}

	/**
	 * 
	 * @param dialog
	 */
	private void newQueryButtonHandler(ConfirmDialog dialog) {
		if (dialog.isConfirmed()) {

			ConfirmDialog.show(this, "Save Concept Map?",
					"<span style='text-align:center;'>Save work before reloading?</span>", "Yes", "No",
					cd -> saveAndClearMapHandler(cd)).setContentMode(ConfirmDialog.ContentMode.HTML);

		} else if (dialog.isCanceled()) {
			_logger.trace("User cancels 'New' operation");
		}
	}

	/**
	 * 
	 * @param event
	 */
	private void newQueryConfirmation(ClickEvent event) {
		if (conceptMapIsEmpty())
			return;
		ConfirmDialog.show(this, "New Graph", "<span style='text-align:center;'>Clear the graph?</span>", "OK",
				"Cancel", cd -> newQueryButtonHandler(cd)).setContentMode(ConfirmDialog.ContentMode.HTML);
	}

	private PopupWindow landingPagePopup = null;

	/**
	 * 
	 */
	public void closeLandingPage() {
		if (landingPagePopup != null) {
			landingPagePopup.close();
			landingPagePopup = null;
		}
	}

	// Terrible hacks. Should restructure the code to not use
	// this many navigators.
	private Navigator relationsTabNavigator;
	private Navigator evidenceTabNavigator;
	private Navigator pubmedTabNavigator;
	private Window legend = null;

	@Value("${application.hostname:'none'}")

	private String application_hostname; // for testing on localhost set "none"

	@Value("${google.tracking_id:''}")
	private String google_tracking_id; // e.g. "UA-87942746-1"

	/**
	 * initialize the desktop view
	 */
	public void initializeDesktopView() {
		
		initConceptLabelDescription();

		TextField searchField = desktopView.getSearch();

		searchField.addStyleName("concept-search-field");

		searchField.addFocusListener(e -> {
			desktopView.getSearchBtn().setClickShortcut(KeyCode.ENTER);
		});

		searchField.addBlurListener(e -> {
			desktopView.getSearchBtn().removeClickShortcut();
		});

		desktopView.getSearchBtn().addClickListener(e -> {
			searchBtnClickListener(searchField, e);
		});
		
		HorizontalLayout viewingConcepts = desktopView.getViewingConcepts();
		viewingConcepts.setSpacing(true);
		
		/*
		 * Deprecating this checkbox triggered version of matchByIdentifier
		 * Try to directly infer presence of a CURIE in the queryText (below)
		 
		// Choice of matching either by CURIE or by keywords
		CheckBox matchByIdCb = new CheckBox("Match By Identifier");
		matchByIdCb.setValue(query.matchByIdentifier());
		matchByIdCb.addValueChangeListener(
				event -> query.setMatchingMode(matchByIdCb.getValue())
		);
		viewingConcepts.addComponent(matchByIdCb);
        */
		
		// Button to reinitialize the query and map
		desktopView.getClearMapBtn().addClickListener(e -> newQueryConfirmation(e));

		desktopView.getDataTabSheet().addSelectedTabChangeListener(e -> {
			// Find the tabsheet
			TabSheet tabsheet = e.getTabSheet();

			// Find the tab (here we know it's a VerticalLayout)
			VerticalLayout tab = (VerticalLayout) tabsheet.getSelectedTab();

			// Get the tab caption from the tab object
			String caption = tabsheet.getTab(tab).getCaption();

			_logger.info("Selected Tab: " + caption);

			setTabNavigators(tab, caption);

			// Note: PubMed view is not a ListView...
			if (caption.equals(ViewName.REFERENCE_TAB)) {
				String state = pubmedTabNavigator.getState();
				if (!state.startsWith(ReferenceView.NAME)) {
					pubmedTabNavigator.navigateTo(ReferenceView.NAME);
				} else {
					pubmedTabNavigator.navigateTo(state);
				}
			} else if (caption.equals(ViewName.EVIDENCE_TAB)) {
				evidenceTabNavigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.EVIDENCE_VIEW);
			} else if (caption.equals(ViewName.RELATIONS_TAB)) {
				relationsTabNavigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.RELATIONS_VIEW);
			}
		});

		String[] colorOptions = { "Dark", "Light" };

		desktopView.getColorSelect().addItems((Object[]) colorOptions);
		desktopView.getColorSelect().setNullSelectionAllowed(false);

		desktopView.getColorSelect().addValueChangeListener(e -> {
			if ("Dark".equals(e.getProperty().getValue())) {
				desktopView.getCmPanel().setId("cm-panel-dark");
			} else if ("Light".equals(e.getProperty().getValue())) {
				desktopView.getCmPanel().setId("cm-panel-light");
			}
			getConceptMap().setStyleColor(((String) e.getProperty().getValue()).toLowerCase());
		});

		desktopView.getColorSelect().setValue(DEFAULT_CM_COLOR);

		Registry.hasSemanticFilter(ViewName.RELATIONS_VIEW, true);

		currentConcept = query.getCurrentQueryConcept();
		if (currentConcept.isPresent()) {
			setConceptInSession(currentConcept.get());
		} else {
			initConceptLabel();
		}

		// load
		KBUploader myUploader = new KBUploader();

		// only to upload ".kb"
		Page.getCurrent().getJavaScript()
				.execute("document.getElementsByClassName('gwt-FileUpload')[0].setAttribute('accept', '.kb')");
		desktopView.getLoadMap().setImmediate(true);
		desktopView.getLoadMap().setReceiver(myUploader);
		desktopView.getLoadMap().addFinishedListener(e -> {
			loadMap(myUploader.getFile());
			Page.getCurrent().getJavaScript()
					.execute("document.getElementsByClassName('gwt-FileUpload')[0].setAttribute('accept', '.kb')");
		});

		// save map to library
		desktopView.getSaveBtn().addClickListener(e -> saveMap(false));

		// search map library names with search text
		desktopView.getSearchMapLibraryBtn().addClickListener(e -> searchMapLibraryByText());

		// align the map to center
		desktopView.getCenterBtn().addClickListener(e -> {
			getConceptMap().alignToCenter();
		});

		// set zoom for map using a slider
		Slider zoomSlider = desktopView.getZoomSlider();
		zoomSlider.setValue(80.0);
		zoomSlider.addValueChangeListener(new ValueChangeListener() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			// when the value on the slider changes, the zoom is set to the new
			// value
			public void valueChange(ValueChangeEvent event) {
				if (zoomEnabled) {
					double value = (Double) zoomSlider.getValue();
					getConceptMap().setZoom(value);
				}
			}
		});

		// display the legend in a pop up window
		desktopView.getShowLegendBtn().addClickListener(e -> {
			// if there is no legend pop up window already opened
			if (legend == null) {
				// get the x and y coordinates where the mouse is clicked
				int x = e.getClientX();
				int y = e.getClientY();
				legend = new Window("Legend");
				legend.addStyleName("backColor");
				legend.setHeight("100%");
				legend.setWidth("250px");
				legend.setPositionX(x - 250);
				legend.setPositionY(y - 600);
				legend.setResizable(true);
				legend.addCloseListener(event -> {
					legend = null;
				});
				UI.getCurrent().addWindow(legend);

				// add contents to the legend
				VerticalLayout subContent = new VerticalLayout();
				subContent.addComponent(new Label(
						"<div class='container'><div class='ACTI circle'></div><div class='label'>Activities & Behaviors</div></div>"
								+ "<div class='container'><div class='ANAT circle'></div><div class='label'>Anatomy</div></div>"
								+ "<div class='container'><div class='CHEM circle'></div><div class='label'>Chemicals & Drugs</div></div>"
								+ "<div class='container'><div class='CONC circle'></div><div class='label'>Concepts & Ideas</div></div>"
								+ "<div class='container'><div class='DEVI circle'></div><div class='label'>Devices</div></div>"
								+ "<div class='container'><div class='DISO circle'></div><div class='label'>Disorders</div></div>"
								+ "<div class='container'><div class='GENE circle'></div><div class='label'>Genes & Molecular Sequences</div></div>"
								+ "<div class='container'><div class='GEOG circle'></div><div class='label'>Geographic Areas</div></div>"
								+ "<div class='container'><div class='LIVB circle'></div><div class='label'>Living Beings</div></div>"
								+ "<div class='container'><div class='OBJC circle'></div><div class='label'>Objects</div></div>"
								+ "<div class='container'><div class='OCCU circle'></div><div class='label'>Occupations</div></div>"
								+ "<div class='container'><div class='ORGA circle'></div><div class='label'>Organizations</div></div>"
								+ "<div class='container'><div class='PHEN circle'></div><div class='label'>Phenomena</div></div>"
								+ "<div class='container'><div class='PHYS circle'></div><div class='label'>Physiology</div></div>"
								+ "<div class='container'><div class='PROC circle'></div><div class='label'>Procedures</div></div>",
						ContentMode.HTML));
				HorizontalLayout subHorizontal = new HorizontalLayout();
				subHorizontal.setStyleName("subHorizontal-layout");
				Button subClose = new Button("Close");
				subClose.addClickListener(event -> {
					legend.close();
					legend = null;
				});
				subHorizontal.addComponent(subClose);
				subContent.addComponent(subHorizontal);
				legend.setContent(subContent);
				legend.setDraggable(false);
			} else {
				// there is already a legend opened, so close it
				legend.close();
				legend = null;
			}
		});

		// RMB: August 15, 2016
		// New landing page doesn't need the old intro tooltip
		// initIntroTooltip();

		initConceptMap();
	}
	
	public void setZoomEnabled(boolean zoomEnabled) {
		this.zoomEnabled = zoomEnabled;
	}

	/**
	 * 
	 * @param tab
	 * @param caption
	 */
	private void setTabNavigators(VerticalLayout tab, String caption) {
		if (relationsTabNavigator == null) {
			if (caption.equals(ViewName.RELATIONS_TAB)) {
				relationsTabNavigator = new Navigator(UI.getCurrent(), tab);
				relationsTabNavigator.addProvider(viewProvider);
			}
		}

		if (evidenceTabNavigator == null) {
			if (caption.equals(ViewName.EVIDENCE_TAB)) {
				evidenceTabNavigator = new Navigator(UI.getCurrent(), tab);
				evidenceTabNavigator.addProvider(viewProvider);
			}
		}

		if (pubmedTabNavigator == null) {
			if (caption.equals(ViewName.REFERENCE_TAB)) {
				pubmedTabNavigator = new Navigator(UI.getCurrent(), tab);
				pubmedTabNavigator.addProvider(viewProvider);
			}
		}
	}

	/**
	 * 
	 * @param searchField
	 * @param e
	 */
	private void searchBtnClickListener(TextField searchField, ClickEvent e) {
		// only allows the user to click once
		Button searchBtn = e.getButton();
		searchBtn.setEnabled(false);

		String queryText = desktopView.getSearch().getValue();

		// RMB: March 1, 2017 - empty queries seem too problematic now
		// so we ignore them again!

		if (nullOrEmpty(queryText)) {
			ConfirmDialog.show(this,
					"<span style='text-align:center;'>Please type in a non-empty query string in the search box</span>",
					cd -> {
					}).setContentMode(ConfirmDialog.ContentMode.HTML);

			searchBtn.setEnabled(true);
			return;
		}

		queryText = queryText.trim();
		query.setCurrentQueryText(queryText);

		if(matchByIdentifier(queryText)) {
			/*
			 * Matching by CURIE - resolve the matching concept 
			 * then go directly to the statements table
			 */
			 Optional<IdentifiedConcept> conceptOpt = conceptService.findByIdentifier(queryText);
			 
			if (!conceptOpt.isPresent()) {
				ConfirmDialog.show(this,
					"<span style='text-align:center;'>Concept identified by '" + queryText + "' could not be resolved.<br/>"
							+ "Please check if you have a valid CURIE identifier for your concept of interest!</span>",
					cd -> {
					}).setContentMode(ConfirmDialog.ContentMode.HTML);
				searchBtn.setEnabled(true);
				return;
			}
			
			IdentifiedConcept concept = conceptOpt.get();
			processConceptSearch(concept);

			searchBtn.setEnabled(true);
			
			gotoStatementsTable();
			
		} else { // Classical Keyword search
	
			// Semantic type constraint in Concept-by-text results listing should initial be empty
			query.setInitialConceptTypes(new HashSet<ConceptType>());
	
			ConceptSearchResults currentSearchResults = 
					new ConceptSearchResults(viewProvider, ViewName.CONCEPTS_VIEW);
			conceptSearchWindow = new Window();
			conceptSearchWindow.setCaption("Concepts Matched by Key Words");
			conceptSearchWindow.addStyleName("concept-search-window");
			conceptSearchWindow.center();
			conceptSearchWindow.setModal(true);
			conceptSearchWindow.setResizable(true);
			conceptSearchWindow.setWidth(75.0f, Unit.EM);
			conceptSearchWindow.setContent(currentSearchResults);
			
			conceptSearchWindow.addCloseListener(event -> {
				searchBtn.setEnabled(true);
				gotoStatementsTable();
			});
	
			UI.getCurrent().addWindow(conceptSearchWindow);
		}
	}

	/**
	 * Match the query against the form of A:B where A and B can be
	 * of any word characters [a-zA-Z_0-9].
	 * @param queryText
	 * @return true if the pattern is matched; otherwise false.
	 */
	private boolean matchByIdentifier(String queryText) {
		Matcher matcher = Pattern.compile("(\\w*):(\\w*)").matcher(queryText);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 */
	private void initConceptLabelDescription() {
		setConceptLabelDescription(null);
	}

	/**
	 * 
	 * @param event
	 */
	private void desktopSplitPanelPositionHandler(SplitPositionChangeEvent event) {

		_logger.debug("desktopSplitPanelPositionHandler(): resizing Concept Map canvas?");

		// float position = event.getSplitPosition() ;
		// Unit unit = event.getSplitPositionUnit() ; // this is probably
		// implicitly known?

		// Clear Concept Map too?
		getConceptMap().resizeGraphCanvas();

	}

	/**
	 * 
	 */
	private void initConceptMap() {
		String[] layoutOptions = { MANUAL_CM_LAYOUT, "Breadth First", "Grid", "Circle", "Cola", "Spread", "Dagre" };

		desktopView.getCmLayoutSelect().setNullSelectionAllowed(false);
		desktopView.getCmLayoutSelect().addItems((Object[]) layoutOptions);
		desktopView.getCmLayoutSelect().addValueChangeListener(e -> {
			String name = e.getProperty().getValue().toString().replaceAll("\\s+", "").toLowerCase();
			getConceptMap().setLayout(new Layout(name));
		});
		desktopView.getCmLayoutSelect().setValue(DEFAULT_CM_LAYOUT);
		desktopView.getCmPanel().addComponent(cm);

		VerticalSplitPanel splitPanel = desktopView.getDesktopSplitPanel();

		// March 26, 2017 - Ben want's his resizable pane back...
		// He's willing to accept the UI consequences!
		// splitPanel.setLocked(true);
		splitPanel.setLocked(false);

		splitPanel.addStyleName("main-splitpanel");
		splitPanel.addSplitPositionChangeListener(e -> desktopSplitPanelPositionHandler(e));

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private Cookie getCookieByName(String name) {
		// Fetch all cookies from the request
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		if (cookies != null) {
			// Iterate to find cookie by its name
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 */
	public void initIntroTooltip() {

		Cookie tooltipCookie = getCookieByName("kb2");

		if (tooltipCookie == null) {

			Label label = new Label("<span>To begin your navigation of the site, please kindly type"
					+ " in characters from the name of your biomedical concept of interest"
					+ " into this search box, then press 'enter' or click "
					+ "<b>the looking glass ('Q') icon button</b> to continue.</span>", ContentMode.HTML);

			Window introWindow = new Window();
			UI.getCurrent().addWindow(introWindow);

			introWindow.setId("introWindow");
			introWindow.setModal(true);

			introWindow.setWidth(40, Unit.EM);
			introWindow.setPosition(450, 50);

			introWindow.setClosable(false);
			introWindow.setResizable(true);

			introWindow.addCloseShortcut(KeyCode.ENTER, null);

			Button ok = new Button("OK, Got it!");
			ok.addClickListener(e -> introWindow.close());

			VerticalLayout content = new VerticalLayout();

			content.setMargin(true);
			content.setSpacing(true);

			content.addComponent(label);
			content.addComponent(ok);
			content.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);

			introWindow.setContent(content);
			introWindow.setCaption("Welcome");

			tooltipCookie = new Cookie("kb2", "accessed");
			tooltipCookie.setComment("Cookie for recording access to KB2");
			tooltipCookie.setMaxAge(864000);

			VaadinService.getCurrentResponse().addCookie(tooltipCookie);
		}

	}

	/**
	 * Opens popup window and navigates to view when btn clicked
	 * 
	 * @param btn
	 * @param view
	 */
	public void popupBtn(Button btn, String view) {
		btn.addClickListener(e -> {

			PopupWindow content = new PopupWindow();

			content.addStyleName("no-padding");

			if (view.equals(AboutView.NAME)) {
				content.setCaption("About us");
				content.setContent(new AboutView());
			} else if (view.equals(FaqView.NAME)) {
				content.setCaption("Help");
				content.setContent(new FaqView());
			} else if (view.equals(ContactView.NAME)) {
				content.setCaption("Contact us");
				content.setContent(new ContactView());
			}

			UI.getCurrent().addWindow(content);
		});

	}

	private static final String REGEX_CST_Id = Pattern.quote("<RootConceptId>") + "(.*?)"
			+ Pattern.quote("</RootConceptId>");
	private static final Pattern REGEX_CST_Id_Pattern = Pattern.compile(REGEX_CST_Id);

	private static final String REGEX_version = Pattern.quote("<version>") + "(.*?)" + Pattern.quote("</version>");
	private static final Pattern REGEX_version_Pattern = Pattern.compile(REGEX_version);

	/**
	 * @author Chandan Mishra (cmishra@sfu.ca) Starting load functionality
	 * @param cm_json_content
	 */
	public void loadMap(String cm_json_content) {

		Matcher version_matcher = REGEX_version_Pattern.matcher(cm_json_content);

		if (version_matcher.find()) {
			String id = version_matcher.group(1);
			if (!id.equals("3.0")) {
				new Notification("Sorry - can only load kb file format version 3.0", Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				return;
			}

		} else {
			new Notification("Cannot detect kb format version?", Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return;
		}

		Matcher cst_matcher = REGEX_CST_Id_Pattern.matcher(cm_json_content);
		if (cst_matcher.find()) {
			// calling import concept map
			getConceptMap().importConceptMap(cm_json_content);

			String id = cst_matcher.group(1);
			String conceptId = id.replaceAll(",", "");

			// Setting manual layout while loading
			desktopView.getCmLayoutSelect().setValue(MANUAL_CM_LAYOUT);

			List<String> beacons = query.getCustomBeacons();
			String sessionId = query.getUserSessionId();
			
			CompletableFuture<AnnotatedConcept> future = 
					kbService.getConceptDetails(conceptId,beacons,sessionId);
			
			try {
				AnnotatedConcept concept = future.get(
						kbService.weightedTimeout(), 
						KnowledgeBeaconService.BEACON_TIMEOUT_UNIT
				);
				
				query.setCurrentQueryConceptById(concept.getId());
				setCurrentConceptTitle(concept.getName());
				
			} catch (InterruptedException | ExecutionException | TimeoutException | IndexOutOfBoundsException e) {
				// e.printStackTrace();
				_logger.error("DesktopUI.loadMap() error: "+e.getMessage()); 
			}
			
			DesktopUI.getCurrent().getConceptMap().alignToCenter();
			
			gotoStatementsTable();

		} else {
			new Notification("Invalid kb File, Could not able to parse meta information.",
					Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		}
	}

	/**
	 * 
	 * @param map
	 */
	public void loadMap(ConceptMapArchive map) {
		updateCurrentConceptMapName(map.getName());
		loadMap(map.getConceptMapJson());
	}

	/**
	 * 
	 * @param file
	 */
	public void loadMap(File file) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(file.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadMap(content);
	}

	/**
	 * 
	 */
	private void searchMapLibraryByText() {

		// Search for maps by text
		query.setLibraryMode(LibrarySearchMode.BY_LIBRARY);

		DesktopUI ui = (DesktopUI) UI.getCurrent();

		// only allows the user to click once
		Button searchBtn = ui.getDesktop().getSearchBtn();
		searchBtn.setEnabled(false);

		// String queryText = desktop.getSearch().getValue();

		// Ignore empty queries!
		// RMB: Aug 10 2016 - accepting empty queries now: returns paged list
		// if(queryText == null || queryText.trim().isEmpty()) {
		// searchBtn.setEnabled(true);
		// return;
		// }

		// queryText = queryText.trim();

		// Now ignoring the main search text input field
		// if the library button is clicked!
		query.setCurrentQueryText("");

		ui.displayConceptMapList();
	}

	/**
	 * @author Chandan Mishra (cmishra@sfu.ca) Starting Save functionality
	 */

	@Autowired
	private ConceptMapArchiveService conceptMapArchiveService;

	/**
	 * 
	 * @return
	 */
	public ConceptMapArchiveService getConceptMapArchiveService() {
		return this.conceptMapArchiveService;
	}

	/**
	 * 
	 * @param isClearMap
	 * @return
	 */
	private boolean saveMap(boolean isClearMap) {
		SaveWindow saveWindow = new SaveWindow(
				getCurrentConceptMapName(),
				query,
				registry.getMapping(ViewName.LIBRARY_VIEW),
				cm,
				applicationNavigator,
				cache
		);
//		SaveWindow.raiseExportWindow("", DesktopUI.getCurrent().getConceptMap(), query, cache);

		this.addWindow(saveWindow);

		return true;
	}

	/**
	 * 
	 * @param map
	 */
	public void switchLibraryToDetails(ConceptMapArchive map) {
		LibrarySearchResults librarySearchResults = (LibrarySearchResults) conceptMapLibraryWindow.getContent();
		String caption = conceptMapLibraryWindow.getCaption();

		ClickListener goBack = e -> {
			conceptMapLibraryWindow.setContent(librarySearchResults);
			conceptMapLibraryWindow.setCaption(caption);
		};

		LibraryDetails libraryDetails = new LibraryDetails(map, query, userService, goBack);

		conceptMapLibraryWindow.setCaption("Concept Map Details");
		conceptMapLibraryWindow.setContent(libraryDetails);
	}

	/**
	 * 
	 * @param userId
	 */
	public void switchLibraryToUserDetails(String userId) {
		LibrarySearchResults librarySearchResults = (LibrarySearchResults) conceptMapLibraryWindow.getContent();
		String caption = conceptMapLibraryWindow.getCaption();

		ClickListener goBack = e -> {
			conceptMapLibraryWindow.setContent(librarySearchResults);
			conceptMapLibraryWindow.setCaption(caption);
		};

		UserDetails userDetails = new UserDetails(userId, goBack);

		conceptMapLibraryWindow.setCaption("User Details");
		conceptMapLibraryWindow.setContent(userDetails);
	}

	/**
	 * 
	 */
	public void displayConceptMapList() {
		LibrarySearchResults availableConceptMaps = new LibrarySearchResults(viewProvider);

		// Create new popup Window if needed, otherwise reuse?
		if (conceptMapLibraryWindow == null) {
			conceptMapLibraryWindow = new Window();
			conceptMapLibraryWindow.addStyleName("concept-search-window");

			conceptMapLibraryWindow.center();
			conceptMapLibraryWindow.setModal(true);
			conceptMapLibraryWindow.setResizable(true);

			// setWindowSize(conceptMapLibraryWindow);
			conceptMapLibraryWindow.setWidth(45.0f, Unit.EM);
			conceptMapLibraryWindow.setHeight(56.0f, Unit.EM);

			conceptMapLibraryWindow.addCloseListener(event -> {
				DesktopUI ui = (DesktopUI) UI.getCurrent();
				Button searchBtn = ui.getDesktop().getSearchBtn();
				searchBtn.setEnabled(true);
				ui.closeLibraryWindow();
				ui.closeConceptSearchWindow();
				gotoStatementsTable();
			});

			// Attempting dynamic resize - not really working

			// conceptMapLibraryWindow.addResizeListener(
			// event -> windowSizeHandler(event)
			// );

			UI.getCurrent().addWindow(conceptMapLibraryWindow);
		}

		conceptMapLibraryWindow.setCaption("Libraries");
		conceptMapLibraryWindow.setContent(availableConceptMaps);

	}

	private Navigator applicationNavigator;

	/**
	 * 
	 * @return
	 */
	public Navigator getApplicationNavigator() {
		return applicationNavigator;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	protected void init(VaadinRequest request) {
		
		GoogleAnalyticsTracker ga_tracker = new GoogleAnalyticsTracker(google_tracking_id, application_hostname, "/#!");
		_logger.info("google_tracking_id: " + google_tracking_id);
		_logger.info("application_hostname: " + application_hostname);

		ga_tracker.extend(this);
		
		/*
		 *  Disable session timeout
		 *  See if this resolves the current issue of pesky premature timeout expiration
		 */
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(-1);

		initializeDesktopView();

		ApplicationLayout applicationLayout = new ApplicationLayout(authenticationManager);

		this.loginView = applicationLayout.getLoginView();

		// Note: the DesktopView is initialized within this UI class, so
		// it is easier to just add the view here.
		registerView(applicationLayout);

		/*
		 * attach the Google Analytics tracker to the Navigator to automatically
		 * track all views
		 *
		 * To use the tracker without the Navigator, just call the
		 * tracker.trackPageview(pageId) separately when tracking is needed.
		 */
		applicationNavigator.addViewChangeListener(ga_tracker);
		
		getPage().addUriFragmentChangedListener(event -> handleURL(event.getUriFragment()));
		
		String fragment = getPage().getUriFragment();
		handleURL(fragment);

		setContent(applicationLayout);
	}
	
	private void handleURL(String fragment) {
		if (fragment == null) return;
		
		final String mapPrefix = "map=";
		final String passwordPrefix = "passwordReset?token=";

		if (fragment.startsWith(mapPrefix)) {
			
			applicationNavigator.navigateTo(ListView.NAME);

			User user = authenticationManager.getCurrentUser();
			String conceptMapName = fragment.replaceFirst(mapPrefix, "");
			
			ConceptMapArchive map = conceptMapArchiveService.getConceptMapArchiveByName(conceptMapName,
					user != null ? user.getUserId() : null,
					user != null ? user.getIdsOfGroupsBelongedTo() : new String[0]);
			
			if (map != null) {
				
				LibraryDetails libraryDetails = new LibraryDetails(map, query, userService,
						e -> {
							conceptMapLibraryWindow.close();
						});

				conceptMapLibraryWindow = new Window();
				conceptMapLibraryWindow.setContent(libraryDetails);
				conceptMapLibraryWindow.setModal(true);
				conceptMapLibraryWindow.setCaption("Viewing Concept Map");
				conceptMapLibraryWindow.addStyleName("concept-search-window");
				conceptMapLibraryWindow.center();
				conceptMapLibraryWindow.setWidth(45.0f, Unit.EM);
				conceptMapLibraryWindow.setHeight(56.0f, Unit.EM);
				conceptMapLibraryWindow.addCloseListener(e -> {
					Button searchBtn = getDesktop().getSearchBtn();
					searchBtn.setEnabled(true);
					closeLibraryWindow();
					closeConceptSearchWindow();
					gotoStatementsTable();
				});
				
				UI.getCurrent().addWindow(conceptMapLibraryWindow);
				
			} else {
				Notification.show("No concept map with the name \"" + conceptMapName
						+ "\" was found. You may need to login to view this map.", Type.WARNING_MESSAGE);
			}

		} else if (fragment.startsWith(passwordPrefix)) {
			
			String token = fragment.replace(passwordPrefix, "");
			applicationNavigator.navigateTo(PasswordResetView.NAME + "/" + token);
		}
	}

	private LoginView loginView;

	/**
	 * 
	 * @return
	 */
	public LoginView getLoginView() {
		return this.loginView;
	}

	/**
	 * 
	 * @param applicationLayout
	 */
	private void registerView(ApplicationLayout applicationLayout) {
		applicationNavigator = applicationLayout.getNavigator();
		applicationNavigator.addView(DesktopView.NAME, desktopView);
		applicationNavigator.addView(ListView.NAME, desktopView);
		applicationNavigator.addView(ReferenceView.NAME, desktopView);
	}

	private String currentConceptMapName = "";

	/**
	 * 
	 * @param name
	 */
	public void updateCurrentConceptMapName(String name) {
		this.currentConceptMapName = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrentConceptMapName() {
		return this.currentConceptMapName;
	}
}

