package bio.knowledge.web.view;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid.SelectionMode;

import bio.knowledge.grid.Grid;
import bio.knowledge.grid.Grid.ScrollListener;
import bio.knowledge.model.Statement;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

@SpringView(name = RelationsView.NAME)
public class RelationsView extends BaseView {

	private static final long serialVersionUID = -5096735414490820214L;
	
	public static final String NAME = "relations";
	private static final int ROWS_TO_DISPLAY = 11;
	private static final String STYLE = "results-grid";
	private static final int DATAPAGE_SIZE = 100;
	private static final long TIME_OUT = 60;
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	
	@Autowired
	KnowledgeBeaconService kbService;
	
	private BeanItemContainer<Statement> container = new BeanItemContainer<Statement>(Statement.class);
	private GeneratedPropertyContainer gpContainer = new GeneratedPropertyContainer(container);
	
	private Grid dataTable;
	
	int numberOfPages = 1;
	
	@PostConstruct
	protected void initialize() {
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		removeAllComponents();
		
		dataTable = new Grid(new ScrollListener(){

			@Override
			public void scrolledToBottom() {
				loadDataPage(numberOfPages);
			}
			
		});
		
		dataTable.setWidth("100%");
		dataTable.setHeightMode(HeightMode.ROW);
		dataTable.setHeightByRows(ROWS_TO_DISPLAY);
		dataTable.setImmediate(true);
		dataTable.addStyleName(STYLE);
//		dataTable.setCellStyleGenerator(cellRef -> getStyle(cellRef));
		dataTable.setSelectionMode(SelectionMode.MULTI);
		
		dataTable.setContainerDataSource(gpContainer);
		
		this.addComponent(dataTable);
		
		loadDataPage(0);
		
	}
	
	private void loadDataPage(int pageNumber) {
		
		List<String> beacons = query.getCustomBeacons();
		String sessionId     = query.getUserSessionId();
		String conceptId     = query.getCurrentQueryConceptId();
		
		CompletableFuture<List<Statement>> future = 
				kbService.getStatements(
									conceptId, 
									null, 
									null, 
									null, 
									pageNumber, 
									DATAPAGE_SIZE,
									beacons,
									sessionId
								);
		
		try {
			List<Statement> statements = future.get(TIME_OUT, TIME_UNIT);
			container.addAll(statements);
			numberOfPages++;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
	}

}
