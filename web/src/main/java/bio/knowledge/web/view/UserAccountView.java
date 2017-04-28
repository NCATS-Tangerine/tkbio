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
package bio.knowledge.web.view;

import java.util.Collection;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.resource.ResourceException;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import bio.knowledge.authentication.AuthenticationListener;
import bio.knowledge.authentication.Role;
import bio.knowledge.authentication.UserGroup;
import bio.knowledge.authentication.UserProfile;
import bio.knowledge.authentication.exceptions.AuthenticationException;
import bio.knowledge.validation.ValidationHandler;
import bio.knowledge.web.design.AboutUserDesign;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.components.UserDetails;

@SpringView(name = UserAccountView.NAME)
public class UserAccountView extends AboutUserDesign implements View, AuthenticationListener, RestrictedAccessView {

	private static final long serialVersionUID = -5518331903310398566L;

	public static final String NAME = "useraccount";

	private final Navigator navigator;

	private ValidationHandler validationHandler;
	
	private Object showName;
	private Object showEmail;
	
	public UserAccountView(Navigator navigator) {
		
		this.navigator = navigator;		
		
		showName = optionGroup.addItem();
		showEmail = optionGroup.addItem();
		optionGroup.setMultiSelect(true);
		
		validationHandler = new ValidationHandler(errorLabel, updateButton);
		
		validationHandler.addNonEmptyValidator(firstnameField, "First name");
		validationHandler.addNonEmptyValidator(lastnameField, "Last name");
		validationHandler.addRequiredMatchValidator(
				emailField,
				"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
				"Email is invalid"
		);
		validationHandler.addNonEmptyValidator(usernameField, "Username");
		validationHandler.addRequiredMatchValidator(
				facebookField,
				"((https?:\\/\\/)?(www\\.)?facebook\\.com\\/.+)?",
				"The Facebook URL provided is invalid"
		);
		validationHandler.addRequiredMatchValidator(
				linkedInField,
				"((https?:\\/\\/)?(www\\.)?([a-z]{2,3}\\.)?linkedin.com\\/.+)?",
				"The LinkedIn URL provided is invalid"
		);
		validationHandler.addRequiredMatchValidator(
				twitterField,
				"((https?:\\/\\/)?(www\\.)?twitter.com\\/.+)?",
				"The Twitter URL provided is invalid"
		);
	}
	
	TextField newGroupField = new TextField();
	TextField newUserField = new TextField();
	Grid userGrid = new Grid();
	IndexedContainer container = new IndexedContainer();
	Button addGroupBtn = new Button("Create Group");
	Button addUserBtn = new Button("Add User");
	ComboBox chooseGroupComboBox = new ComboBox();
	Label newGroupLabel = new Label(
			"<span style=\"font-weight:bold;font-size:15px;\">Create a new group</span>",
			ContentMode.HTML
	);
	Label newUserLabel = new Label(
			"<span style=\"font-weight:bold;font-size:15px;\">Manage group members</span>",
			ContentMode.HTML
	);
	
	private void setupGroupComponentFunctionality() {
		chooseGroupComboBox.setValue(chooseGroupComboBox.getNullSelectionItemId());
		newUserField.setEnabled(false);
		newUserField.setValue("");
		addUserBtn.setEnabled(false);
		
		addGroupBtn.addClickListener(event -> {
			DesktopUI ui = ((DesktopUI)UI.getCurrent());
			UserProfile user = ui.getAuthenticationManager().getCurrentUser();
			try {
				ui.getAuthenticationManager().createGroup(user, newGroupField.getValue());
				Notification.show("Group created");
				newGroupField.clear();
			} catch (AuthenticationException e) {
				Notification.show(e.getMessage());
			}
			
			chooseGroupComboBox.removeAllItems();
			for (UserGroup group : user.getGroupsOwned()) {
				chooseGroupComboBox.addItem(group);
			}
		});
		
		
		
		addUserBtn.addClickListener(event -> {
			String name = newUserField.getValue();
			name = name.trim();
			UserGroup group = (UserGroup) chooseGroupComboBox.getValue();
			try {
				group.addMember(name);
				newUserField.setValue("");
			} catch (AuthenticationException e) {
				Notification.show(e.getMessage());
			}
			
			refreshContainer(container, group);
			
		});
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(container);
		gpcontainer.addGeneratedProperty("username", new PropertyValueGenerator<String> () {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return ((UserProfile) itemId).getUsername();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});
		
		gpcontainer.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = -8939778710313465221L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return FontAwesome.REMOVE.getHtml();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
			
		});

		// We use my custom ButtonRenderer because it automatically displays
		// text as HTML, which is what we need here
		bio.knowledge.renderer.ButtonRenderer deleteRenderer = new bio.knowledge.renderer.ButtonRenderer();
		deleteRenderer.addClickListener(event -> {
			UserGroup group = (UserGroup) chooseGroupComboBox.getValue();
			UserProfile user = (UserProfile) event.getItemId();
			group.removeMember(user);
			
			refreshContainer(container, group);
		});
		ButtonRenderer usernameRenderer = new ButtonRenderer();
		usernameRenderer.addClickListener(event -> {
			Window window = new Window();
			UI.getCurrent().addWindow(window);
			
			UserProfile user = (UserProfile) event.getItemId();
			UserDetails userDetails = new UserDetails(user, clickEvent -> {
				window.close();
			});
			window.setCaption("User Details");
			userDetails.setMargin(true);
			userDetails.setSpacing(true);
			userDetails.setButtonText("close");
			window.addStyleName("concept-search-window");
			window.center();
			window.setResizable(true);
			window.setContent(userDetails);
		});
		userGrid.removeAllColumns();
		userGrid.addColumn("username");
		userGrid.addColumn("delete");
		userGrid.getColumn("delete").setRenderer(deleteRenderer);
		userGrid.getColumn("username").setRenderer(usernameRenderer);
