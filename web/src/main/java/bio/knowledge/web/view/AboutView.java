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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * The actual view is created using Vaadin Designer.
 * To make changes to the view, open the html file with Vaadin Designer
 * This file may be used to program the functionality of the application
 * @author farzi_000
 *
 */
@SpringView(name = AboutView.NAME)
public class AboutView extends VerticalLayout implements View {

	private static final long serialVersionUID = 2649916889832808064L;

	public static final String NAME = "about";
	
	public AboutView() {
		initialize();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		initialize();
	}

	private void initialize() {
		removeAllComponents();
		
		setSpacing(true);
		
		setUpTitleSection();
		setUpDescriptionSection();
		setUpUseCaseSection();
	}

	private void setUpUseCaseSection() {
		Label sectionHeaderLabel = new Label("USE CASE");
		sectionHeaderLabel.addStyleName("section-header-label");
		
		String introSentence = "Here is an example of what you can discover in here.";
		
		String question = "Given a patient with a mutation in the " +
							"Sepiaterin Reductase (SPR) gene and " +
							"a collection of Movement Disorders and " +
							"significantly disrupted sleep, " +
							"what treatment might you offer them?";
		
		String answer = "The answer is contained in the knowledge.bio graph " +
						"depicted here (and in Figure 1 of a ground breaking paper " +
						"in Science Translational Medicine )";
		
		Label introSentenceLabel = new Label(introSentence);
		Label questionLabel = new Label(question);
		Label answerLabel = new Label(answer);
		
		String contentStyleName = "section-content-label-left text-light";
		
		introSentenceLabel.addStyleName(contentStyleName);
		questionLabel.addStyleName(contentStyleName);
		answerLabel.addStyleName(contentStyleName);
		
		VerticalLayout paragraphLayout = new VerticalLayout();
		paragraphLayout.addComponents(sectionHeaderLabel, introSentenceLabel, 
									questionLabel, answerLabel);
		
		paragraphLayout.setSpacing(true);
		
		ThemeResource resource = new ThemeResource("images/usecase.png");
		Image useCaseImg = new Image("", resource);
		useCaseImg.addStyleName("usecase-img");
		
		HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.addComponents(paragraphLayout, useCaseImg);
		contentLayout.setWidth("100%");
		contentLayout.setSpacing(true);
		contentLayout.addStyleName("usecase-layout");
		
		contentLayout.setExpandRatio(paragraphLayout, 4);
		contentLayout.setExpandRatio(useCaseImg, 6);
		
		this.addComponent(contentLayout);
	}
	
	private void setUpDescriptionSection() {
		String introSentence = "Knowledge.bio is an open-source project that allows " +
				"its users to find and create biomedical connections from " +
				"over 20 million PubMed abstracts.";
		
		String followUpSentence = "Knowledge.bio integrates the explicit "
				+ "connections identified by the SemRep system from the "
				+ "US National Library of Medicine along with the "
				+ "Implicitome from the Leiden University Medical Center "
				+ "to provide predicted relationships between "
				+ "genes and diseases based on 'concept profile' technology.";
		
		
		Label introLabel = new Label(introSentence);	
		Label followUpLabel = new Label(followUpSentence);
		
		introLabel.addStyleName("section-content-label-left text-black kb-intro-label");
		followUpLabel.addStyleName("section-content-label-left text-black");
		
		ThemeResource resource = new ThemeResource("images/chart_images_06.png");
		Image pubmedConnectionImg = new Image("", resource);
		pubmedConnectionImg.addStyleName("pubmed-img");
		
		Label whatIsItLabel = new Label("WHAT IS IT?");
		whatIsItLabel.addStyleName("section-header-label");

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponents(introLabel, followUpLabel);
		
		HorizontalLayout labelContent = new HorizontalLayout();
		
		labelContent.addStyleName("kb-description-layout");
		labelContent.setWidth("100%");
		
		labelContent.addComponents(whatIsItLabel, contentLayout);
		
		labelContent.setExpandRatio(contentLayout, 7);
		labelContent.setExpandRatio(whatIsItLabel, 3);

		VerticalLayout whatIsItContent = new VerticalLayout();
		whatIsItContent.setSpacing(true);
		whatIsItContent.addComponents(pubmedConnectionImg, labelContent);
		
		this.addComponent(whatIsItContent);
	}

	private void setUpTitleSection() {
		Label titleLabel = new Label("<h1>" + "<span style=\"color:#333\">" + 
							"KNOWLEDGE." + "</span>" + "<span style=\"color:#5cb85c\">" +
							"BIO" + "</h1>", ContentMode.HTML);		

		Label sloganLabel = new Label("Discover the Connections");
		
		titleLabel.addStyleName("about-title-label");
		sloganLabel.addStyleName("about-slogan-label");

		VerticalLayout titleContent = new VerticalLayout();
		titleContent.addComponents(titleLabel, sloganLabel);
		
		VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.addComponent(titleContent);
		titleLayout.setComponentAlignment(titleContent, Alignment.MIDDLE_CENTER);
		
		this.addComponent(titleLayout);
	}
}