package bio.knowledge.web.view;

import java.util.Collection;
import java.util.List;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellDescriptionGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.client.model.BeaconConcept;


public class StatementsView extends VerticalLayout {
	
	public static final String NAME = "statements";
	public static final String STATEMENTS_ID = "Statements";
	public static final String CONCEPTS_NAME_ID = "name"; // based on field name in BeaconConcept

	private static final long serialVersionUID = -7988756397144422937L;
	
	private Grid conceptsGrid = new Grid();
	private Grid statemtsGrid = new Grid();
	private Button addToGraphBtn = new Button();
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout statemtsLayout = new VerticalLayout();
	private Label statemtsTitle = new Label("Statements for: ");
	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	
	/**
	 * Initializes concepts and statements tab views
	 * @param conceptsContainer
	 * @param kbService
	 * @param kbQuery
	 */
	public StatementsView(List<BeaconConcept> results) {
		init();
		setConceptsGridColumns(results); 
	}

	private void setConceptsGridColumns(List<BeaconConcept> results) {
		GeneratedPropertyContainer container = addStatementsColumn
				(new BeanItemContainer<>(BeaconConcept.class, results));
		conceptsGrid.setContainerDataSource(container);
		
		conceptsGrid.getColumn(STATEMENTS_ID).setWidth(80);
		conceptsGrid.getColumn(STATEMENTS_ID).setHeaderCaption("Statements");
		conceptsGrid.setColumnOrder(STATEMENTS_ID, CONCEPTS_NAME_ID, "categories");
		conceptsGrid.getColumn(CONCEPTS_NAME_ID).setExpandRatio(1);
		conceptsGrid.getColumn("clique").setHidden(true);
	}

	public GeneratedPropertyContainer addStatementsColumn(Indexed conceptsContainer) {
		GeneratedPropertyContainer container = new GeneratedPropertyContainer(conceptsContainer);
		container.addGeneratedProperty(STATEMENTS_ID, new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
		    public String getValue(Item item, Object itemId, Object propertyId) {
		        return "show";
		    }
;
		    @Override
		    public Class<String> getType() {
		        return String.class;
		    }
		});
		
		return container;
	}

	private void init() {
		conceptsGrid.setSelectionMode(SelectionMode.SINGLE);
		conceptsGrid.setCellDescriptionGenerator(getCellDescriptionGenerator());
		conceptsGrid.setSizeFull();
		
		statemtsGrid.setSelectionMode(SelectionMode.MULTI);
		statemtsGrid.setCellDescriptionGenerator(getCellDescriptionGenerator());		
		statemtsGrid.setSizeFull();
				
		statemtsTitle.setCaptionAsHtml(true);
		
		addToGraphBtn.setCaption("<strong style = \"font-size: 120%;\">Add to Graph</strong>");
		addToGraphBtn.setCaptionAsHtml(true);
		addToGraphBtn.setDescription("Click to add selected statements to canvas");
		addToGraphBtn.setEnabled(false);
		addToGraphBtn.setWidth(100, Unit.PERCENTAGE);
		
		progressBar.setIndeterminate(true);
				
		statemtsLayout.addComponent(statemtsTitle);
		statemtsLayout.addComponent(statemtsGrid);
		statemtsLayout.setComponentAlignment(statemtsGrid, Alignment.MIDDLE_CENTER);
		
		statemtsLayout.addComponent(addToGraphBtn);
		statemtsLayout.setExpandRatio(statemtsTitle, 0.1f);
		statemtsLayout.setExpandRatio(statemtsGrid, 1);
		statemtsLayout.setSizeFull();

		setSpacing(true);
		setHeight(100, Unit.PERCENTAGE);
		
		initGraphBtnToggle();
		
		splitPanel.setFirstComponent(conceptsGrid);
		splitPanel.setSecondComponent(statemtsLayout);
		showConceptsResults();
//		splitPanel.setLocked(true);
		
		addComponent(splitPanel);
	}

	private void initGraphBtnToggle() {
		statemtsGrid.addSelectionListener(e -> {
			Collection<Object> itemIds = statemtsGrid.getSelectedRows();
			if (itemIds.isEmpty()) {
				addToGraphBtn.setEnabled(false);
			} else {
				addToGraphBtn.setEnabled(true);
			}
		});
	}

	private CellDescriptionGenerator getCellDescriptionGenerator() {
		return cell -> {
			String value = cell.getValue().toString();
			return "<strong style = \"font-size: 120%;\">" + value + "</strong>";
		};
	}
	
	public Grid getConceptsGrid() {
		return conceptsGrid;
	}
	
	public Grid getStatemtsGrid() {
		return statemtsGrid;
	}
	
	public Button getAddToGraphButton() {
		return addToGraphBtn;
	}
	
	public VerticalLayout getStatementsLayout() {
		return statemtsLayout;
	}
	
	public void showProgress() {
		if (statemtsLayout.getComponentIndex(statemtsGrid) != -1) {
			statemtsLayout.replaceComponent(statemtsGrid, progressBar);
			statemtsLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		}
	}
	
	public void hideProgress() {
		if (statemtsLayout.getComponentIndex(progressBar) != -1) {
			statemtsLayout.replaceComponent(progressBar, statemtsGrid);		
		}
	}

	public void setStatementsTitle(BeaconConcept concept) {
		statemtsTitle.setCaption("Statements for: <strong style = \"font-size: 120%\">" + concept.getName() + " (" + concept.getClique() + ") </strong>");	
	}
	
	public HorizontalSplitPanel getSplitPanel() {
		return splitPanel;
	}
	
	public void showConceptsResults() {
		splitPanel.setSplitPosition(70, Unit.PERCENTAGE);
	}
	
	public void showStatementsResults() {
		splitPanel.setSplitPosition(30, Unit.PERCENTAGE);
	}
}