//		userGrid.setHeaderVisible(false);
		
		userGrid.setContainerDataSource(gpcontainer);
		
		chooseGroupComboBox.setVisible(true);
		
		chooseGroupComboBox.addValueChangeListener(event -> {
			UserGroup group = (UserGroup) event.getProperty().getValue();
			
			refreshContainer(container, group);
			if (group != chooseGroupComboBox.getNullSelectionItemId()) {
				newUserField.setEnabled(true);
				addUserBtn.setEnabled(true);
			} else {
				newUserField.setValue("");
				newUserField.setEnabled(false);
				addUserBtn.setEnabled(false);
			}
			
		});
	}
	
	private VerticalLayout setupGroupComponentLayout() {
		VerticalLayout mainLayout = this.groupLayout;
		this.groupLayout.removeAllComponents();
		
		newGroupField.setWidth("150");
		newUserField.setWidth("150");
		addGroupBtn.setWidth("150");
		addUserBtn.setWidth("150");
		chooseGroupComboBox.setWidth("150");
		
		newUserField.setInputPrompt("Username or email");
		newGroupField.setInputPrompt("New group name");
		chooseGroupComboBox.setInputPrompt("Choose a group");
		
		HorizontalLayout hLayout = new HorizontalLayout();
		
		VerticalLayout subLayout1 = new VerticalLayout();
		VerticalLayout subLayout2 = new VerticalLayout();
		
		
		HorizontalLayout h1 = new HorizontalLayout();
		VerticalLayout v1 = new VerticalLayout();
		
		userGrid.setHeight("290px");
		userGrid.setWidth("300px");
		v1.setSizeFull();
		h1.setSizeFull();
		
		h1.addComponent(v1);
		h1.addComponent(userGrid);
		v1.addComponent(chooseGroupComboBox);
		v1.addComponent(newUserField);
		v1.addComponent(addUserBtn);
		
		subLayout1.addComponent(newGroupLabel);
		subLayout1.addComponent(newGroupField);
		subLayout1.addComponent(addGroupBtn);
		
		subLayout2.addComponent(newUserLabel);
		subLayout2.addComponent(h1);
		
		hLayout.addComponent(subLayout1);
		// Putting in these empty labels are the only way I know to spread the
		// components out horizontally
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(
				"<hr style=\"width: 1px; height: 300px; display: inline-block; color: white;\">", 
				ContentMode.HTML
		));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(new Label(""));
		hLayout.addComponent(subLayout2);
		mainLayout.addComponent(hLayout);
		
		mainLayout.setMargin(true);
		mainLayout.setComponentAlignment(hLayout, Alignment.TOP_CENTER);
		
		mainLayout.setSpacing(true);
		hLayout.setSpacing(true);
		subLayout1.setSpacing(true);
		subLayout2.setSpacing(true);
		h1.setSpacing(true);
		v1.setSpacing(true);
		
		hLayout.setComponentAlignment(subLayout1, Alignment.TOP_LEFT);
