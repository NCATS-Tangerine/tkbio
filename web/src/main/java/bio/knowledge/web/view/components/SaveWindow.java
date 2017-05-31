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

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.authentication.exceptions.AuthenticationException;
import bio.knowledge.graph.ConceptMapDisplay;
import bio.knowledge.graph.ContentRequester;
import bio.knowledge.model.Concept;
import bio.knowledge.model.ConceptMapArchive;
import bio.knowledge.model.Library;
import bio.knowledge.model.user.Group;
import bio.knowledge.model.user.User;
import bio.knowledge.service.Cache;
import bio.knowledge.service.ConceptMapArchiveService;
import bio.knowledge.service.ConceptMapArchiveService.SearchMode;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.KBQuery.LibrarySearchMode;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.DesktopView;
import bio.knowledge.web.view.LoginView;
import bio.knowledge.web.view.Registry.Mapping;

public class SaveWindow extends Window {
	
	private static final long serialVersionUID = -7277760996463244429L;

	private KBQuery query;
	private Mapping mapping;
	private Navigator navigator;
	private ConceptMapArchiveService conceptMapArchiveService;
	private ConceptMapDisplay conceptMapDisplay;
	private Cache cache;

	private TextField nameField;
	private Button saveButton;
	private Button cancelButton;
	private SingleRadioButton isPublicOption;
	private TextArea commentsArea;
	private ComboBox groupChooser;

	private Book book;
	
	private final String defaultName;

	// public abstract void save();

	public SaveWindow(
			String defaultName,
			KBQuery query, 
			Mapping mapping,
			ConceptMapDisplay conceptMapDisplay, 
			Navigator navigator, 
			Cache cache) {
		
		this.defaultName = defaultName;
		this.cache = cache;
		this.conceptMapDisplay = conceptMapDisplay;
		this.conceptMapArchiveService = ((DesktopUI) UI.getCurrent()).getConceptMapArchiveService();
		this.query = query;
		this.mapping = mapping;
		this.navigator = navigator;

		setupLayout();
		setConceptMapName(defaultName);
	}

	private AlertWindow alertWindow;
	
	private ConceptMapArchive archive;

	private void saveToLibrary(String jsonContent, String pngContent) {
		String conceptMapName = nameField.getValue();
		String authorsComments = commentsArea.getValue();
		
		User user = ((DesktopUI)UI.getCurrent()).getAuthenticationManager().getCurrentUser();		

		String accountId;
		
		AuthenticationManager auth = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
		
		if (auth.isUserAuthenticated()) {
			accountId = auth.getCurrentUser().getUserId();
		} else {
			accountId = null;
		}
		
		archive = conceptMapArchiveService.getConceptMapArchiveByName(
				conceptMapName,
				user != null ? user.getId() : null,
				user != null ? user.getIdsOfGroupsBelongedTo() : new String[0]
		);

		if (archive == null) {
			archive = new ConceptMapArchive(conceptMapName);
			archive.setComments(authorsComments);
			save(jsonContent, pngContent, archive);
			((DesktopUI) UI.getCurrent()).updateCurrentConceptMapName(archive.getName());
			
		} else {
			archive.setComments(authorsComments);
			
			String authorId = archive.getAuthorsAccountId();

			if (authorId == null) {
				alertWindow = new AlertWindow(
						"An anonymous map already exists by that name. Anonymous maps cannot be overwritten.", e -> {
							alertWindow.close();
						}, true, true);

				UI.getCurrent().addWindow(alertWindow);
			} else if (authorId.equals(accountId)) {
				alertWindow = new AlertWindow("You already have a concept map with that name. Overwrite it?", e -> {
					save(jsonContent, pngContent, archive);
					((DesktopUI) UI.getCurrent()).updateCurrentConceptMapName(archive.getName());
					alertWindow.close();
				}, true);

				UI.getCurrent().addWindow(alertWindow);
			} else if (accountId == null) {
				alertWindow = new AlertWindow(
						"A map already exists with that name, and only the author can overwrite it. Log in?",
						makeGoToLoginClickListener(), true);

				UI.getCurrent().addWindow(alertWindow);
			} else {
				alertWindow = new AlertWindow(
						"This concept map does not belong to you, you cannot overwrite it.", event -> {
//							Do nothing.
						});

				UI.getCurrent().addWindow(alertWindow);
			}
		}
	}
	
