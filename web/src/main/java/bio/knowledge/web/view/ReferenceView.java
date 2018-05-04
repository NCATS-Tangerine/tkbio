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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//import bio.knowledge.service.AnnotationService;
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
	
	private Logger _logger = LoggerFactory.getLogger(ReferenceView.class);
	
	// TODO: Might be helpful to replace this with a call to PrefixCommons?
	private Map<String, String> urlMapping = new HashMap<String, String>();
	
	@PostConstruct
	public void init() {
		urlMapping.put("RXNAV",		"https://rxnav.nlm.nih.gov/REST/Ndfrt/allInfo?nui=");
		urlMapping.put("CHEMBL",	"https://www.ebi.ac.uk/chembl/compound/inspect/CHEMBL");
		urlMapping.put("WD",		"http://www.wikidata.org/entity/");
		urlMapping.put("OBO",		"http://purl.obolibrary.org/obo/");
		urlMapping.put("PMID",		"https://www.ncbi.nlm.nih.gov/pubmed/");
		urlMapping.put("NDEX.NETWORK","http://ndexbio.org/#/network/");
	}
	
	private String[] parseCURIE(String curie) {
		
		String[] parsedCurie = new String[3];
		
		int i = curie.indexOf(":");
		if (i == -1) { throw new RuntimeException("The statement id " + curie + "is not a curie and could not be parsed"); }
		String objId = curie.substring(i+1);
		String namespace = curie.substring(0, i);
		namespace = namespace.toUpperCase();
		
		// Check full namespace first for a match to the registered namespaces
		if (urlMapping.keySet().contains(namespace)) {
			parsedCurie[0] = curie;
			parsedCurie[1] = namespace;
			parsedCurie[2] = objId;
		} else {
			// otherwise, test the suffix of the id...
			i = namespace.lastIndexOf(".");
			namespace = namespace.substring(i+1);
			
			if (urlMapping.keySet().contains(namespace)) {
				// Return CURIE with a trimmed namespace
				parsedCurie[0] = namespace+":"+objId;
				parsedCurie[1] = namespace;
				parsedCurie[2] = objId;
			} else {
				parsedCurie[0] = curie;
				parsedCurie[1] = "";
				parsedCurie[2] = "";
			}
		}
		return parsedCurie;
	}
	
	private String getUrl(String id) {
		
		if(id.startsWith("http://") || id.startsWith("https://"))
			return id; // whoa! I'm a URL already?
		
		String[] pc = parseCURIE(id);
		
		if (! (pc[1].isEmpty()||pc[2].isEmpty())) {
			return urlMapping.get(pc[1]) + pc[2];
		} else {
			return "";
		}
	}
	
	@Autowired
	private KBQuery query ;

	//@Autowired
	//private AnnotationService annotationService ;
	
	public ReferenceView() { }
	
	private TextField refIdSearchField = new TextField();
	
	private Boolean IS_PUBMED_ARTICLE = false ;
	
	private String uri = "";

	private void setUri(String uri) {
		this.uri = uri;
	}
	
	private String getUri() {
		return uri;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		String annotationId = null;
		
		Optional<Annotation> annotationOpt = query.getCurrentAnnotation();
		
		String parameters = event.getParameters();
		if (parameters != null && !parameters.isEmpty()) {
			String encodedId = parameters.split("/")[0];
			
			try {
				annotationId = URLDecoder.decode(encodedId, StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				_logger.error("ReferenceView.enter() ERROR for encoded annotation id: '"+encodedId+"': "+e.getMessage());
				return;
			}
			
		} else if (annotationOpt.isPresent()) {
			annotationId = annotationOpt.get().getId();
		} else {
			return;
		}
		
		try {
			// TODO: At the moment, evidence id's should be URL's?
			URL url = new URL(annotationId);
			setUri(url.toString());
		} catch (MalformedURLException e) {
			setUri(getUrl(annotationId));
		}
		
		/*
		 *  Assume that you have something to work with
		 *  at this point so refresh the display!
		 */
		removeAllComponents();

		VerticalLayout localAbstractLayout = new VerticalLayout() ;	
		localAbstractLayout.setMargin(true);
		localAbstractLayout.setSpacing(true);
		
		HorizontalLayout abstractMenu = new HorizontalLayout();
		abstractMenu.setSpacing(true);
		
		refIdSearchField.setInputPrompt("Search Reference ID");
		String[] pc = parseCURIE(annotationId);
		refIdSearchField.setValue(pc[0]);
		
		Button referenceSearchBtn = new Button("GO");
		referenceSearchBtn.setClickShortcut(KeyCode.ENTER);
		
		Button showInNewWindowBtn        = new Button("Show Reference in New Window");
		Button showReferenceRelationsBtn = new Button("Show Associated Concept Relations");
		
		VerticalLayout articleLayout = new VerticalLayout();
		articleLayout.setHeight("100%");
		
		referenceSearchBtn.addClickListener(e-> {
			String accId = refIdSearchField.getValue().trim();
			setUri(getUrl(accId));
			openReferenceLink( articleLayout, getUri() );
		});
		
		showInNewWindowBtn.addClickListener(e -> {
			// display currently set uri - should be set
			getUI().getPage().open( getUri(), "_blank", false );
		});
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.addComponents( refIdSearchField, referenceSearchBtn );
		
		abstractMenu.addComponents(searchLayout, showInNewWindowBtn);
		
		// TODO: For now, don't give the user a 'pubmed relations' button unless they have such a beast!
		if(IS_PUBMED_ARTICLE) abstractMenu.addComponent(showReferenceRelationsBtn);
		
		setHeightUndefined();

		openReferenceLink(articleLayout, getUri());
		
		localAbstractLayout.addComponents(abstractMenu, articleLayout);
		localAbstractLayout.setHeight("100%");
		
		addComponent(localAbstractLayout);

		DesktopUI ui = (DesktopUI) UI.getCurrent() ;
		
		// TODO: This button event should only be visible if Pubmed entry is displayed?
		showReferenceRelationsBtn.addClickListener(e -> {
			
			String accId = refIdSearchField.getValue().trim();
			String pmid  = RdfUtil.getQualifiedObjectId(accId);

			query.setCurrentPmid(pmid);
			
			ui.showRelationsTab() ;
			
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
