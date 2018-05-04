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

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

import bio.knowledge.web.design.LandingPageDesign;
import bio.knowledge.web.ui.DesktopUI;

/**
 * The actual view is created using Vaadin Designer.
 * To make changes to the view, open the html file with Vaadin Designer
 * This file may be used to program the functionality of the application
 * @author Colin Qiao
 *
 */
@SpringView(name = LandingPageView.NAME)
public class LandingPageView extends LandingPageDesign implements View {
	
	private static final long serialVersionUID = 5370382426716678474L;

	public static final String NAME = "";
	
	public LandingPageView() {
		initialize();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	private void initialize() {
		initImages();
		initButtons();
	}

	private void initButtons() {
		launchBtn.setDisableOnClick(true);
		launchBtn.addClickListener(e -> {
			Navigator navigator = ((DesktopUI)UI.getCurrent()).getApplicationNavigator();
			navigator.navigateTo(DesktopView.NAME);
			launchBtn.setEnabled(true);
		});
	}
	
	// add the alt texts for images
	private void initImages() {
		logoImage.setAlternateText("Knowledge.Bio logo");
		overviewImage.setAlternateText("Steps overview");
		usecaseImg.setAlternateText("Use Case");
	}
}