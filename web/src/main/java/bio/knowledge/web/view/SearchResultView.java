package bio.knowledge.web.view;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.client.model.BeaconConcept;

public class SearchResultView extends VerticalLayout {

	private static final long serialVersionUID = 1918173093105535900L;

	private Grid resultGrid;
	private Button statementBtn = new Button();

	public SearchResultView(List<BeaconConcept> results) {
		initGrid(results);
		initButton();
		
		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(statementBtn);
		
		setSpacing(true);
		addComponents(resultGrid, buttonLayout);
	}

	private void initGrid(List<BeaconConcept> results) {
		BeanItemContainer<BeaconConcept> container = new BeanItemContainer<BeaconConcept>(BeaconConcept.class, results);
		resultGrid = new Grid(container);
		resultGrid.setSelectionMode(SelectionMode.SINGLE);
		resultGrid.setCellDescriptionGenerator(cell -> {
			String value = cell.getValue().toString();
			return "<strong style = \"font-size: 120%;\">" + value + "</strong>";
		});
		resultGrid.addSelectionListener(e -> {
			boolean isEmpty = e.getSelected().isEmpty();
			statementBtn.setEnabled(isEmpty ? false : true);
		});
	}

	private void initButton() {
		statementBtn.setCaptionAsHtml(true);
		statementBtn.setCaption("<strong style = \"font-size: 120%;\">Go</strong>");
		statementBtn.setEnabled(false);
		statementBtn.setWidth(100, Unit.PERCENTAGE);
		statementBtn.addClickListener(e -> {
			BeaconConcept concept = (BeaconConcept) resultGrid.getSelectedRow();
			System.out.println(concept);
			getUI().getNavigator().navigateTo(DesktopView.NAME + "/" + "statements" + "/" + concept.getClique());
		});
	}

}