//		hLayout.setWidth("50%");
		
		return mainLayout;
	}
	
	private void refreshGroupChooser() {
		UserProfile profile = ((DesktopUI) UI.getCurrent()).getAuthenticationManager().getCurrentUser();
		chooseGroupComboBox.setInputPrompt("Select a group");

		if (chooseGroupComboBox.removeAllItems()) {
			for (UserGroup group : profile.getGroupsOwned()) {
				chooseGroupComboBox.addItem(group);
			}
		}
	}
	
	private void refreshContainer(IndexedContainer container, UserGroup group) {
		UserProfile currentUser = ((DesktopUI) UI.getCurrent()).getAuthenticationManager().getCurrentUser();
		container.removeAllItems();
		if (group != null) {
			for (UserProfile profile : group.getMembers()) {
				if (!profile.equals(currentUser)) {
					container.addItem(profile);
				}
			}
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		setup();
		
		//DesktopUI ui = (DesktopUI) UI.getCurrent();
		//UserProfile profile = ui.getAuthenticationManager().getCurrentUser();
		
		refreshGroupChooser();
		
		setupGroupComponentLayout();
		setupGroupComponentFunctionality();
	}

	private void setup() {
		validationHandler.resetState();
		optionGroup.setCaption("Allow other users to view:");
		optionGroup.setItemCaption(showName, "Your full name");
		optionGroup.setItemCaption(showEmail, "Your email");
		
		DesktopUI ui = (DesktopUI)UI.getCurrent();
		UserProfile userProfile = ui.getAuthenticationManager().getCurrentUser();
		if ( userProfile.getPermission(UserProfile.EMAIL_PUBLICIZED_PERMISSION)) {
			optionGroup.select(showEmail);
		}
		
		if (userProfile.getPermission(UserProfile.NAME_PUBLICIZED_PERMISSION)) {
			optionGroup.select(showName);
		}
		
		String email      = userProfile.getEmail();
		String username   = userProfile.getUsername();
		String facebook   = userProfile.getFacebookUrl();
		String twitter    = userProfile.getTwitterUrl();
		String linkedin   = userProfile.getLinkedInUrl();
		String firstname  = userProfile.getFirstName();
		String middlename = userProfile.getMiddleName();
		String lastname   = userProfile.getLastName();
		
		this.emailField.setValue(email);
		this.lastnameField.setValue(lastname);
		this.usernameField.setValue(username);
		this.firstnameField.setValue(firstname);
		this.middlenameField.setValue(notNull(middlename));
		this.facebookField.setValue(notNull(facebook));
		this.linkedInField.setValue(notNull(linkedin));
		this.twitterField.setValue(notNull(twitter));
		
		this.updateButton.addClickListener(event -> {
			if (validationHandler.validateAll()) {
				String urlPrefixRegex = "https?:\\/\\/|www\\.";
				String facebookUrl = facebookField.getValue().replaceAll(urlPrefixRegex, "");
				String linkedinUrl = linkedInField.getValue().replaceAll(urlPrefixRegex, "");
				String twitterUrl = twitterField.getValue().replaceAll(urlPrefixRegex, "");
				String firstname1 = firstnameField.getValue();
				String middlename1 = middlenameField.getValue();
				String lastname1 = lastnameField.getValue();
				String username1 = usernameField.getValue();
				String email1 = emailField.getValue();
				
				facebookField.setValue(facebookUrl);
				linkedInField.setValue(linkedinUrl);
				twitterField.setValue(twitterUrl);
				userProfile.setFacebookUrl(facebookUrl);
				userProfile.setFirstName(firstname1);
				userProfile.setTwitterUrl(twitterUrl);
				userProfile.setLastName(lastname1);
				userProfile.setLinkedInUrl(linkedinUrl);
				userProfile.setMiddleName(middlename1);
				
				userProfile.setPermission(
						UserProfile.EMAIL_PUBLICIZED_PERMISSION,
						((Collection) optionGroup.getValue()).contains(showEmail)
				);
				
				userProfile.setPermission(
						UserProfile.NAME_PUBLICIZED_PERMISSION,
						((Collection) optionGroup.getValue()).contains(showName)
				);
				
				try {
					userProfile.setEmail(email1);
				} catch (ResourceException e1) {
					if (e1.getCode() == 409) {
						validationHandler.addProhibitedMatchValidator(
								emailField,
								email1,
								"That email is already in use."
						);
						validationHandler.validateImmediately(emailField);
						
					} else {
						Notification.show("Unknown error occurred with setting email to " + email1, Type.ERROR_MESSAGE);
					}
				}
				
				try {
					userProfile.setUsername(username1);
				} catch (ResourceException e2) {
					if (e2.getCode() == 409) {
						validationHandler.addProhibitedMatchValidator(
								usernameField,
								username1,
								"That username is already in use."
						);
						validationHandler.validateImmediately(usernameField);
					} else {
						Notification.show("Unknown error occurred with setting username to " + username1, Type.ERROR_MESSAGE);
					}
				}
			}
			
			if (validationHandler.validateAll()) {
				ui.getApplicationNavigator().navigateTo(LandingPageView.NAME);
			}
		});	
	}
	
	private String notNull(String string) {
		return string != null ? string : "";
	}

	@Override
	public void onLogin(Account account) { 
		
	};

	@Override
	public void onLogout() {
		if (navigator.getCurrentView() == this) {
			navigator.navigateTo(LandingPageView.NAME);
		}
	}

	@Override
	public Role[] permittedRoles() {
		return Role.getAll();
	}
}
