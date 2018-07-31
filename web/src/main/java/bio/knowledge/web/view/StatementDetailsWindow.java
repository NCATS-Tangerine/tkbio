package bio.knowledge.web.view;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconStatementAnnotation;
import bio.knowledge.client.model.BeaconStatementCitation;
import bio.knowledge.client.model.BeaconStatementDetails;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class StatementDetailsWindow extends Window {
	
	private static final float WIDTH = 80;
	private static final float HEIGHT = 40;
	private static final String TITLE = "Statement Details: ";
	
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout contents;
	
	private static final long serialVersionUID = -3816876107728223236L;

	public StatementDetailsWindow(String statementId, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		setCaption(TITLE + statementId);
		kbService.getStatementDetailsAsync(statementId, null, 1, 1000, createCallback());
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

	public void showDetails(BeaconStatementDetails details) {
		contents.removeAllComponents();
		contents.addComponent(new Label("Statement ID: " + details.getId()));
		contents.addComponent(new Label("Defined by: " + details.getIsDefinedBy()));
		contents.addComponent(new Label("Provided by: " + details.getProvidedBy()));
		contents.addComponent(new Label("Qualifiers: " + String.join(", ", details.getQualifiers())));
		
		showEntriesTable(details.getAnnotation(), details.getEvidence());
	}
	
	private void showEntriesTable(List<BeaconStatementAnnotation> annotations, List<BeaconStatementCitation> citations) {
		
		DetailsTreeTable table = new DetailsTreeTable("Additional Information");

		Object citationsList = table.addTitleRow("Publications");
		for (int i = 1; i < citations.size(); i++) {
			BeaconStatementCitation c = citations.get(i);
			
			Object title = table.addTitleRow(String.valueOf(i), citationsList);
			table.addInfoRow("id", c.getId(), title);
			table.addInfoRow("name",c.getName(), title);
			table.addInfoRow("uri", c.getUri(), title);
			table.addInfoRow("evidence type", c.getEvidenceType(), title);
			table.addInfoRow("date", c.getDate(), title);
		}
		
		Object annotationsList = table.addTitleRow("Additional Annotations");
		annotations.sort(Comparator.comparing(BeaconStatementAnnotation::getTag));
		
		for (BeaconStatementAnnotation a : annotations) {
			table.addInfoRow(a.getTag(), a.getValue(), annotationsList);
		}
		
		contents.addComponent(table);
	}

	public void showError(String msg) {
		contents.removeAllComponents();
		Label label = new Label("Error - Could not find details: " + msg);
		contents.addComponent(label);
	}
	
	private ApiCallback<BeaconStatementDetails> createCallback() {
		return new DetailsCallback().createStatementDetailsCallback(this);
	}
	
}
