package bio.knowledge.web.view;

import javax.annotation.PostConstruct;

import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends BaseView {

	public static final String NAME = "relations";
	private static final int ROWS_TO_DISPLAY = 11;
	private static final String STYLE = "results-grid";
	
	private GeneratedPropertyContainer gpContainer;
	
	private Grid dataTable;
	
	@PostConstruct
	protected void initialize() {
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		removeAllComponents();
		
		dataTable = new Grid();
		dataTable.setWidth("100%");
		dataTable.setHeightMode(HeightMode.ROW);
		dataTable.setHeightByRows(ROWS_TO_DISPLAY);
		dataTable.setImmediate(true);
		dataTable.addStyleName(STYLE);
//		dataTable.setCellStyleGenerator(cellRef -> getStyle(cellRef));
		dataTable.setSelectionMode(SelectionMode.MULTI);
		
		
		
	}

}
