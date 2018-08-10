package bio.knowledge.web.view;

import java.util.Collection;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellDescriptionGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;


public class StatementsView extends VerticalLayout {
	
	public static final String NAME = "statements";
	public static final String DETAILS_ID = "Details";
	public static final String STATEMENTS_ID = "Statements";

	private static final long serialVersionUID = -7988756397144422937L;
	
	private Grid conceptsGrid = new Grid();
	private Grid statemtsGrid = new Grid();
	private Button addToGraphBtn = new Button();
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout statemtsLayout = new VerticalLayout();

	/**
	 * Initializes side-by-side view of concepts and statements grids
	 * @param conceptsContainer
	 * @param kbService
	 * @param kbQuery
	 */
	public StatementsView(Indexed conceptsContainer) {
		init();
		GeneratedPropertyContainer container = addDetailsAndStatementsColumns(conceptsContainer);
		conceptsGrid.setContainerDataSource(container);
		conceptsGrid.getColumn(DETAILS_ID).setWidth(90);
		conceptsGrid.getColumn(STATEMENTS_ID).setWidth(90);
	}
	
	public GeneratedPropertyContainer addDetailsAndStatementsColumns(Indexed conceptsContainer) {
		GeneratedPropertyContainer container = new GeneratedPropertyContainer(conceptsContainer);
		container.addGeneratedProperty(DETAILS_ID, new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
		    public String getValue(Item item, Object itemId, Object propertyId) {
		        return "details"; 
		    }

		    @Override
		    public Class<String> getType() {
		        return String.class;
		    }
		});
		
		container.addGeneratedProperty(STATEMENTS_ID, new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
		    public String getValue(Item item, Object itemId, Object propertyId) {
		        return "search"; 
		    }

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
		
		addToGraphBtn.setCaption("<strong style = \"font-size: 120%;\">Add to Graph</strong>");
		addToGraphBtn.setCaptionAsHtml(true);
		addToGraphBtn.setDescription("Click to add selected statements to canvas");
		addToGraphBtn.setEnabled(false);
		addToGraphBtn.setWidth(100, Unit.PERCENTAGE);
		
		progressBar.setIndeterminate(true);
		
		statemtsLayout.addComponent(statemtsGrid);
		statemtsLayout.setComponentAlignment(statemtsGrid, Alignment.MIDDLE_CENTER);
		statemtsLayout.setSizeFull();
		
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setFirstComponent(conceptsGrid);
		splitPanel.setSecondComponent(statemtsLayout);
		splitPanel.setSplitPosition(55, Unit.PERCENTAGE);
		
		setSpacing(true);
		setHeight(100, Unit.PERCENTAGE);
		addComponents(splitPanel, addToGraphBtn);
		setExpandRatio(splitPanel, 1);

		initGraphBtnToggle();
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
	
	public void showProgress() {
		statemtsLayout.replaceComponent(statemtsGrid, progressBar);
		statemtsLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
	}
	
	public void hideProgress() {
		statemtsLayout.replaceComponent(progressBar, statemtsGrid);		
	}
}
