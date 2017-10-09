package bio.knowledge.web.view.components;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import bio.knowledge.client.model.KnowledgeBeacon;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;
import bio.knowledge.service.beacon.KnowledgeBeaconService;
import bio.knowledge.web.ui.DesktopUI;

public class KnowledgeBeaconWindow extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3216657180755749441L;
	
	private final KnowledgeBeaconRegistry kbRegistry;
	private final KnowledgeBeaconService kbService;
	
	private List<KnowledgeBeacon> defaultBeacons;
	private String sessionId = RandomStringUtils.randomAlphanumeric(20);

	private OptionGroup optionGroup;

	public KnowledgeBeaconWindow(KnowledgeBeaconRegistry kbRegistry, KBQuery query, KnowledgeBeaconService kbService) {
		this.kbRegistry = kbRegistry;
		this.kbService = kbService;
		
		defaultBeacons = kbService.getKnowledgeBeacons();
		
		optionGroup = new OptionGroup("Beacons", defaultBeacons);
		
		System.out.println(optionGroup);
		
		setCaption("Knowledge Beacon Tools");
		this.center();
		
		VerticalLayout mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		VerticalLayout chooseKbPanel = buildChooseKbPanel();
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
		
		SingleRadioButton radioButton = new SingleRadioButton("Record logs", false);
		Button viewLogs = new Button("View Logs");
		viewLogs.setEnabled(kbService.hasSessionId());
		radioButton.setChecked(kbService.hasSessionId());
		
		viewLogs.addClickListener(event -> {
			getUI().getPage().open(kbService.getAggregatorBaseUrl() + "/errorlog?sessionId=" + sessionId, "_blank");
		});
		
		radioButton.addValueChangeListener(event -> {
			if (radioButton.isChecked()) {
				viewLogs.setEnabled(true);
				kbService.setSessionId(sessionId);
			} else {
				viewLogs.setEnabled(false);
				kbService.clearSessionId();
			}
		});
		
		mainLayout.addComponent(radioButton);
		mainLayout.addComponent(viewLogs);
		
		
	}

	private VerticalLayout buildChooseKbPanel() {
		VerticalLayout panel = new VerticalLayout();
		optionGroup.setCaption("Set Active Knowledge Beacons:");
		
		refreshOptionGroup();
		
		optionGroup.addValueChangeListener(event -> {
			
			@SuppressWarnings("unchecked")
			Collection<KnowledgeBeacon> selected = 
					(Collection<KnowledgeBeacon>) event.getProperty().getValue();
			
			List<String> beaconIds = new ArrayList<String>();
			beaconIds.addAll(
					selected.stream().map(
							beacon -> beacon.getId()
						).collect(Collectors.toList())
			);
			
			kbService.setCustomBeacons(beaconIds);
			
		});
		
		panel.addComponent(optionGroup);
		
		return panel;
	}

	private FormLayout buildAddKbPanel() {
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
			refreshOptionGroup();
		});
	
		flayout.addComponents(nameField, urlField, descrArea, addButton);
		flayout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
		
		return flayout;
	}
	
	private void refreshOptionGroup() {
		optionGroup.removeAllItems();
		optionGroup.setMultiSelect(true);
		for (KnowledgeBeacon kb : defaultBeacons) {
			optionGroup.addItem(kb);
			optionGroup.setItemCaption(kb, kb.getName() + " - " + kb.getUrl());
			optionGroup.select(kb);
		}
	}
	
}
