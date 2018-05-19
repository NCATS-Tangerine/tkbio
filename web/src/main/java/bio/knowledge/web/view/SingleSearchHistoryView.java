package bio.knowledge.web.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.http.HttpStatus;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConceptsQuery;
import bio.knowledge.client.model.BeaconConceptsQueryBeaconStatus;
import bio.knowledge.client.model.BeaconConceptsQueryResult;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class SingleSearchHistoryView extends HorizontalLayout implements SearchHistoryView.Listener {

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
	private boolean done = false;
	
	private ApiCallback<BeaconConceptsQueryResult> resultCb;
	private List<Integer> beacons;
	private List<BeaconConceptsQueryBeaconStatus> beaconStatuses;
	private BeaconConceptsQuery conceptQuery;
	private BeaconConceptsQueryResult conceptQueryResult;
	
	public SingleSearchHistoryView(String conceptName) {
		init(conceptName);
	}

	public SingleSearchHistoryView(String conceptName, KBQuery kbQuery, KnowledgeBeaconService kbService) {
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
		
		beacons = kbQuery.getCustomBeacons();
		
		initButtons();
		initCallbacks();
		
		postQuery();
	}
	

	private void initCallbacks() {
		resultCb = new ApiCallback<BeaconConceptsQueryResult>() {
		
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				e.printStackTrace();
				
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
	}

	private void initButtons() {
		detailsButton.setStyleName("page-button");
		detailsButton.addClickListener(e -> {
			Window detailsWindow = new Window();
			detailsWindow.setCaption("Matching results");
			detailsWindow.center();
			detailsWindow.setModal(true);
			detailsWindow.setContent(new SearchResultView(conceptQueryResult.getResults()));
			getUI().addWindow(detailsWindow);

		});

		removeButton.setStyleName("page-button");
		removeButton.addClickListener(e -> {
			Layout parent = (Layout) this.getParent();
			parent.removeComponent(this);
		});
	}

	@Override
	public void update() {
		timeLabel.setValue(p.format(creationTime));
		if (!done) {
			boolean ready = checkStatus();
			if (ready) {
				beacons = getResultBeacons(beaconStatuses);
				kbService.getConceptsAsync(conceptQuery.getQueryId(), beacons, 1, 500, resultCb);
				done = true;
			}
		}
	}

	private boolean checkStatus() {
		beaconStatuses = kbService.getConceptsQueryStatus(conceptQuery.getQueryId(), beacons);
		boolean ready = true;
		for (BeaconConceptsQueryBeaconStatus status : beaconStatuses) {
			if (status.getStatus() == HttpStatus.PROCESSING.value()) {
				ready = false;
				break;
			}
		}
		return ready;
	}
	
	private List<Integer> getResultBeacons(List<BeaconConceptsQueryBeaconStatus> beaconStatuses) {
		List<Integer> beacons = new ArrayList<>();
		for (BeaconConceptsQueryBeaconStatus beaconStatus : beaconStatuses) {
			if (beaconStatus.getStatus() == HttpStatus.OK.value()) {
				Integer beaconId = beaconStatus.getBeacon();
				beacons.add(beaconId);
			}
		}
		return beacons;
	}
	
	public void setServices(KBQuery kbQuery, KnowledgeBeaconService kbService) {
		this.kbQuery = kbQuery;
		this.kbService = kbService;
	}

	private void postQuery() {		
		conceptQuery = kbService.postConceptsQuery(conceptName, kbQuery.getTypes(), kbQuery.getCustomBeacons());
	}
}
