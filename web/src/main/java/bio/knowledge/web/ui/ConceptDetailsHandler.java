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
package bio.knowledge.web.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import bio.knowledge.datasource.DataSourceException;
import bio.knowledge.datasource.wikidata.ConceptDescriptor;
import bio.knowledge.model.AnnotatedConcept;
import bio.knowledge.model.GeneralStatement;
import bio.knowledge.model.IdentifiedConcept;
import bio.knowledge.model.Predicate;
import bio.knowledge.model.PredicateImpl;
import bio.knowledge.model.RdfUtil;
import bio.knowledge.model.Statement;
import bio.knowledge.model.datasource.ResultSet;
import bio.knowledge.service.ConceptService;
import bio.knowledge.service.KBQuery;
import bio.knowledge.service.wikidata.WikiDataService;
import bio.knowledge.web.view.DescriptionBuilder;

@org.springframework.stereotype.Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConceptDetailsHandler {

	private Logger _logger = LoggerFactory.getLogger(ConceptDetailsHandler.class);

	private DesktopUI parentUi;

	@Autowired
	public ConceptDetailsHandler(DesktopUI ui) {
		this.parentUi = ui;
	}

	@Autowired
	private KBQuery query;

	private DescriptionBuilder descriptionBuilder = null;

	@Autowired
	private ConceptService conceptService;

	@Autowired
	private WikiDataService wikiDataService;

	public VerticalLayout getDetails(IdentifiedConcept selectedConcept) {
		FormLayout labelsLayout = new FormLayout();
		labelsLayout.setMargin(false);
		labelsLayout.setSpacing(false);
		labelsLayout.setWidthUndefined();

		// set up clique label
		Label cliqueLabel = new Label();
		cliqueLabel.setCaption("Clique Id:");
		cliqueLabel.setWidthUndefined();

		// set up accession label
		Label accessionLabel = new Label();
		accessionLabel.setCaption("Accession Id:");
		accessionLabel.setWidthUndefined();

		// set up name label
		Label nameLabel = new Label();
		nameLabel.setCaption("Name:");
		nameLabel.setWidthUndefined();
		
		// set up type label
		Label typeLabel = new Label();
		typeLabel.setCaption("Semantic Categories:");

		// set up taxon label
		Label taxonLabel = new Label();
		taxonLabel.setCaption("Taxon:");

		// set up aliases
		VerticalLayout aliasesLayout = new VerticalLayout();
		aliasesLayout.setCaption("Aliases:");
		aliasesLayout.setWidthUndefined();
		
		Panel aliasesContentPanel = new Panel();
		aliasesContentPanel.setStyleName("aliases-panel");
		aliasesLayout.addComponent(aliasesContentPanel);
				
		VerticalLayout aliasesContent = new VerticalLayout();
		aliasesContent.setWidthUndefined();
		aliasesContent.setMargin(new MarginInfo(false, true, false, false));
		aliasesContentPanel.setContent(aliasesContent);
		aliasesContentPanel.setWidthUndefined();
		
		labelsLayout.addComponents(nameLabel, cliqueLabel, accessionLabel, typeLabel, taxonLabel, aliasesLayout);

		VerticalLayout details = new VerticalLayout();
		details.setWidthUndefined();
		details.setStyleName("concept-details-layout");
		details.addComponent(labelsLayout);

		// return a skeleton layout if the selected concept is null
		if (selectedConcept == null) {
			return details;
		}
		
		/**
		 * Fill in the details layouts with data
		 */
		query.setCurrentSelectedConcept(selectedConcept);
		AnnotatedConcept annotatedConcept = (AnnotatedConcept) selectedConcept;

		// fill in labels
		cliqueLabel.setValue(annotatedConcept.getCliqueId());
		accessionLabel.setValue(annotatedConcept.getId());
		nameLabel.setValue(annotatedConcept.getName());
		typeLabel.setValue(String.join(", ", annotatedConcept.getCategories()));
		taxonLabel.setValue(annotatedConcept.getTaxon());
		
		// fill in aliases
		Set<String> aliases = annotatedConcept.getAliases();
		for (String alias : aliases) {
			Label aliasLabel = new Label(alias);
			aliasLabel.setWidthUndefined();
			aliasesContent.addComponent(aliasLabel);
		}
		if (aliases.size() <= 10) {
			aliasesContentPanel.setHeight(12, Unit.EM);
		} else {
			aliasesContentPanel.setHeight(25, Unit.EM);
		}
		
		try {
			// resetting descriptionBuilder
			descriptionBuilder = null; 
			conceptService.getDescription(this::updateDescription);
		} catch (TimeoutException te) {
			_logger.error(te.getMessage());
		} catch (DataSourceException dse) {
			_logger.error(dse.getMessage());
		} catch (Exception e) {
			_logger.error(e.getMessage());
		}
		
		boolean wiki_article_available = 
				descriptionBuilder != null && 
				!descriptionBuilder.getArticleUrl().equals("");
		
		if (wiki_article_available) {
			
			VerticalLayout article_uri_layout = new VerticalLayout();
			article_uri_layout.setCaption("Wikipedia Url:");
			Button articleDisplayButton = new Button(selectedConcept.getName());
			articleDisplayButton.setStyleName("concept-detail-button");
			articleDisplayButton.addClickListener(e -> displayUrl(descriptionBuilder.getArticleUrl()));
			article_uri_layout.addComponent(articleDisplayButton);
			labelsLayout.addComponent(article_uri_layout);
		}

		Map<Integer, Component> sordidDetails = new TreeMap<Integer, Component>();
		if (descriptionBuilder != null) {
			
			// TODO: Generalize the listing of tag=value pairs for diverse concept types!
			Map<String, String> conceptDetails = descriptionBuilder.getDetails();

			for (String key : conceptDetails.keySet()) {

				ConceptDescriptor descriptor = ConceptDescriptor.getByKey(key);

				if (descriptor != null) {
					String fieldValue = conceptDetails.getOrDefault(key, "Unknown");
					Component field = formatDisplayValue(selectedConcept, descriptor, fieldValue);
					field.setCaption(descriptor.getDescription() + ":");
					sordidDetails.put(descriptor.labelOrder(), field);
				} else {
					_logger.warn("onConceptDetailsSelection() WARNING: concept details key '" + key
							+ "' not yet mapped to ConceptDescriptor enum?");
				}
			}
		}

		/*
		 * Displays the concept detail data in the desired ConceptDescriptor
		 * order and layout
		 */
		for (Integer byOrder : sordidDetails.keySet()) {
			Component field = sordidDetails.get(byOrder);
			labelsLayout.addComponent(field);
		}
		return details;
	}

	/**
	 * 
	 * @param url
	 */
	public void displayUrl(String url) {
		if( !( url==null || url.isEmpty()) ) 
			parentUi.getPage().open(url, "_blank", false);
		else
			_logger.warn("displayUrl given a null or empty url?");
	}

	/**
	 * 
	 * @param accessionId
	 * @param name
	 */
	public void displayDataPage(String accessionId, String name) {

		// get Formatted URL here?
		String[] parameters = new String[1];
		parameters[0] = name;
		String propertyInfoUrl = wikiDataService.getFormattedUrl(accessionId, parameters);

		// no such URL?
		if (propertyInfoUrl == null || propertyInfoUrl.isEmpty())
			return;

		displayUrl(propertyInfoUrl);

	}

	/*
	 * May 18 iteration on concept definition presentation... Delegates a
	 * retrieval to the Concept back end retrieving the results via
	 * a Function<ResultSet,Void> implementation.
	 */
	private Void updateDescription(ResultSet rs) {
		if (!rs.isEmpty()) {

			if (descriptionBuilder == null)
				descriptionBuilder = new DescriptionBuilder();

			rs.forEach(descriptionBuilder);
		}
		return (Void) null;
	}

	/**
	 * This method applies specific formatting to each kind of property value.
	 * 
	 * @param currentConcept
	 *            whose information is being displayed
	 * @param descriptor
	 *            particular concept descriptor being displayed
	 * @param rawValue
	 *            the unparsed information String to be displayed (may have
	 *            embedded meta-data)
	 * @return the full Vaadin component to be displayed as the descriptor value
	 */
	private Component formatDisplayValue(IdentifiedConcept currentConcept, ConceptDescriptor descriptor, String rawValue) {
		String fieldId = descriptor.getQualifiedId();
		Boolean isRetrievable = descriptor.isRetrievable();

		// Large lists of id's rendered differently
		String[] ids = rawValue.split("\\;");
		if (ids.length > 2) {
			// Many multiple id's in list...
			// stuff them into a dropdown Combo Box widget
			ComboBox valueList = new ComboBox();
			valueList.setWidth("15em");

			Map<String, String> itemMap = new HashMap<String, String>();
			List<String> idList = new ArrayList<String>();
			for (String id : ids) {
				String[] idPart = id.split("\\|");
				String label = literalValue(idPart[0]);
				idList.add(label);
				if (idPart.length > 1) {
					// Map object ID if URI is available
					itemMap.put(label, RdfUtil.getObjectId(idPart[1]));
				}
			}

			valueList.addItems(idList);
			valueList.select(idList.get(0));

			HorizontalLayout valueSelection = new HorizontalLayout();
			valueSelection.setSpacing(true);
			valueSelection.addComponent(valueList);

			if (isRetrievable) {
				Button valueActionButton = new Button();
				valueActionButton.setStyleName("concept-detail-button");
				if (descriptor.isWikiDatum()) {
					valueActionButton.setCaption("Add to Map");
					valueActionButton.addClickListener(
							e -> addWikiDataItemToMapFromList(currentConcept, descriptor.getKey(), valueList, itemMap));

				} else {
					valueActionButton.setCaption("Display");
					valueActionButton.addClickListener(e -> displayItemFromList(fieldId, valueList, itemMap));
					valueSelection.addComponent(valueActionButton);
				}
				valueSelection.addComponent(valueActionButton);
			}

			return valueSelection;

		} else {

			HorizontalLayout valueBar = new HorizontalLayout();
			valueBar.setSpacing(true);

			VerticalLayout idStack = new VerticalLayout();

			for (String item : ids) {

				String[] idPart = item.split("\\|");
				String valueLabel = literalValue(idPart[0]);

				/*
				 * Hacky patch for Image files (would be better to update these
				 * WikiData properties with a formattedUrl property?
				 */
				if (fieldId.equals("wd:P692") || fieldId.equals("wd:P18")) {
					// get the file name (only)
					valueLabel = RdfUtil.getObjectId(valueLabel);
					// translate HTTP Encoding of spaces
					valueLabel = valueLabel.replaceAll("\\%20", " ");
					// remove file extension
					int extDot = valueLabel.lastIndexOf(".");
					if (extDot != -1)
						valueLabel = valueLabel.substring(0, extDot);
				}

				String uri = idPart[1]; // second half may be the URI

				final String valueObjectId = RdfUtil.getObjectId(uri);

				Component valueDisplay;
				if (isRetrievable) {
					if (descriptor.isWikiDatum()) {
						
						Label wikiValueLabel = new Label(valueLabel);
						Button addToMap = new Button("Add to Map");
						addToMap.setStyleName("concept-detail-button");
						
						addToMap.addClickListener(e -> {
							IdentifiedConcept object = conceptService.annotate("wd:" + valueObjectId);
							if(object==null) return;
							
							// If annotated 'object' concept exists, add it to the concept map!
							DesktopUI ui = (DesktopUI) UI.getCurrent();
							ui.addNodeToConceptMap(currentConcept);
							ui.addNodeToConceptMap(object);
							Predicate relation  = new PredicateImpl(descriptor.getKey());
							String accId = "wds:"+currentConcept.getCliqueId()+"_"+descriptor.name()+"_"+object.getCliqueId();
							Statement statement = new GeneralStatement(accId,currentConcept,relation,object);
							ui.addEdgeToConceptMap(statement);
						});
						
						HorizontalLayout wikiValueLayout = new HorizontalLayout();
						wikiValueLayout.addComponents(wikiValueLabel, addToMap);
						valueDisplay = wikiValueLayout;
						
					} else {
						
						Button valueDisplayButton = new Button(valueLabel);
						valueDisplayButton.setStyleName("concept-detail-button");
						valueDisplayButton.addClickListener( e -> displayDataPage(fieldId, valueObjectId) );
						valueDisplay = valueDisplayButton;
					}
				} else {
					valueDisplay = new Label(valueLabel);
				}
				idStack.addComponent(valueDisplay);
			}
			valueBar.addComponent(idStack);
			return valueBar;
		}
	}

	private void addWikiDataItemToMapFromList(IdentifiedConcept subject, String relation, ComboBox source, Map<String, String> itemMap) {
		
		String id = (String) source.getValue();
		if (itemMap.containsKey(id)) {
			
			/*
			 * WikiData id's are often clique id's, but perhaps not always... 
			 * this 'annotate' call may occasionally fail?
			 */
			IdentifiedConcept object = conceptService.annotate("wd:" + itemMap.get(id));
			
			if(object==null) return;
			
			// If annotated 'object' concept exists, add it to the concept map!
			DesktopUI ui = (DesktopUI) UI.getCurrent();
			ui.addNodeToConceptMap(subject);
			ui.addNodeToConceptMap(object);
			//TODO: June 13 2017 - Commenting out this line doesn't seem to break anything.
			//		This method now takes a Statement, and I don't see how to access that here.
//			ui.addEdgeToConceptMap(subject, object, relation);
		}
	}

	private void displayItemFromList(String accessionId, ComboBox source, Map<String, String> itemMap) {
		String id = (String) source.getValue();
		if (itemMap.containsKey(id))
			displayDataPage(accessionId, itemMap.get(id));
	}

	// L'il hack to ignore xmlschema languages
	private String literalValue(String literal) {
		String[] token = literal.split("\\@");
		return token[0];
	}
}
