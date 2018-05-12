package bio.knowledge.web.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconConceptsQuery;
import bio.knowledge.client.model.BeaconConceptsQueryResult;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class ResultRowView extends HorizontalLayout implements SearchResultView.Listener {

	private static final long serialVersionUID = -2841062072954936319L;

	private KnowledgeBeaconService kbService;
	private KBQuery kbQuery;

	private Label conceptLabel = new Label("Concept Placeholder");
	private Label timeLabel = new Label("time ago");
	private Button detailsButton = new Button(FontAwesome.CHECK);
	private Button removeButton = new Button(FontAwesome.TIMES);
	private ProgressBar progressBar = new ProgressBar();

	private VerticalLayout titleLayout = new VerticalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	
	private Date creationTime = new Date();
	private PrettyTime p = new PrettyTime();
	private String conceptName = "";
	
	private ApiCallback<BeaconConceptsQuery> callback;
	private BeaconConceptsQuery conceptQuery;
	private BeaconConceptsQueryResult conceptQueryResult;
	
	public ResultRowView(String conceptName) {
		init(conceptName);
	}

	public ResultRowView(String conceptName, KBQuery kbQuery, KnowledgeBeaconService kbService) {
		this.kbQuery = kbQuery;
		this.kbService = kbService;
		init(conceptName);
	}
	
	private void init(String conceptName) {
		this.conceptName = conceptName;
		conceptLabel.setValue("<strong style = \"font-size: 120%;\">" + conceptName + "</strong>");
		conceptLabel.setStyleName("text-overflow");
		conceptLabel.setContentMode(ContentMode.HTML);

		timeLabel.setValue(p.format(creationTime));
		progressBar.setIndeterminate(true);

		setSpacing(true);
		setSizeFull();

		titleLayout.addComponents(timeLabel, conceptLabel);
		titleLayout.setSizeFull();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponents(progressBar, removeButton);

		addComponents(titleLayout, buttonsLayout);
		setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
		setExpandRatio(titleLayout, 1);
		initButtons();
		initCallbacks();
		
		postQuery();
	}
	

	private void initCallbacks() {
		List<Integer> beacons = kbQuery.getCustomBeacons();
		ApiCallback<BeaconConceptsQueryResult> queryCallback = new ApiCallback<BeaconConceptsQueryResult>() {
		
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(BeaconConceptsQueryResult result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				getUI().access(() -> {
					buttonsLayout.replaceComponent(progressBar, detailsButton);
					conceptQueryResult = result;
				});
				
			}

			@Override
			public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
				// TODO Auto-generated method stub
				
			}
		};
		
		callback = new ApiCallback<BeaconConceptsQuery>() {

			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				e.printStackTrace();

			}

			@Override
			public void onSuccess(BeaconConceptsQuery result, int statusCode, Map<String, List<String>> responseHeaders) {
				conceptQuery = result;					
				kbService.getConceptsAsync(result.getQueryId(), beacons, null, null, queryCallback);
			}

			@Override
			public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
			}

			@Override
			public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
			}
		};
	}

	private void initButtons() {
		detailsButton.setStyleName("page-button");
		detailsButton.addClickListener(e -> {
			Window detailsWindow = new Window();
			detailsWindow.setCaption("Matching results");
			detailsWindow.center();
			detailsWindow.setModal(true);
			
			VerticalLayout contentLayout = new VerticalLayout();
			contentLayout.setSpacing(true);
			detailsWindow.setContent(contentLayout);
			
			List<BeaconConcept> results = conceptQueryResult.getResults();
			BeanItemContainer<BeaconConcept> container = new BeanItemContainer<BeaconConcept>(BeaconConcept.class, results);
			Grid grid = new Grid(container);
			contentLayout.addComponent(grid);
			
			getUI().addWindow(detailsWindow);

		});

		removeButton.setStyleName("page-button");
		removeButton.addClickListener(e -> {
			getUI().access(() -> {				
				Layout parent = (Layout) this.getParent();
				parent.removeComponent(this);
			});
		});
	}

	@Override
	public void update() {
		getUI().access(() -> {			
			timeLabel.setValue(p.format(creationTime));
		});
	}

	public void setServices(KBQuery kbQuery, KnowledgeBeaconService kbService) {
		this.kbQuery = kbQuery;
		this.kbService = kbService;
	}

	private void postQuery() {		
		kbService.postConceptsQueryAsync(conceptName, kbQuery.getTypes(), kbQuery.getCustomBeacons(), callback);
	}
}
