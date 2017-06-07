package bio.knowledge.web.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.service.beacon.GenericKnowledgeService.QueryListener;
import bio.knowledge.service.beacon.KnowledgeBeacon;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class ConsoleWindow extends Window {
	private static final long serialVersionUID = 3645524757035928630L;
	
	private VerticalLayout layout = new VerticalLayout();
	ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<String>();
	List<KnowledgeBeacon> knowledgeBeacons = new ArrayList<KnowledgeBeacon>();
	
	public ConsoleWindow(KnowledgeBeaconService kbService) {
		setCaption("API Console");
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(new Label(String.format("<font size=\"2\">Time stamp, Number of responses, API Query"), ContentMode.HTML));
		mainLayout.addComponent(layout);
		setContent(mainLayout);
		
		mainLayout.setSizeUndefined();
		layout.setSizeFull();
		
		setResizable(true);
		setWidth(60, Unit.PERCENTAGE);
		setHeight(50, Unit.PERCENTAGE);
		
		kbService.setQueryListener(new QueryListener() {
			@Override
			public void getQuery(CompletableFuture<List<Map<String, String>>> future) {
				try {
					List<Map<String, String>> messages = future.get();
					for (Map<String, String> message : messages) {
						String query = message.get("query");
						String errorMessage = message.get("errorMessage");
						String responseCount = message.get("responseCount");
						String timeStamp = message.get("timeStamp");
						
						if (errorMessage == null) {
							if (query != null) {
								HorizontalLayout hlayout = new HorizontalLayout();
								hlayout.setSpacing(true);
								layout.addComponent(hlayout);
								hlayout.addComponent(new Label(timeStamp));
								hlayout.addComponent(new Label(responseCount));
								hlayout.addComponent(new Label(query));
							}
						} else {
							HorizontalLayout hlayout = new HorizontalLayout();
							hlayout.setSpacing(true);
							VerticalLayout vlayout = new VerticalLayout();
							layout.addComponent(vlayout);
							vlayout.addComponent(hlayout);
							hlayout.addComponent(new Label(timeStamp));
							hlayout.addComponent(new Label(responseCount));
							try {
								Label queryLabel = new Label(String.format("<font color=\"red\">") + query, ContentMode.HTML);
								hlayout.addComponent(queryLabel);
								Label errorLabel = new Label(String.format("<font color=\"red\">Error: ") + errorMessage, ContentMode.HTML);
								vlayout.addComponent(errorLabel);
							} catch (Exception e) {
								hlayout.addComponent(new Label("Error occurred for: " + query));
								e.printStackTrace();
							}
							
						}
						
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
