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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AlertWindow extends Window {

	private static final long serialVersionUID = -7693383757134777855L;
	
	private Label messageLabel;
	private Button okayButton;
	private Button cancelButton;
	
	public AlertWindow(String message, ClickListener okayClickListener, boolean setModal, boolean hideCancelButton) {
		messageLabel = new Label(message);
		okayButton = new Button("Okay", okayClickListener);
		cancelButton = new Button("Cancel", (ClickListener) event -> close());
		
		cancelButton.setVisible(!hideCancelButton);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(messageLabel);
		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.addComponent(okayButton);
		buttonBar.addComponent(cancelButton);
		mainLayout.addComponent(buttonBar);
		mainLayout.setComponentAlignment(buttonBar, Alignment.BOTTOM_CENTER);
		mainLayout.setSpacing(true);
		messageLabel.setStyleName("alert-window-label");
		buttonBar.setSpacing(true);
		this.setContent(mainLayout);
		this.addStyleName("concept-search-window");
		this.center();
		this.setResizable(false);
		this.setCaption("Alert");
		this.setModal(setModal);
	}
	
	public AlertWindow(String message, ClickListener okayClickListener) {
		this(message, okayClickListener, false, false);
	}
	
	public AlertWindow(String message, ClickListener okayClickListener, boolean setModal) {
		this(message, okayClickListener, setModal, false);
	}
}