	private String skeleton = 
			"<!--Knowledge.Bio 3.0 Concept Map Dump--> \n"
			+ "<version>"
			+  "3.0"
			+ "</version>\n"
			+ "<meta>\n"
			+ "<ConceptName>"
			+ 	"{0}"
			+ "</ConceptName>\n"
			+ "<RootConceptId>"
			+		"{1}"
			+ "</RootConceptId>\n"
			+ "</meta>\n"
			+ "<data>"
			+ "{2}"
			+ "</data>";

	private Button exportButton;

	private void save(String jsonContent, String pngContent, ConceptMapArchive archive) {

		generateArchive(jsonContent, pngContent, archive);
		 
		try {
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			User userProfile = ui.getAuthenticationManager().getCurrentUser();
			String userId = userProfile != null ? userProfile.getId() : null;
			if (conceptMapArchiveService.save(archive, userId, isPublicOption.isChecked())) {
				if (this.isPublicOption.isChecked()) {
					new Notification("Successfully saved to public library.").show(Page.getCurrent());
				} else {
					new Notification("Successfully saved to private library.").show(Page.getCurrent());
				}
			}
		} catch (Exception ex) {
			new Notification("Unable to save to library.", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
		} finally {
			close();
		}
	}
	
	private void generateArchive(String jsonContent, String pngContent, ConceptMapArchive archive) {
		Concept concept = query.getCurrentQueryConcept().get();
		jsonContent = MessageFormat.format(skeleton, concept.getName(), concept.getId(), jsonContent);
		
		// first clear node ids
		query.clearNodeIdsFromConceptMap();
		// get nodes
		Iterator<HashMap<String, HashMap<String, Serializable>>> itr = 
				conceptMapDisplay.getElements().getNodes().iterator();
		
		// Get all node's id from conceptmap and set to kbquery.
		while (itr.hasNext()) {
			query.addNodeIdToSet((String) itr.next().get("data").get("id"));
		}
		// reset cache to reflect any change in library value for
		// @todo Ideally, it should be invalidate that particular entry,
		// however we don't have enough information to build cachekey, So
		// resetting cache is also fine.
		cache.resetCache();

		archive.setConceptMapJson(jsonContent);
		archive.setConceptMapPng(pngContent);
		archive.setConceptMapSif(conceptMapDisplay.convertToSIF());
		archive.setConceptMapTsv(conceptMapDisplay.converterToTSV());

		archive.setId(concept.getId());
		archive.setVersion(1);
		archive.setVersionDate(new Date().getTime());
		
		Group group = (Group) groupChooser.getValue();
		archive.setGroupId(group != null ? group.getGroupId() : null);

		Optional<Library> parentMapOpt = query.getCurrentImportedMaps();
		if (parentMapOpt.isPresent()) {
			Library parentMaps = parentMapOpt.get();
			archive.setParents(parentMaps);
		}
	}

	private ValueChangeListener makeNameFieldValueChangeListener() {
		return event -> {
			if (!nameField.isValid()) {
				nameField.setValidationVisible(true);
			}
		};
	}

	private Validator makeNameFieldValidator() {
		return value -> {
			if (value.toString().isEmpty()) {
				throw new InvalidValueException("Your concept map must have a name");
			}
		};
	}

	private void setupLayout() {
		query.setLibraryMode(LibrarySearchMode.BY_LIBRARY);

		VerticalLayout mainLayout = new VerticalLayout();

		groupChooser = new ComboBox();
		nameField = new TextField();
		commentsArea = new TextArea("Comments");
		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");
		exportButton = new Button("Export");
		exportButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ConceptMapArchive newArchive = new ConceptMapArchive(nameField.getValue());
				
				conceptMapDisplay.requestContent(new ContentRequester() {
					@Override
					public void processRequestedContent(String jsonContent, String pngContent) {
						Concept concept = query.getCurrentQueryConcept().get();
						jsonContent = MessageFormat.format(skeleton, concept.getName(), concept.getId(), jsonContent);
						
						// first clear node ids
						query.clearNodeIdsFromConceptMap();
						// get nodes
						Iterator<HashMap<String, HashMap<String, Serializable>>> itr = 
								conceptMapDisplay.getElements().getNodes().iterator();
						
						// Get all node's id from conceptmap and set to kbquery.
						while (itr.hasNext()) {
							query.addNodeIdToSet((String) itr.next().get("data").get("id"));
						}
						// reset cache to reflect any change in library value for
						// @todo Ideally, it should be invalidate that particular entry,
						// however we don't have enough information to build cachekey, So
						// resetting cache is also fine.
						cache.resetCache();

						newArchive.setConceptMapJson(jsonContent);
						newArchive.setConceptMapPng(pngContent);
						newArchive.setConceptMapSif(conceptMapDisplay.convertToSIF());
						newArchive.setConceptMapTsv(conceptMapDisplay.converterToTSV());

						newArchive.setId(concept.getId());
						newArchive.setVersion(1);
						newArchive.setVersionDate(new Date().getTime());
						
						Group group = (Group) groupChooser.getValue();
						newArchive.setGroupId(group != null ? group.getId() : null);

						Optional<Library> parentMapOpt = query.getCurrentImportedMaps();
						if (parentMapOpt.isPresent()) {
							Library parentMaps = parentMapOpt.get();
							newArchive.setParents(parentMaps);
						}	
					}
				});
				ExportWindow exportWindow = new ExportWindow(query, newArchive);
				UI.getCurrent().addWindow(exportWindow);	
			}
		});
		
		isPublicOption = new SingleRadioButton("Publicly Available", true);
		
		refreshGroupChooser();
		for (Object id : groupChooser.getItemIds()) {
			groupChooser.select(id);
			break;
		}

		nameField.setInputPrompt("Concept map name");
		nameField.setId("filename");
		
		commentsArea.setSizeFull();
