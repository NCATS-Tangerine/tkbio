package bio.knowledge.web.view.components;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

import bio.knowledge.service.KBQuery;
import bio.knowledge.service.beacon.KnowledgeBeacon;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;
import bio.knowledge.web.ui.DesktopUI;

public class KnowledgeBeaconWindow extends Window {
	
	private final KnowledgeBeaconRegistry kbRegistry;
	private final KBQuery query;
	private OptionGroup optionGroup = new OptionGroup();

	public KnowledgeBeaconWindow(KnowledgeBeaconRegistry kbRegistry, KBQuery query) {
		this.query = query;
		this.kbRegistry = kbRegistry;
		
		setCaption("Knowledge Beacon Tools");
		this.center();
		
		VerticalLayout mainLayout = new VerticalLayout();
		this.setContent(mainLayout);
		
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		FormLayout addKbPanel = buildAddKbPanel();
		VerticalLayout chooseKbPanel = buildChooseKbPanel();
		
		mainLayout.addComponents(addKbPanel, chooseKbPanel);
	}

	private VerticalLayout buildChooseKbPanel() {
		VerticalLayout panel = new VerticalLayout();
		optionGroup.setCaption("Set active knowledge beacons");
		
		refreshOptionGroup();
		
		optionGroup.addValueChangeListener(new ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Collection<KnowledgeBeacon> selected = 
						(Collection<KnowledgeBeacon>) event.getProperty().getValue();
				
				for (KnowledgeBeacon k : kbRegistry.getKnowledgeBeacons()) {
					k.setEnabled(false);
				}
				
				for (KnowledgeBeacon k : selected) {
					k.setEnabled(true);
				}
			}
			
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
		
		urlField.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
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
			}
			
		});
		
		Button addButton = new Button();

		addButton.setCaption("Add Beacon");
		
		addButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
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
			}
			
		});
	
		flayout.addComponents(nameField, urlField, descrArea, addButton);
		flayout.setComponentAlignment(addButton, Alignment.BOTTOM_RIGHT);
		
		return flayout;
	}
	
	private void refreshOptionGroup() {
		List<KnowledgeBeacon> kbs = kbRegistry.getKnowledgeBeacons();
		optionGroup.removeAllItems();
		optionGroup.setMultiSelect(true);
		for (KnowledgeBeacon kb : kbs) {
			optionGroup.addItem(kb);
			optionGroup.setItemCaption(kb, kb.getUrl());
			if (kb.isEnabled())
				optionGroup.select(kb);
		}
	}
	
}
