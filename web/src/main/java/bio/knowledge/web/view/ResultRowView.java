package bio.knowledge.web.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

//import com.google.common.util.concurrent.FutureCallback;
//import com.google.common.util.concurrent.Futures;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConceptsQuery;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.KBQueryImpl;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class ResultRowView extends HorizontalLayout implements SearchResultView.Listener {

	private static final long serialVersionUID = -2841062072954936319L;

	KnowledgeBeaconService kbService = KnowledgeBeaconService.get();

	KBQuery query = KBQueryImpl.get();

	private Label conceptLabel = new Label("Concept Name");
	private Label timeLabel = new Label("1 min ago");
	private Button detailsButton = new Button(FontAwesome.CHECK);
	private Button removeButton = new Button(FontAwesome.TIMES);

	private Date creationTime = new Date();
	private PrettyTime p = new PrettyTime();
	private String conceptName = "";

	private ProgressBar progressBar = new ProgressBar();

	private VerticalLayout titleLayout = new VerticalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();

	private ApiCallback<BeaconConceptsQuery> callback;

	public ResultRowView(String conceptName) {
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
		initCallback();
		
		postQuery();
	}

	private void initCallback() {
		callback = new ApiCallback<BeaconConceptsQuery>() {

			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				// TODO Auto-generated method stub
				e.printStackTrace();

			}

			@Override
			public void onSuccess(BeaconConceptsQuery result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				System.out.println("resuult is:");
				System.out.println(result);

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
			Notification.show("Concept Details clicked!", Notification.Type.TRAY_NOTIFICATION);
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
	}

	private void replaceProgress() {
		buttonsLayout.replaceComponent(progressBar, detailsButton);
	}

	private void postQuery() {		
		kbService.postConceptsQueryAsync(conceptName, query.getTypes(), query.getCustomBeacons(), callback);
	}
}
