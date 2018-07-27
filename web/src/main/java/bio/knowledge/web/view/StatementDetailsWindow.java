package bio.knowledge.web.view;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.vaadin.event.SelectionEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

import bio.knowledge.client.ApiCallback;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.BeaconStatementAnnotation;
import bio.knowledge.client.model.BeaconStatementCitation;
import bio.knowledge.client.model.BeaconStatementDetails;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconService;

public class StatementDetailsWindow extends Window {
	private ProgressBar progressBar = new ProgressBar();
	private VerticalLayout contents;
	private TreeTable table;
	
	private static final long serialVersionUID = -3816876107728223236L;

	public StatementDetailsWindow(String statementId, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		setCaption(statementId);
		kbService.getStatementDetailsAsync(statementId, null, 1, 1000, createCallback());
	}
	
	public StatementDetailsWindow(RendererClickEvent e, KnowledgeBeaconService kbService, KBQuery kbQuery) {
		init();
		//TODO: fix statementId
		String statementId = e.getItemId().toString();
		setCaption(statementId);
		
		kbService.getStatementDetailsAsync(statementId, null, 1, 1000, createCallback());
	}

	private void init() {
		setHeight(40, Unit.EM);
		setWidth(80, Unit.EM);
		center();
		setResizable(true);
		
		contents = new VerticalLayout();
		table = new TreeTable("Additional Information");
		
		progressBar.setIndeterminate(true);
		contents.addComponent(progressBar);
		contents.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		
		this.setContent(contents);
	}

	private void showDetails(BeaconStatementDetails details) {
		contents.removeAllComponents();
		contents.addComponent(new Label("Id: " + details.getId()));
		contents.addComponent(new Label("Defined by: " + details.getIsDefinedBy()));
		contents.addComponent(new Label("Provided by: " + details.getProvidedBy()));
		contents.addComponent(new Label("Qualifiers: " + String.join(", ", details.getQualifiers())));
		
		showEntriesTable(details.getAnnotation(), details.getEvidence());
	}
	
	private void showEntriesTable(List<BeaconStatementAnnotation> annotations, List<BeaconStatementCitation> citations) {

		TreeTable table = new TreeTable("Additional Information");
		table.addContainerProperty("Title", String.class, "");
		table.addContainerProperty("Value", String.class, null);
		
		Object citationsList = table.addItem(new Object[] {"Citations", "--"}, null);
		table.setCollapsed(citationsList, false);
		
		for (int i = 1; i < citations.size(); i++) {
			BeaconStatementCitation c = citations.get(i);
			
			Object title = table.addItem(new Object[] {"evidence" + i, "--"}, null);
			
			Object id = table.addItem(new Object[] {"id", c.getId()}, null);
			Object name = table.addItem(new Object[] {"name", c.getName()}, null);
			Object uri = table.addItem(new Object[] {"uri", c.getUri()}, null);
			Object type = table.addItem(new Object[] {"evidence type", c.getEvidenceType()}, null);
			Object date = table.addItem(new Object[] {"date", c.getDate()}, null);
			
			table.setChildrenAllowed(id, false);
			table.setChildrenAllowed(name, false);
			table.setChildrenAllowed(uri, false);
			table.setChildrenAllowed(type, false);
			table.setChildrenAllowed(date, false);
			
			table.setParent(id, title);
			table.setParent(name, title);
			table.setParent(uri, title);
			table.setParent(type, title);
			table.setParent(date, title);
		}
		
		Object annotationsList = table.addItem(new Object[] {"Annotations", "--"}, null);
		annotations.sort(Comparator.comparing(BeaconStatementAnnotation::getTag));
		
		for (BeaconStatementAnnotation a : annotations) {
			Object statement = table.addItem(new Object[] {a.getTag(), a.getValue()}, null);
			table.setParent(statement, annotationsList);
			table.setChildrenAllowed(statement, false);
		}

		table.setCollapsed(annotationsList, false);
		
		table.setWidth(98, Unit.PERCENTAGE);
		table.setSortEnabled(false);
		contents.addComponent(table);
		
	}

	private void showError(String msg) {
		contents.removeAllComponents();
		Label label = new Label("Error - Could not find details: " + msg);
		contents.addComponent(label);
	}
	
	private ApiCallback<BeaconStatementDetails> createCallback() {
		return new ApiCallback<BeaconStatementDetails>() {
	
			@Override
			public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
				getUI().access(() -> {
					showError(e.toString());
				});
			}
	
			@Override
			public void onSuccess(BeaconStatementDetails result, int statusCode,
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
