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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.model.user.Permission;
import bio.knowledge.model.user.User;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.LibrarySearchResults;

public class UserDetails extends VerticalLayout {

	private static final long serialVersionUID = -2963574283905392754L;
		
	private LibrarySearchResults librarySearchResults;
	
	private Button goBackBtn = new Button("Go back");

	public UserDetails(User user, ClickListener goBack) {
		goBackBtn.addClickListener(goBack);
		setupLayout(user);
	}
	
	public UserDetails(String userId, ClickListener goBack) {
		this(
				((DesktopUI)UI.getCurrent()).getAuthenticationManager().getUser(userId),
				goBack
		);
	}
	
	public void hideBackButton() {
		this.goBackBtn.setVisible(false);
	}
	
	public void setButtonText(String caption) {
		this.goBackBtn.setCaption(caption);
	}

	private void setupLayout(User user) {
		goBackBtn.setSizeFull();
		
		Label title = this.formatDataTableLabel("Viewing ", user.getUsername(), " details");
		title.addStyleName("predication-label");

		int numberOfProperties = 5;
		GridLayout infoGrid = new GridLayout(2, numberOfProperties);
		infoGrid.setSpacing(true);

		infoGrid.addComponent(new Label("Date joined:"));
		infoGrid.addComponent(new Label(user.getDateJoined()));
		
		boolean showName  = user.getPermission(Permission.NAME_PUBLICIZED);
		boolean showEmail = user.getPermission(Permission.EMAIL_PUBLICIZED);
		
		if (showName) {
			infoGrid.addComponent(new Label("Name:"));
			infoGrid.addComponent(new Label(user.getFullName()));
		}
		
		if (showEmail) {
			infoGrid.addComponent(new Label("Email:"));
			infoGrid.addComponent(new Label(user.getEmail()));
		}
		
		String facebookUrl = user.getFacebookUrl();
		String linkedinUrl = user.getLinkedInUrl();
		String twitterUrl  = user.getTwitterUrl();
		
		if (facebookUrl != "") {
			infoGrid.addComponent(new Label("Facebook URL"));
			infoGrid.addComponent(new Label(facebookUrl));
		}
		
		if (linkedinUrl != "") {
			infoGrid.addComponent(new Label("LinkedIn URL"));
			infoGrid.addComponent(new Label(linkedinUrl));
		}
		
		if (twitterUrl != "") {
			infoGrid.addComponent(new Label("Twitter URL"));
			infoGrid.addComponent(new Label(twitterUrl));
		}

		addComponent(title);
		addComponent(infoGrid);
		if (!showEmail && !showName && twitterUrl == null && facebookUrl == null && linkedinUrl == null) {
			addComponent(new Label("This user has decided to not publicize their details"));
		}
		
		addComponent(goBackBtn);
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
}
