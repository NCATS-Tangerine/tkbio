package bio.knowledge.web.view;

import java.util.List;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconConcept;
import bio.knowledge.client.model.BeaconConceptDetail;
import bio.knowledge.client.model.BeaconConceptWithDetails;
import bio.knowledge.client.model.BeaconConceptWithDetailsBeaconEntry;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class ConceptDetailsWindow extends Window {
	
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout contents;
	
	private static final long serialVersionUID = -3816876107728223236L;

	public ConceptDetailsWindow(RendererClickEvent e, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		BeaconConcept concept = (BeaconConcept) e.getItemId();
		setCaption(concept.getClique());
		kbService.getConceptDetailsAsync(concept.getClique(), kbQuery.getCustomBeacons(), createCallback());
	}

	private void init() {
		setHeight(40, Unit.EM);
		setWidth(80, Unit.EM);
		center();
		setResizable(true);
		
		contents = new VerticalLayout();
		progressBar.setIndeterminate(true);
		contents.addComponent(progressBar);
		contents.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		this.setContent(contents);
	}

	private void showDetails(BeaconConceptWithDetails details) {
		contents.removeAllComponents();
		contents.addComponent(new Label("Clique: " + details.getClique()));
		contents.addComponent(new Label("Name: " + details.getName()));
		contents.addComponent(new Label("Aliases: " + String.join(", ", details.getAliases())));
		contents.addComponent(new Label("Categories: " + String.join(", ", details.getCategories())));
		
		List<BeaconConceptWithDetailsBeaconEntry> entries = details.getEntries();
		showEntriesTable(entries);
	}
	
	private void showEntriesTable(List<BeaconConceptWithDetailsBeaconEntry> entries) {
		
		DetailsTreeTable table = new DetailsTreeTable("Additional Information");
		
		for (BeaconConceptWithDetailsBeaconEntry e : entries) {
			Object title = table.addTitleRow("Beacon " + e.getBeacon() + ": " + e.getId());
			table.addInfoRow("id", e.getId(), title);
			table.addInfoRow("definition", e.getDefinition(), title);
			table.addInfoRow("synonyms", String.join(", ", e.getSynonyms()), title);

			Object annotations = table.addTitleRow("details", title);
			
			for (BeaconConceptDetail d : e.getDetails()) {
				table.addInfoRow(d.getTag(), d.getValue(), annotations);
			}
		}
		contents.addComponent(table);
	}

	private void showError(String msg) {
		contents.removeAllComponents();
		Label label = new Label("Error - Could not find details: " + msg);
		contents.addComponent(label);
	}
	
	private ApiCallback<BeaconConceptWithDetails> createCallback() {
		return new ApiCallback<BeaconConceptWithDetails>() {
	
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				getUI().access(() -> {
					showError(e.toString());
				});
			}
	
			@Override
			public void onSuccess(BeaconConceptWithDetails result, int statusCode,
					Map<String, List<String>> responseHeaders) {
				getUI().access(() -> {
					showDetails(result);
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
	
}

//public class ConceptDetailsLayout extends VerticalLayout {
//	
//	private ProgressBar progressBar = new ProgressBar();
//	
//	private static final long serialVersionUID = -3816876107728223236L;
//
//	public ConceptDetailsLayout(RendererClickEvent e, KnowledgeBeaconService kbService, KBQuery kbQuery) {
//		setMargin(true);
//		showProgress();
//		BeaconConcept concept = (BeaconConcept) e.getItemId();
////		List<Integer> beacons = new ArrayList<>();
////		beacons.add(1);
////		Optional<BeaconConceptWithDetails> optional = kbService.getConceptDetails(concept.getClique(), kbQuery.getCustomBeacons());
////		if (optional.isPresent()) {
////			BeaconConceptWithDetails details = optional.get();
////			removeProgress();
////			showDetails(details);
////		} else {
////			removeProgress();
////			showError(concept.getClique());
////		}
//		
//		
//		kbService.getConceptDetailsAsync(concept.getClique(), kbQuery.getCustomBeacons(), createCallback());
//		
//	}
//
//	private void showDetails(BeaconConceptWithDetails details) {
//		addComponent(new Label(details.getClique()));
//		addComponent(new Label(details.getName()));
//		addComponent(new Label(String.join(", ", details.getAliases())));
//		addComponent(new Label(String.join(", ", details.getCategories())));
//		
//		Grid grid = new Grid("Info");
//		addComponent(grid);
//		
////		GeneratedPropertyContainer wrapperContainer = new GeneratedPropertyContainer(indexed);
////        wrapperContainer.removeContainerProperty("id");
////        setContainerDataSource(wrapperContainer);
////        getColumns().stream().forEach(c -> c.setSortable(false));
////		
////		addComponent(infoGrid);
////		infoGrid.setSizeFull();
////		infoGrid.addColumn("Clique");
////		infoGrid.addColumn("Name");
////		infoGrid.addColumn("Aliases");
////		infoGrid.addColumn("Categories");
////		List<BeaconConceptWithDetailsBeaconEntry> entries = details.getEntries();
////		for (BeaconConceptWithDetailsBeaconEntry e : entries) {
////			infoGrid.addRow(e.getBeacon(), e.getId(), e.getDefinition(), e.getSynonyms());
////		}
////		
//		
//	}
//	
//	private void showError(String msg) {
//		Label label = new Label("Error - Could not find details: " + msg);
//		addComponent(label);
//		this.markAsDirty();
//	}
//
//	private void removeProgress() {
//		removeComponent(progressBar);
//	}
//
//	private void showProgress() {
//		progressBar.setIndeterminate(true);
//		addComponent(progressBar);
//		setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
//	}
//
//	private void init() {
//	}
//	
//	private ApiCallback<BeaconConceptWithDetails> createCallback() {
//		return new ApiCallback<BeaconConceptWithDetails>() {
//	
//			@Override
//			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
//				// TODO Auto-generated method stub
//				removeProgress();
//				showError(e.toString());
//			}
//	
//			@Override
//			public void onSuccess(BeaconConceptWithDetails result, int statusCode,
//					Map<String, List<String>> responseHeaders) {
//				removeProgress();
//				showDetails(result);
//			}
//	
//			@Override
//			public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
//				// TODO Auto-generated method stub	
//			}
//	
//			@Override
//			public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
//				// TODO Auto-generated method stub
//			}
//		};
//	}
//	
//}
