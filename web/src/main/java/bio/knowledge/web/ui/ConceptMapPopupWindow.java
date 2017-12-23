/*-------------------------------------------------------------------------------
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.ConfirmDialog.Listener;

import com.ibm.icu.util.Calendar;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.graph.jsonmodels.Node;
import bio.knowledge.graph.jsonmodels.NodeData;
import bio.knowledge.model.Annotation;
import bio.knowledge.model.Annotation.Type;
import bio.knowledge.model.Evidence;
import bio.knowledge.model.EvidenceCode;
import bio.knowledge.model.GeneralStatement;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.PredicateImpl;
import bio.knowledge.model.Reference;
import bio.knowledge.model.Statement;
import bio.knowledge.model.neo4j.Neo4jAnnotatedConcept;
import bio.knowledge.model.neo4j.Neo4jAnnotation;
import bio.knowledge.model.neo4j.Neo4jEvidence;
import bio.knowledge.model.neo4j.Neo4jGeneralStatement;
import bio.knowledge.model.neo4j.Neo4jReference;
import bio.knowledge.service.AnnotationService;
import bio.knowledge.service.Cache;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.EvidenceService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.KBQuery.RelationSearchMode;
import bio.knowledge.service.PredicateService;
import bio.knowledge.service.ReferenceService;
import bio.knowledge.service.StatementService;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.view.ConceptSearchResults;
import bio.knowledge.web.view.ViewName;

/**
 * This class creates an popup window to be filled in by views Properties
 *
 */
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConceptMapPopupWindow {

	private DesktopUI parentUi;
	
	@Autowired
	KnowledgeBeaconService kbService;

	@Autowired
	public ConceptMapPopupWindow(DesktopUI ui) {
		this.parentUi = ui;
		details = new VerticalLayout();
		details.setMargin(true);
		details.setSpacing(true);

		buttonsLayout = new HorizontalLayout();
		buttonsLayout.addStyleName("node-popup-button-layout");
		buttonsLayout.setSpacing(true);
	}

	@Autowired
	private ConceptService conceptService;

	@Autowired
	private Cache cache;

	@Autowired
	private StatementService statementService;

	@Autowired
	private PredicateService predicateService;

	@Autowired
	private EvidenceService evidenceService;

	@Autowired
	AnnotationService annotationService;

	@Autowired
	ReferenceService referenceService;

	@Autowired
	private KBQuery query;
	
	@Autowired
	ConceptDetailsHandler detailshandler;

	// position
	int myX;
	int myY;
	
	// components
	private VerticalLayout details;
	private HorizontalLayout buttonsLayout;
	private Window conceptDetailsWindowOnGraph;
	private Button showRelations;
	private Button showEvidence;
	private Button okay;
	private Button addAnno;
	private Button delete;
	private Button cancel;

	// data
	private BeanItemContainer<NodeData> graphNodeContainer = new BeanItemContainer<NodeData>(NodeData.class);
	private final NodeData moreNodesStub = new NodeData("-1", "More nodes...");
	ComboBox comboBoxSource = new ComboBox("Source", graphNodeContainer);
	ComboBox comboBoxTarget = new ComboBox("Target", graphNodeContainer);
	
	private void basicSkeleton(String name, String type, int x, int y) {
		basicSkeleton(name, type, x, y, null);
	}

	private void basicSkeleton(String name, String type, int x, int y, VerticalLayout detailsLayout) {
		
		// initialize our new-fangled conceptDetailsWindowOnGraph
		conceptDetailsWindowOnGraph = new Window();
		conceptDetailsWindowOnGraph.setCaption(name);
		conceptDetailsWindowOnGraph.addStyleName("node-popup-window");
		conceptDetailsWindowOnGraph.setResizable(false);

		if(!name.equals("Annotate Graph")) {
			Label nameLabel = new Label(
				"<span style=\"font-weight: bold;\"> Name: " + "</span>" + "<span>" + name + "</span>");
			nameLabel.setContentMode(ContentMode.HTML);
			details.addComponent(nameLabel);
		}
		
		if (detailsLayout != null) {
			details.addComponent(detailsLayout);
		}

		// bind components together
		details.addComponent(buttonsLayout);
		conceptDetailsWindowOnGraph.setContent(details);

		// add and render the concepts
		parentUi.addWindow(conceptDetailsWindowOnGraph);
		// click outside popup will close this window
		parentUi.addClickListener(e -> {
			conceptDetailsWindowOnGraph.close();
		}); 
		
		// We were positioning the window where the mouse click occurred,
		// but this often resulted in windows appearing half way off screen.
		conceptDetailsWindowOnGraph.center();
	}

	/**
	 *  Nodes are assumed to be globally identified by clique identifiers.
	 *  This method attempts to return a fully annotation Concept object
	 *  aggregating concept details across all beacons.
	 *  
	 * @param cliqueId
	 * @param name
	 * @param x
	 * @param y
	 */
	public void conceptMapNodePopUp(String cliqueId, String name, int x, int y) {
		
		/*
		 *  This search is new beacon "concept details" 
		 *  search for data... should work in principle
		 */
		IdentifiedConcept annotatedConcept = conceptService.searchAllBeacons(cliqueId);
		
		if (annotatedConcept == null) {
			try {
				
				/*
				 *  Older KB 3.0 direct WikiData retrieval (bypasses beacons)
				 *  but doesn't recognize non-WikiData entities
				 *  (Q: Could this somehow be generalized, though, for any standard
				 *  CURIE pointing to an external resource(?) e.g. NCBIGene, CHEBI, etc?)
				 */
				annotatedConcept = conceptService.getQualifiedDataItem(cliqueId);
				
			} catch (DataSourceException e) {
				
				details.addComponent(new Label("Cannot find concept '"+name+"'. Datasource may be offline?"));
			}
		} 
		
		final IdentifiedConcept selectedConcept = annotatedConcept;
		
		if( selectedConcept != null ) {
			
			/* DISABLE FOR NOW - review and republish feature later
			addAnno = new Button("Add Annotation", e -> {
				parentUi.getPredicatePopupWindow().conceptMapUserAnnotation(selectedConcept, x, y);
			});
			
			addAnno.setEnabled(false);

			
			if (addAnno != null && ((DesktopUI) UI.getCurrent()).getAuthenticationManager().isUserAuthenticated() )
				buttonsLayout.addComponent(addAnno);
			*/
			
			showRelations = new Button("Show Relations", e -> {
				parentUi.queryUpdate(selectedConcept, RelationSearchMode.RELATIONS);
				conceptDetailsWindowOnGraph.close();
				parentUi.gotoStatementsTable();
			});
			
			//if (showRelations != null)
			buttonsLayout.addComponent(showRelations);
			
			//if (selectedConcept == null || !conceptIsAvailable) {
				//addAnno.setEnabled(false);
				//showRelations.setEnabled(false);
			//}
			
			// Create buttons related to node popup
			// Okay -> no
			// ShowRelations -> conditional
			// Cancel -> yes
			// Delete -> yes
			// addAnno -> yes
		}

		delete = new Button("Delete", e -> {
			ConfirmDialog.show(parentUi, "Please Confirm:", "Are you really sure?", "I am", "Not quite",
					(Listener) dialog -> {
				if (dialog.isConfirmed()) {

					// Confirmed to continue
					parentUi.getConceptMap().deleteNodefromConceptMap(cliqueId);
					conceptDetailsWindowOnGraph.close();
					
				} else {
					// User did not confirm
					conceptDetailsWindowOnGraph.close();
				}
			});
		});

		cancel = new Button("Cancel", e -> {
			conceptDetailsWindowOnGraph.close();
		});

		buttonsLayout.addComponents(delete, cancel);

		if (selectedConcept == null) {
			basicSkeleton(name, null, x, y);
		} else {
			VerticalLayout conceptdetails = detailshandler.getDetails(selectedConcept);
			conceptdetails.setWidthUndefined();
			basicSkeleton(name, selectedConcept.getType().getDescription(), x, y, conceptdetails);
		}
	}
	
	public void conceptMapEdgePopUp(String sourceId, String targetId, String label, int x, int y, String description,
			String uri, String statementId) {

		// Generate popup content from passed data
		Optional<IdentifiedConcept> sourceOpt = conceptService.getDetailsByCliqueId(sourceId);
		if (!sourceOpt.isPresent())
			return;
		IdentifiedConcept sourceConcept = sourceOpt.get();
		String sourceName = sourceConcept.getName();

		Optional<IdentifiedConcept> targetOpt = conceptService.getDetailsByCliqueId(targetId);
		if (!targetOpt.isPresent())
			return;
		IdentifiedConcept targetConcept = targetOpt.get();
		String targetName = targetConcept.getName();
		
		addAnno = new Button("Add Annotation", e -> {
			parentUi.getPredicatePopupWindow().conceptMapUserAnnotation(sourceOpt.get(), x, y);
		});
		addAnno.setEnabled(false);
		if (addAnno != null && ((DesktopUI) UI.getCurrent()).getAuthenticationManager().isUserAuthenticated() )
			buttonsLayout.addComponent(addAnno);
		
		if (statementId != null) {
			showEvidence = new Button("Show Evidence", e -> {
				Predicate predicate = new PredicateImpl(label);
				GeneralStatement statement = new GeneralStatement(statementId, targetConcept, predicate, sourceConcept);
				conceptDetailsWindowOnGraph.close();
				parentUi.displayEvidence(statement);
			});

			buttonsLayout.addComponent(showEvidence);
		}

		delete = new Button("Delete", e -> {
			ConfirmDialog.show(parentUi, "Please Confirm:", "Are you really sure?", "I am", "Not quite",
					(Listener) dialog -> {
				if (dialog.isConfirmed()) {
					// Confirmed to continue
					parentUi.getConceptMap().deleteEdgefromConceptMap(sourceId, targetId, label);
					conceptDetailsWindowOnGraph.close();
				} else {
					// User did not confirm
					conceptDetailsWindowOnGraph.close();
				}
			});
		});

		cancel = new Button("Cancel", e -> {
			conceptDetailsWindowOnGraph.close();
		});

		buttonsLayout.addComponents(delete, cancel);

		String display = sourceName + "- " + label + "-> " + targetName;

		basicSkeleton(display, null, x, y);

	}

	public void conceptMapUserAnnotation(IdentifiedConcept selectedConcept, int x, int y) {
		query.tempCoordX(x);
		query.tempCoordY(y);
		
		// create the collections necessary for the menus
		// predicate
		Set<Predicate> predicateCollection = predicateService.findAllPredicates();

		// graph nodes
		// because it's essentially json data we need to manufacture the new
		// property properly
		initGraphNodeContainer();
		
		// populate details with content

		ComboBox comboBoxPredicate = new ComboBox("Predicate", predicateCollection);

		comboBoxSource.setValue(selectedConcept.getId());
		comboBoxTarget.setValue(selectedConcept.getId());

		comboBoxSource.addValueChangeListener(sourceData -> {
			System.out.println( ((NodeData) comboBoxSource.getValue()));
			System.out.println(graphNodeContainer.toString());
			if (((NodeData) comboBoxSource.getValue()) != null && ((NodeData) comboBoxSource.getValue()).getId() == moreNodesStub.getId()) {
				annotationConceptSearchBox();
				conceptDetailsWindowOnGraph.close();
			};
		});

		comboBoxTarget.addValueChangeListener(targetData -> {
			System.out.println(((NodeData) comboBoxTarget.getValue()));
			System.out.println(graphNodeContainer.toString());

			if (((NodeData) comboBoxTarget.getValue()) != null && ((NodeData) comboBoxTarget.getValue()).getId() == moreNodesStub.getId()) {
				annotationConceptSearchBox();
				conceptDetailsWindowOnGraph.close();
			};
		});

		comboBoxPredicate.setInputPrompt("Predicate");
		comboBoxSource.setInputPrompt("Source Node");
		comboBoxTarget.setInputPrompt("Target Node");
		comboBoxPredicate.setTextInputAllowed(false);
		comboBoxSource.setTextInputAllowed(false);
		comboBoxTarget.setTextInputAllowed(false);

		TextArea descriptionText  = new TextArea("Description");
		TextField uriEvidenceText = new TextField("URI of Evidence");
		CheckBox visibilityBox = new CheckBox("Visible to All", true);
		
		// set box settings
		comboBoxSource.setItemCaptionPropertyId("name");
		comboBoxTarget.setItemCaptionPropertyId("name");
		comboBoxSource.select(selectedConcept.getId());

		details.addComponents(comboBoxSource, comboBoxPredicate, comboBoxTarget, descriptionText, uriEvidenceText, visibilityBox);

		// Create buttons related to node popup
		// Okay -> yes
		// Cancel -> yes
		// ShowRelations -> no
		// Delete -> no
		// addAnno -> no

		okay = new Button("Okay", e -> {
			// getValue returns from NodeData or Predicate, i.e. the item
			// content.

			String sourceId = "";
			if (((NodeData) comboBoxSource.getValue()) != null) {
				sourceId = ((NodeData) comboBoxSource.getValue()).getId();
				if (sourceId == moreNodesStub.getId()) {
					return;
				}
			} else {
				return;
			}

			String targetId = "";
			if (((NodeData) comboBoxTarget.getValue()) != null) {
				targetId = ((NodeData) comboBoxTarget.getValue()).getId();
				if (targetId == moreNodesStub.getId()) {
					return;
				}
			} else {
				return;
			}

			String relationLabel;
			if (((Predicate) comboBoxPredicate.getValue()) != null) {
				relationLabel = ((Predicate) comboBoxPredicate.getValue()).getName();
			} else {
				// open up the search box?
				relationLabel = "";
			}

			int edgeId = (sourceId + targetId + relationLabel).hashCode();
			String evidenceId = sourceId.split(":")[sourceId.split(":").length - 1] + "."
					+ relationLabel.split(":")[relationLabel.split(":").length - 1] + "."
					+ targetId.split(":")[targetId.split(":").length - 1];
			String description = descriptionText.getValue();
			String uri = uriEvidenceText.getValue();

			String statementId = String.valueOf(edgeId);
			Predicate relation = (Predicate) comboBoxPredicate.getValue();

			// assert the Statement
			Statement statement = 
					statementService.findbySourceAndTargetId(sourceId, targetId, relationLabel);

			// statement doesn't exist in database
			if (statement == null) {
				// find subject
				IdentifiedConcept subject = conceptService.findByCliqueId(sourceId);
				if (subject == null) {
					// subject = new Concept();
					return;
				}

				// find object
				IdentifiedConcept object = conceptService.findByCliqueId(targetId);
				if (object == null) {
					// object = new Concept();
					return;
				}

				// add this to the database
				statement = new Neo4jGeneralStatement(statementId,
						conceptService.getDetailsByCliqueId(sourceId).get(), 
						relation,
						conceptService.getDetailsByCliqueId(targetId).get());

				Evidence evidence = evidenceService.createByEvidenceId(evidenceId);
				statement.setEvidence(evidence);

				// not putting this back into the statement object might have been a bit of a problem
				statement = statementService.save((Neo4jGeneralStatement) statement);
				
			}

			// statement should no longer be null here no matter what the case.
			if (statement != null) {
				// May have seen this Reference before?
				Reference reference = referenceService.findByUri(uri);
				if (reference == null) {
					// TODO: can we turn this into something more generic?
					reference = new Neo4jReference();
					reference.setUri(uri);

					// User citation is given today's date
					Calendar calendar = Calendar.getInstance();
					reference.setYearPublished(calendar.get(Calendar.YEAR));
					reference.setMonthPublished(calendar.get(Calendar.MONTH));
					reference.setDayPublished(calendar.get(Calendar.DAY_OF_MONTH));

					reference = referenceService.save((Neo4jReference) reference);
				}

				// Wonder how the annotationId should be consistently computed?
				String annotationId = "kba:" + String.valueOf((description + uri).hashCode());
				Annotation annotation = annotationService.findById(annotationId);
				
				if (annotation == null) {
					annotation = new Neo4jAnnotation(annotationId, description, Type.Remark, EvidenceCode.IC, reference);
					if(((DesktopUI) UI.getCurrent()).getAuthenticationManager().isUserAuthenticated()) {
						annotation.setUserId(((DesktopUI) UI.getCurrent()).getAuthenticationManager().getCurrentUser().getId());					
					}
					annotation.setVisible(visibilityBox.getValue());
					annotation = annotationService.save(annotation);
				}

				// Hopefully Statement.evidence is not null here!
				Evidence evidence = statement.getEvidence();
				// ..update and save?
				evidence.addAnnotation(annotation);
				evidenceService.save((Neo4jEvidence) evidence);

				List<IdentifiedConcept> subjects = statement.getSubjects();
				for (IdentifiedConcept s : subjects) {
					s.incrementUsage();
					/*
					 * TODO: Don't think that we need to replace subject in
					 * statement subjects list, but may be worthwhile to double
					 * check this
					 */
					conceptService.save((Neo4jAnnotatedConcept) s);
				}

				List<IdentifiedConcept> objects = statement.getObjects();
				for (IdentifiedConcept o : objects) {
					o.incrementUsage();
					/*
					 * TODO: Don't think that we need to replace subject in
					 * statement subjects list, but may be worthwhile to double
					 * check this
					 */
					conceptService.save((Neo4jAnnotatedConcept) o);
				}

				// Save the whole updated statement?
				statement = statementService.save((Neo4jGeneralStatement) statement);
			}
			
			/*
			 *  Here, we invalidate any cache references 
			 *  to statements associated with either 
			 *  the source or target concepts
			 */
			cache.invalidate("Statement", sourceId);
			cache.invalidate("Statement", targetId);
			cache.invalidate("Evidence", "kbe:"+evidenceId); // need to add prefix to raw evidenceId
			
			parentUi.getConceptMap().addEdgeToConceptMap(statement);

			conceptDetailsWindowOnGraph.close();
		});

		cancel = new Button("Cancel", e -> {
			conceptDetailsWindowOnGraph.close();
		});

		buttonsLayout.addComponents(okay, cancel);

		basicSkeleton("Annotate Graph", selectedConcept.getType().getDescription(), x, y);
	};

	private void annotationConceptSearchBox() {
		// set global query parameters relevant to the query
		
		String queryText = "";
		
		query.setCurrentQueryText(queryText);

		ConceptSearchResults currentSearchResults = 
				new ConceptSearchResults( 
						((DesktopUI) UI.getCurrent()).getViewProvider(), 
						ViewName.ANNOTATIONS_VIEW);
		Window conceptSearchWindow = new Window();
		conceptSearchWindow.setCaption("Concepts Matched by Key Words");
		conceptSearchWindow.addStyleName("concept-search-window");
		conceptSearchWindow.center();
		conceptSearchWindow.setModal(true);
		conceptSearchWindow.setResizable(true);

		// setWindowSize(conceptSearchWindow);
		conceptSearchWindow.setWidth(60.0f, Unit.EM);

		conceptSearchWindow.setContent(currentSearchResults);

		((DesktopUI) UI.getCurrent()).setConceptSearchWindow(conceptSearchWindow);

		UI.getCurrent().addWindow(((DesktopUI) UI.getCurrent()).getConceptSearchWindow());
		
	}

	public void addToGraphNodeContainerFromKBQuery() {
		IdentifiedConcept lastConcept = query.getLastSelectedConcept();
		Node node = new Node(lastConcept);
		NodeData nd = node.getData();
		initGraphNodeContainer();
		if(!graphNodeContainer.containsId(nd)) {
			// Add to the "bottom" of the container
			graphNodeContainer.addItemAt(graphNodeContainer.size() - 1, nd);
		}
		System.out.println(graphNodeContainer.getItemIds().toString());
	}

	private void initGraphNodeContainer() {
		Set<NodeData> graphNodeCollection = parentUi.getConceptMap().getElements().getNodes().getNodesAsCollection()
				.parallelStream().map(node -> {
					return node.getData();
				}).collect(Collectors.toSet());
	
		graphNodeContainer.addAll(graphNodeCollection);
		graphNodeContainer.addBean(moreNodesStub);
	}
	
	public void addConceptToComboBoxes(IdentifiedConcept concept) {
		Node node = new Node(concept);
		NodeData nd = node.getData();
		graphNodeContainer.addBean(nd);
		ComboBox newComboBoxSource = new ComboBox();
		newComboBoxSource.addItems(graphNodeContainer);
		ComboBox newComboBoxTarget = new ComboBox();
		newComboBoxTarget.addItems(graphNodeContainer);
	}

	public int getMyX() {
		return this.myX;
	}
	
	public int getMyY() {
		return this.myY;
	}
	
}
