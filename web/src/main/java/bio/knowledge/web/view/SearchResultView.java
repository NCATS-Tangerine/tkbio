package bio.knowledge.web.view;

import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.client.model.BeaconConcept;

public class SearchResultView extends VerticalLayout {

	private static final long serialVersionUID = 1918173093105535900L;

	private Grid resultGrid;
	private Button addToMapBtn = new Button();

	public SearchResultView(List<BeaconConcept> results) {
		initGrid(results);
		initButton();
		
		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(addToMapBtn);
		
		setSpacing(true);
		addComponents(resultGrid, buttonLayout);
	}

	private void initGrid(List<BeaconConcept> results) {
		BeanItemContainer<BeaconConcept> container = new BeanItemContainer<BeaconConcept>(BeaconConcept.class, results);
		resultGrid = new Grid(container);
		resultGrid.setSelectionMode(SelectionMode.MULTI);
		resultGrid.setCellDescriptionGenerator(cell -> {
			String value = cell.getValue().toString();
			return "<strong style = \"font-size: 120%;\">" + value + "</strong>";
		});
		resultGrid.addSelectionListener(e -> {
			boolean isEmpty = e.getSelected().isEmpty();
			addToMapBtn.setEnabled(isEmpty ? false : true);
		});
	}

	private void initButton() {
		addToMapBtn.setCaptionAsHtml(true);
		addToMapBtn.setCaption("<strong style = \"font-size: 120%;\">Add to graph</strong>");
		addToMapBtn.setEnabled(false);
		addToMapBtn.setWidth(100, Unit.PERCENTAGE);
		addToMapBtn.addClickListener(e -> {
			Collection<Object> concepts = resultGrid.getSelectedRows();
			
		});
	}

}
