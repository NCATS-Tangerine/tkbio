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
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class ConceptDetailsWindow extends Window {
	
	private static final float WIDTH = 80;
	private static final float HEIGHT = 40;
	private static final String TITLE = "Concept Details: ";
	
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout contents;
	
	private static final long serialVersionUID = -3816876107728223236L;

	public ConceptDetailsWindow(RendererClickEvent e, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		BeaconConcept concept = (BeaconConcept) e.getItemId();
		setCaption(TITLE + concept.getClique());
		kbService.getConceptDetailsAsync(concept.getClique(), kbQuery.getCustomBeacons(), createCallback());
	}
	
	public ConceptDetailsWindow(IdentifiedConcept concept, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		String cliqueId = concept.getCliqueId();
		setCaption(TITLE + cliqueId + " (" + concept.getName() + ")");
		kbService.getConceptDetailsAsync(cliqueId, kbQuery.getCustomBeacons(), createCallback());
	}

	private void init() {
		setHeight(HEIGHT, Unit.EM);
		setWidth(WIDTH, Unit.EM);
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