//		groupChooser.setCaption("Choose a user group to save this concept map under");

		String[] columnNames = { "name", "dateLastModified" };
		Map<String, RendererClickListener> clickListenerMapping = new HashMap<String, RendererClickListener>();

		clickListenerMapping.put(columnNames[0], event -> {
			String conceptMapName = event.getItemId().toString();
			setConceptMapName(conceptMapName);
		});
		
		AuthenticationManager auth = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
		User userProfile = auth.getCurrentUser();
		book = new Book(columnNames, clickListenerMapping, userProfile, mapping);

		HorizontalLayout saveBar = new HorizontalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);

		saveBar.addComponent(nameField);
//		saveBar.addComponent(saveButton);
		saveBar.addComponent(exportButton);
		saveBar.addComponent(cancelButton);
//		saveBar.addComponent(cancelButton);
//
//		saveBar.setExpandRatio(nameField, 3);
//		saveBar.setExpandRatio(saveButton, 1);
//		saveBar.setExpandRatio(cancelButton, 1);

		saveBar.setSizeFull();
		nameField.setSizeFull();
		saveButton.setSizeFull();
		cancelButton.setSizeFull();

		saveBar.setSpacing(true);
		
		if (auth.isUserAuthenticated()) {
			HorizontalLayout hlayout = new HorizontalLayout();
			mainLayout.addComponent(hlayout);
			hlayout.setSpacing(true);
//			hlayout.setMargin(true);
			hlayout.addComponent(groupChooser);
			hlayout.addComponent(isPublicOption);
		}
		mainLayout.addComponent(saveBar);
		
		mainLayout.addComponent(commentsArea);

		if (auth.isUserAuthenticated()) {
			TabSheet tabsheet = this.makeTabSheetController(book.getListContainer());
			mainLayout.addComponent(tabsheet);
			mainLayout.addComponent(book);
			
			refreshGroupChooser();
			
			groupChooser.setNewItemsAllowed(true);
			groupChooser.setImmediate(true);
			groupChooser.setNewItemHandler(newItemCaption -> {
				User user = auth.getCurrentUser();
				try {
					Group group = auth.createGroup(user, newItemCaption);
					refreshGroupChooser(group);
				} catch (AuthenticationException e) {
					Notification.show(e.getMessage(), Type.WARNING_MESSAGE);
				}
			});
			
		} else {
			groupChooser.setVisible(false);
			NativeButton goToLogin = new NativeButton("Login to view private maps");

			goToLogin.addClickListener(makeGoToLoginClickListener());

			mainLayout.addComponent(goToLogin);
			mainLayout.addComponent(book);
		}

		this.setContent(mainLayout);

		this.addStyleName("concept-search-window");
		this.center();
		// this.setModal(true);
		this.setResizable(true);
		this.setCaption("Save to Library");
		this.setModal(true);
		this.setWidth(40.0f, Unit.EM);

		nameField.setValidationVisible(false);
		nameField.addValidator(makeNameFieldValidator());
		nameField.addValueChangeListener(makeNameFieldValueChangeListener());

		saveButton.addClickListener(event -> {
			if (nameField.isValid()) {
				conceptMapDisplay.requestContent(new ContentRequester() {

					@Override
					public void processRequestedContent(String jsonContent, String pngContent) {
						saveToLibrary(jsonContent, pngContent);
					}

				});
			} else {
				nameField.setValidationVisible(true);
			}
		});

		cancelButton.addClickListener(event -> close());
	}

	/**
	 * Refreshes the content of the ComboBox representing the group choosing
	 * widget in the SaveWindow.
	 */
	private void refreshGroupChooser() {
		DesktopUI ui = (DesktopUI) UI.getCurrent();
		User currentUser = ui.getAuthenticationManager().getCurrentUser();
		
		if (currentUser != null) {
			if (groupChooser.removeAllItems()) {
				for (Group userGroup : currentUser.getGroupsOwned()) {
					groupChooser.addItem(userGroup);
				}
			}
		}
		
		if (groupChooser.isEmpty()) {
			groupChooser.setInputPrompt("Create a group");
		} else {
			groupChooser.setInputPrompt("Select a group");
		}
	}
	
	/**
	 * Refreshes the content of the ComboBox representing the group choosing
	 * widget in the SaveWindow.
	 * 
	 * @param group
	 *            The group that should be displayed after the content of the
	 *            ComboBox is refreshed.
	 */
	private void refreshGroupChooser(Group group) {
		refreshGroupChooser();
		groupChooser.select(group);
		this.nameField.focus();
	}

	private ClickListener makeGoToLoginClickListener() {
		SaveWindow thisWindow = this;

		// UI.getCurrent().removeWindow(this);

		return event -> {
			UI.getCurrent().removeWindow(thisWindow);

			if (alertWindow != null) {
				alertWindow.close();
			}
			
			Callable<Void> callAfterAuthentication = new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					thisWindow.navigator.navigateTo(DesktopView.NAME);
					UI.getCurrent().addWindow(thisWindow);

					// Re-create the layout, since it might now be different
					// if we have authenticated.
					thisWindow.setupLayout();
					book.redraw();
					setConceptMapName(defaultName);
					return null;
				}

			};
			
			LoginView loginView = ((DesktopUI) UI.getCurrent()).getLoginView();
			loginView.navigateToAndExecute(callAfterAuthentication);
		};
	}
	
	private TabSheet makeTabSheetController(ListContainer listContainer) {
		// This is a hack to bypass tabs requiring and being identified by their
		// content.
		// We're creating invisible labels to use as the content of each tab. We
		// only want
		// the functionality of this tabsheet, we don't want to visibly put
		// anything in it!
		TabSheet tabsheet = new TabSheet();
		Label publicHiddenLabel = new Label("");
		Label personalHiddenLabel = new Label("");
		tabsheet.addTab(personalHiddenLabel, "Personal Maps");
		tabsheet.addTab(publicHiddenLabel, "All Maps");

		tabsheet.addSelectedTabChangeListener(event -> {
			Label selectedLabel = (Label) tabsheet.getSelectedTab();

			if (selectedLabel == publicHiddenLabel) {
				listContainer.setFirstPage();
				listContainer.refresh(SearchMode.ALL_MAPS);
				book.redraw();
			} else {
				listContainer.setFirstPage();
				listContainer.refresh(SearchMode.AUTHORED_MAPS);
				book.redraw();
			}
		});

		AuthenticationManager auth = ((DesktopUI) UI.getCurrent()).getAuthenticationManager();
		if (auth.isUserAuthenticated()) {
			tabsheet.setVisible(true);
		} else {
			tabsheet.setVisible(false);
		}

		listContainer.setFirstPage();
		listContainer.refresh(SearchMode.AUTHORED_MAPS);
		
		return tabsheet;
	}
	
	/**
	 * Sets the concept map's name in the name field, and if that concept map
	 * already exists updates the UI with its properties
	 * 
	 * @param conceptMapName
	 */
	public void setConceptMapName(String conceptMapName) {
		User user = ((DesktopUI)UI.getCurrent()).getAuthenticationManager().getCurrentUser();

		archive = conceptMapArchiveService.getConceptMapArchiveByName(
				conceptMapName,
				user != null ? user.getUserId() : null,
				user != null ? user.getIdsOfGroupsBelongedTo() : new String[0]
		);
		
		if (archive != null) {
			commentsArea.setValue(archive.getComments());
			isPublicOption.setChecked(archive.isPublic());
			
			for (Object group : groupChooser.getItemIds()) {
				group = (Group) group;
			}
			
			groupChooser.setValue(null);
			for (Object object : groupChooser.getItemIds()) {
				Group group = (Group) object;
				if (group.getGroupId().equals(archive.getGroupId())) {
					groupChooser.setValue(group);
					break;
				}
			}
		}
		conceptMapName = conceptMapName != null ? conceptMapName : "";
		nameField.setValue(conceptMapName);
	}

}
