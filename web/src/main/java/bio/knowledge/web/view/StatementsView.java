package bio.knowledge.web.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellDescriptionGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

public class StatementsView extends VerticalLayout {
	
	public final String NAME = "statements";

	private static final long serialVersionUID = -7988756397144422937L;
	
	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	private Grid conceptsGrid = new Grid();
	private Grid statemtsGrid = new Grid();
	private Button addToGraphBtn = new Button();
	private ProgressBar progressBar = new ProgressBar();
	
	public StatementsView() {
		conceptsGrid.setSelectionMode(SelectionMode.SINGLE);
		conceptsGrid.setCellDescriptionGenerator(getCellDescriptionGenerator());
		statemtsGrid.setSelectionMode(SelectionMode.MULTI);
		statemtsGrid.setCellDescriptionGenerator(getCellDescriptionGenerator());		
		
		addToGraphBtn.setDescription("Add to Graph");
		addToGraphBtn.setEnabled(false);
		addToGraphBtn.setWidth(100, Unit.PERCENTAGE);
		
		progressBar.setIndeterminate(true);
		
		splitPanel.setFirstComponent(conceptsGrid);
		splitPanel.setSecondComponent(statemtsGrid);
		splitPanel.setSplitPosition(50, Unit.PERCENTAGE);
		
		setSpacing(true);
		addComponents(splitPanel, addToGraphBtn);
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
	
	public HorizontalSplitPanel getSplitPanel() {
		return splitPanel;
	}
	
	public void showProgress() {
		splitPanel.replaceComponent(statemtsGrid, progressBar);
	}
	
	public void hideProgress() {
		splitPanel.replaceComponent(progressBar, statemtsGrid);		
	}
}
