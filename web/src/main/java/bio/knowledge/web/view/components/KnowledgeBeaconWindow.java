package bio.knowledge.web.view.components;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.client.model.BeaconMetadata;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.ui.DesktopUI;

public class KnowledgeBeaconWindow extends Window {
	
	private static final long serialVersionUID = -3216657180755749441L;
	
	private final KnowledgeBeaconRegistry kbRegistry;
	private List<BeaconMetadata> defaultBeacons;

	private OptionGroup optionGroup;

	public KnowledgeBeaconWindow(KnowledgeBeaconRegistry kbRegistry, KBQuery query, KnowledgeBeaconService kbService) {

		this.kbRegistry = kbRegistry;
		defaultBeacons = kbService.getKnowledgeBeacons();
		
		optionGroup = new OptionGroup("Beacons", defaultBeacons);
		
		System.out.println(optionGroup);
		
		setCaption("Knowledge Beacon Tools");
		this.center();
		
		VerticalLayout mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		VerticalLayout chooseKbPanel = buildChooseKbPanel(query);
		chooseKbPanel.addComponent(optionGroup);
		
//		FormLayout addKbPanel = buildAddKbPanel();
//		addKbPanel.setCaption("Add New Knowledge Beacon:");
		
		Button closeButton = new Button();
		closeButton.setCaption("Done");
		closeButton.addClickListener(event -> { close(); });
		
		Button consoleButton = new Button();
		consoleButton.setCaption("Open Console");
		consoleButton.addClickListener(event -> { 
			ConsoleWindow window = new ConsoleWindow(kbService);
			DesktopUI.getCurrent().addWindow(window);
		});
		
		mainLayout.addComponents( optionGroup, closeButton );
		mainLayout.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);
		
		SingleRadioButton recordLogsRadioButton = new SingleRadioButton("Record logs", false);
		Button viewLogs = new Button("View Logs");
		viewLogs.setEnabled(query.hasSessionId());
		recordLogsRadioButton.setChecked(query.hasSessionId());
		
		viewLogs.addClickListener(event -> {
			getUI().getPage().open(kbService.getPublicAggregatorBaseUrl() + "/errorlog?sessionId=" + query.getUserSessionId(), "_blank");
		});
		
		recordLogsRadioButton.addValueChangeListener(event -> {
			if (recordLogsRadioButton.isChecked()) {
				viewLogs.setEnabled(true);
				query.generateUserSessionId();
			} else {
				viewLogs.setEnabled(false);
				query.clearUserSessionId();
			}
		});
		
		mainLayout.addComponent(recordLogsRadioButton);
		mainLayout.addComponent(viewLogs);
	}

	private VerticalLayout buildChooseKbPanel(KBQuery query) {
		
		VerticalLayout panel = new VerticalLayout();
		
		optionGroup.setCaption("Set Active Knowledge Beacons:");
		
		refreshOptionGroup(query.getCustomBeacons());
		
		optionGroup.addValueChangeListener(event -> {
			
			@SuppressWarnings("unchecked")
			Collection<BeaconMetadata> selected = 
					(Collection<BeaconMetadata>) event.getProperty().getValue();
			
			List<String> beaconIds = new ArrayList<String>();
			beaconIds.addAll(
					selected.stream().map(
							beacon -> beacon.getId()
						).collect(Collectors.toList())
			);
			
			query.setCustomBeacons(beaconIds);
			
		});
		
		panel.addComponent(optionGroup);
		
		return panel;
	}

	/*
	 * Users don't add ad hoc beacons to TKBio anymore but register them
	 * outside the system, at Github NCATS-Tangerine/translator-knowledge-beacon
	 * or perhaps, at Smart-API.info?
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private FormLayout buildAddKbPanel(KBQuery query) {
		
		FormLayout flayout = new FormLayout();
		flayout.setMargin(false);
		flayout.setWidth(3, Unit.INCH);
		
		TextField nameField = new TextField("Name");
		TextField urlField = new TextField("URL");
		TextArea descrArea = new TextArea("Description");	
		
		nameField.setWidth("100%");
		urlField.setWidth("100%");
		descrArea.setWidth("100%");
		
		urlField.addValidator(value -> {
			String url = (String) value;
			
			if (url.isEmpty()) return;
			
			try {
				URL trueUrl = new URL(url);
				trueUrl.toURI();
			} catch (MalformedURLException | URISyntaxException e) {
				throw new InvalidValueException(url + " is not a valid URL");
			}
			
			if (url.endsWith("/")) {
				throw new InvalidValueException("URL must not end in /");
			}
		});
		
		Button addButton = new Button();
		addButton.setCaption("Add Beacon");
		
		addButton.addClickListener(event -> {
//				kbRegistry.addKnowledgeBeacon("name", "description", textField.getValue());
//				Notification.show("Knowledge beacon " + textField.getValue() + " added");
//				
			String name = nameField.getValue();
			String description = descrArea.getValue();
			String url = urlField.getValue();
			
			if (url.isEmpty()) return;
			
			try {
				urlField.validate();
			} catch (InvalidValueException e) {
				return;
			}
			
			kbRegistry.addKnowledgeBeacon(name, description, url);
			
			refreshOptionGroup(query.getCustomBeacons());
		});
	
		flayout.addComponents(nameField, urlField, descrArea, addButton);
		flayout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
		
		return flayout;
	}
	
	private void refreshOptionGroup(List<String> customBeacons) {
		
		optionGroup.removeAllItems();
		optionGroup.setMultiSelect(true);
		
		for (BeaconMetadata kb : defaultBeacons) {
			
			optionGroup.addItem(kb);
			optionGroup.setItemCaption(kb, kb.getName() + " - " + kb.getUrl());
			
			if( customBeacons.isEmpty() || customBeacons.contains(kb.getId())) 
				optionGroup.select(kb);
		}
	}
	
}
