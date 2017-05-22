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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.model.Annotation;
import bio.knowledge.model.RdfUtil;
import bio.knowledge.service.AnnotationService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.design.ReferenceDesign;
import bio.knowledge.web.ui.DesktopUI;

/**
 * ReferenceView displays the specific content of a 
 * Reference URI treated as a REST call or plain URL
 */
@SpringView(name = ReferenceView.NAME)
public class ReferenceView extends ReferenceDesign implements View {

	public static final String NAME = "references";

	private static final long serialVersionUID = -827840039603594744L;
	
	private Map<String, String> urlMapping = new HashMap<String, String>();
	
	@PostConstruct
	public void init() {
		urlMapping.put("RXNAV",		"https://rxnav.nlm.nih.gov/REST/Ndfrt/allInfo?nui=");
		urlMapping.put("CHEMBL",	"https://www.ebi.ac.uk/chembl/compound/inspect/CHEMBL");
		urlMapping.put("WD",		"http://www.wikidata.org/entity/");
		urlMapping.put("OBO",		"http://purl.obolibrary.org/obo/");
		urlMapping.put("PMID",		"https://www.ncbi.nlm.nih.gov/pubmed/");
	}
	
	private String getUrl(String id) {
		int i = id.indexOf(":");
		String objId = id.substring(i+1);
		id = id.substring(0, i);
		i = id.lastIndexOf(".");
		id = id.substring(i+1);
		id = id.toUpperCase();
		
		if (urlMapping.keySet().contains(id)) {
			return urlMapping.get(id) + objId;
		} else {
			return null;
		}
	}
	
	@Autowired
	private KBQuery query ;

	@Autowired
	private AnnotationService annotationService ;
	
	public ReferenceView() { }
	
	private TextField refIdSearchField = new TextField();
	private String    baseUri = "";
	
	private Boolean IS_PUBMED_ARTICLE = false ;
	
	@Override
	public void enter(ViewChangeEvent event) {

		removeAllComponents();
		
		Optional<Annotation> annotationOpt = query.getCurrentAnnotation();
		
		String[] uri = new String[1];
		
		if (annotationOpt.isPresent()) {
			String id = annotationOpt.get().getId();
			try {
				// TODO: At the moment, evidence id's are URL's
				URL url = new URL(id);
				uri[0] = url.toString();
			} catch (MalformedURLException e) {
				uri[0] = getUrl(id);
			}
		} else {
			return;
		}
		
		VerticalLayout localAbstractLayout = new VerticalLayout() ;	
		localAbstractLayout.setMargin(true);
		localAbstractLayout.setSpacing(true);
		
		HorizontalLayout abstractMenu = new HorizontalLayout();
		abstractMenu.setSpacing(true);
		
		refIdSearchField.setInputPrompt("Search Reference ID");
		
		Button referenceSearchBtn = new Button("GO");
		referenceSearchBtn.setClickShortcut(KeyCode.ENTER);
		
		Button showInNewWindowBtn        = new Button("Show Abstract in New Window");
		Button showReferenceRelationsBtn = new Button("Show Associated Concept Relations");

		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.addComponents( refIdSearchField, referenceSearchBtn );
		
		abstractMenu.addComponents(searchLayout, showInNewWindowBtn);
		
		// TODO: For now, don't give the user a 'pubmed relations' button unless they have such a beast!
		if(IS_PUBMED_ARTICLE) abstractMenu.addComponent(showReferenceRelationsBtn);
		
		VerticalLayout articleLayout = new VerticalLayout();
		articleLayout.setHeight("100%");
		
		setHeightUndefined();

		openReferenceLink(articleLayout, uri[0]);
		
		localAbstractLayout.addComponents(abstractMenu, articleLayout);
		localAbstractLayout.setHeight("100%");
		
		addComponent(localAbstractLayout);
		
		referenceSearchBtn.addClickListener(e-> {
			String accId    = refIdSearchField.getValue().trim();
			baseUri         = RdfUtil.resolveBaseUri(accId);
			String objectId = RdfUtil.getQualifiedObjectId(accId);
			openReferenceLink(articleLayout, baseUri+objectId);
		});
		
		showInNewWindowBtn.addClickListener(e -> {
			getUI().getPage().open( uri[0], "_blank", false );
		});

		DesktopUI ui = (DesktopUI) UI.getCurrent() ;
		
		// TODO: This button event should only be visible if Pubmed entry is displayed?
		showReferenceRelationsBtn.addClickListener(e -> {
			
			String accId = refIdSearchField.getValue().trim();
			String pmid  = RdfUtil.getQualifiedObjectId(accId);

			query.setCurrentPmid(pmid);
			
			ui.gotoStatementsTable() ;
			
		});
	}
	
	public void openReferenceLink(VerticalLayout pageLayout, String uri){
		
		pageLayout.removeAllComponents();

		if (uri != null && !uri.isEmpty()) {
			String URL =  "<iframe src=\"" + uri + "\" width=\"100%\" height=\"100%\"></iframe>";
			
			Label page = new Label(URL, ContentMode.HTML);
//			page.setHeight("100%");
			page.setHeight(500, Unit.PIXELS);
			
			pageLayout.addComponent(page);
			
		} else {
			
			refIdSearchField.setValue("");
			
			Label errorLabel = new Label("<span>" + "<b>" + "Cannot resolve the Reference '" + uri + "'</b>" + "</span>");
			errorLabel.setContentMode(ContentMode.HTML);
			errorLabel.addStyleName("pubmed-article-label");
			
			pageLayout.setMargin(true);
			pageLayout.addComponent(errorLabel);
		}
	}
}
