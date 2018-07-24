package bio.knowledge.web.view;

import java.util.Collection;

import com.vaadin.data.Container.Indexed;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellDescriptionGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

public class StatementsView extends VerticalLayout {
	
	public static final String NAME = "statements";

	private static final long serialVersionUID = -7988756397144422937L;
	
	private Grid conceptsGrid = new Grid();
	private Grid statemtsGrid = new Grid();
	private Button addToGraphBtn = new Button();
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout statemtsLayout = new VerticalLayout();
	
	public StatementsView() {
		init();
	}
	
	public StatementsView(Indexed conceptsContainer) {
		init();
		conceptsGrid.setContainerDataSource(conceptsContainer);
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
		splitPanel.setSplitPosition(50, Unit.PERCENTAGE);
		
		setSpacing(true);
		setHeight(100, Unit.PERCENTAGE);
		addComponents(splitPanel, addToGraphBtn);
		setExpandRatio(splitPanel, 1);

		initGrid();
	}

	private void initGrid() {
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
