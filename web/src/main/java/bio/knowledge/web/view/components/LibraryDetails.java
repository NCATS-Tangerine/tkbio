/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.web.view.components;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.authentication.AuthenticationContext;
import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.authentication.UserProfile;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.ui.DesktopUI;


public class LibraryDetails extends VerticalLayout {
	
	private static final long serialVersionUID = 6011165323751217447L;
	
	private AuthenticationContext context;

	// these aren't autowired because this is not a spring @component.
	// TODO: Refactor into Spring Component?
	private KBQuery query;
	private ConceptMapArchiveService conceptMapArchiveService;
	
	private ClickListener onGoBackClickListener;
	
	String userId;
	boolean isPublic;
	
	public LibraryDetails(
			ConceptMapArchive map,
			KBQuery query,
			AuthenticationContext context,
			ClickListener onGoBackClickListener
	) {
		this.onGoBackClickListener = onGoBackClickListener;
		this.context = context;
		this.query = query;
		this.conceptMapArchiveService = ((DesktopUI) UI.getCurrent()).getConceptMapArchiveService();
		this.userId = map.getAuthorsAccountId();
		this.isPublic = map.isPublic();
		this.setSizeFull();

		update(map);
	}

	public void update(ConceptMapArchive map) {
		Button addToMapBtn = new Button("Add to map");
		addToMapBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				DesktopUI ui = (DesktopUI) UI.getCurrent();
				ui.loadMap(map);
				query.addImportedMap(map);
				ui.closeLibraryWindow();
				ui.gotoStatementsTable();
			}

		});

		Button backBtn = new Button("Go back");
		backBtn.addClickListener(onGoBackClickListener);

		Button exportBtn = new Button("Export");
		exportBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				ExportWindow exportWindow = new ExportWindow(query, map);
				UI.getCurrent().addWindow(exportWindow);
			}

		});
//
//		exportBtn.addClickListener(new ClickListener() {
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				((DesktopUI) UI.getCurrent()).saveMapToFile(false, map);
//			}
//
//		});

		HorizontalLayout buttonBar = new HorizontalLayout();

		TextArea commentsField = new TextArea();
		commentsField.setRows(5);
		commentsField.setSizeFull();
		
		Label title = this.formatDataTableLabel("Viewing ", map.getName(), " details");
		title.addStyleName("predication-label");

		ExternalResource url = new ExternalResource(map.getConceptMapPng());
		Image image = new Image("", url);
		image.setStyleName("map-preview-image");
		image.setSizeFull();
		
		int numberOfProperties = 5;
		GridLayout infoGrid = new GridLayout(2, numberOfProperties);
		infoGrid.setSpacing(true);
		
		String userId = map.getAuthorsAccountId();
		String name;
		
		if (userId != null) {
			name = context.getUserProfile(userId).getUsername();
		} else {
			name = "an anonymous user";
		}
		
		String rootUrl = ((DesktopUI)UI.getCurrent()).getAuthenticationContext().getRootURL();
		if (!rootUrl.endsWith("/")) rootUrl = rootUrl + "/";
		TextField linkField = new TextField();
		linkField.setValue(rootUrl + "#map=" + map.getName());
		linkField.setWidth("100%");
		linkField.setReadOnly(true);
		
		VerticalLayout linkLayout = new VerticalLayout();
		linkLayout.addComponent(new Label("Show others this map by sharing the following link:"));
		linkLayout.addComponent(linkField);
		linkLayout.setWidth("100%");
		
		infoGrid.addComponent(new Label("Concept map name"));
		infoGrid.addComponent(new Label(map.getName()));
		infoGrid.addComponent(new Label("Authored by"));
		infoGrid.addComponent(new Label(name));
		infoGrid.addComponent(new Label("Date created"));
		infoGrid.addComponent(new Label(map.getDateCreated()));
		infoGrid.addComponent(new Label("Date last modified"));
		infoGrid.addComponent(new Label(map.getDateLastModified()));

//		createdLabel.addStyleName("details-label-style");
//		dateCreatedLabel.setStyleName("details-label-style");
		// dateLastModifiedLabel.setStyleName("details-label-style");
		commentsField.setStyleName("details-label-style");

		this.addComponent(title);
		this.addComponent(image);
		this.addComponent(buttonBar);
//		this.addComponent(new EditableLabel("THIS IS A LABEL"));
		this.addComponent(infoGrid);
		this.addComponent(commentsField);
		this.addComponent(linkLayout);
		buttonBar.addComponent(addToMapBtn);
		buttonBar.addComponent(exportBtn);
		buttonBar.addComponent(backBtn);

		addToMapBtn.setSizeFull();
		backBtn.setSizeFull();
		buttonBar.setSizeFull();
		exportBtn.setSizeFull();
		buttonBar.setExpandRatio(addToMapBtn, 3);
		buttonBar.setExpandRatio(backBtn, 1);
		buttonBar.setExpandRatio(exportBtn, 1);
		buttonBar.setSpacing(true);
		this.setSpacing(true);
		this.setMargin(true);

		this.setWidth("100%");
		this.setHeight(null);

		commentsField.setValue(map.getComments());
		commentsField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (mapCreatedByCurrntUser(map)) {
					map.setComments(commentsField.getValue());
					
					conceptMapArchiveService.save(map, userId, isPublic);
				}
			}

		});
		
		if (mapCreatedByCurrntUser(map)) {
			commentsField.setReadOnly(false);
		} else {
			commentsField.setReadOnly(true);
		}
		
		commentsField.setCaption("Description:");
		commentsField.setInputPrompt("The author has not provided any description.");
	}

	public Label formatDataTableLabel(String prefix, String middle, String suffix) {
		String html = "";

		if (!prefix.isEmpty())
			html += "<span class=\"data-table-label-regular\">" + prefix + "</span>&nbsp;";

		if (!middle.isEmpty())
			html += "<span class=\"data-table-label-highlight\">'" + middle + "'</span>";

		if (!suffix.isEmpty())
			html += "&nbsp;<span class=\"data-table-label-regular\">" + suffix + "</span>";

		return new Label(html, ContentMode.HTML);
	}
	
	private boolean mapCreatedByCurrntUser(ConceptMapArchive map) {
		AuthenticationManager auth = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
		UserProfile userProfile = auth.getCurrentUser();
		
		if (userProfile != null) {
			return userProfile.getId().equals(map.getAuthorsAccountId());
		} else {
			return false;
		}
	}
}
