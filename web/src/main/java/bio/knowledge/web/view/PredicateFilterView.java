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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import bio.knowledge.model.Predicate;
import bio.knowledge.model.util.Util;
import bio.knowledge.service.PredicateService;
import bio.knowledge.web.design.PredicateFilterDesign;

@SpringView(name = PredicateFilterView.NAME)
public class PredicateFilterView extends PredicateFilterDesign implements View, Util {

	private static final long serialVersionUID = 3768288267000228449L;
	
	private Logger _logger = LoggerFactory.getLogger(PredicateFilterView.class);

	private PredicateService predicateService;
	
	public static final String NAME = "predicate_filter" ;
	
	private boolean shouldRefresh = false;
	
	private Window window;
	private ListSelect predicateSelector;

	private Set<Predicate> predicateSet;
	//private Collection<Object> itemIds; // stores item ids (including categories)
	
	public PredicateFilterView(PredicateService predicateService, Window window) { 
		
		this.predicateService = predicateService;
		this.window = window;
			
		initialize();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		initialize();
	}

	private void initialize() {
		
		removeAllComponents();
		
		predicateSet = new HashSet<Predicate>();
		
		setMargin(new MarginInfo(false, true, true, true));
		
		TextField searchField = new TextField();
		
		searchField.addValueChangeListener( e -> {
			String queryText = searchField.getValue();
			if (nullOrEmpty(queryText)) {
				return;
			}
			queryText = queryText.trim();
			_logger.debug("Searching predicates for match to '"+queryText+"'");
			predicateSelector.removeAllItems();
			Optional<Set<Predicate>> matchingOpt = 
					predicateService.getMatchingPredicates(queryText);
			if(matchingOpt.isPresent())
				predicateSelector.addItems(matchingOpt.get());
		});
		
		predicateSelector = new ListSelect();
		predicateSelector.addStyleName("filter-tree");
		predicateSelector.setMultiSelect(true);
		predicateSelector.setImmediate(true);
		predicateSelector.setRows(10);
		predicateSelector.setWidth("100em");
		predicateSelector.addItems(predicateService.findAllPredicates());
		
		Set<Predicate> selectedSet = new HashSet<Predicate>();
        predicateSelector.addValueChangeListener(e -> {
        	@SuppressWarnings("unchecked")
			Set<Predicate> selection = 
        			(Set<Predicate>) e.getProperty().getValue();
            if ( selection != null) {
				selectedSet.addAll(selection);
            }
		});		

        ListSelect selectedPredicates = new ListSelect();
        selectedPredicates.addStyleName("filter-tree");
        selectedPredicates.setMultiSelect(true);
        selectedPredicates.setImmediate(true);
        selectedPredicates.setRows(10);
        selectedPredicates.setWidth("100em");

		Set<Predicate> removedSet = new HashSet<Predicate>();
		selectedPredicates.addValueChangeListener(e -> {
        	@SuppressWarnings("unchecked")
			Set<Predicate> selection = 
        			(Set<Predicate>) e.getProperty().getValue();
            if ( selection != null) {
				if (removedSet.containsAll(selection)) {
					removedSet.removeAll(selection);
				} else {
					removedSet.addAll(selection);
				}
            }
		});	
        
		Button selectBtn = new Button("Select");
		selectBtn.addClickListener(e -> {
			selectedPredicates.removeAllItems();
			selectedPredicates.addItems(selectedSet);
			selectedSet.clear();
		});
		
		Button removeBtn = new Button("Remove");
		removeBtn.addClickListener(e -> {
			selectedSet.removeAll(removedSet);
			removedSet.clear();
			selectedPredicates.removeAllItems();
			selectedPredicates.addItems(selectedSet);
		});
		
		HorizontalLayout selectionButtons = new HorizontalLayout();
		selectionButtons.setSpacing(true);
		selectionButtons.setMargin(true);
		selectionButtons.addComponents(selectBtn,removeBtn);
		selectionButtons.setComponentAlignment(selectBtn, Alignment.MIDDLE_LEFT);
		selectionButtons.setComponentAlignment(removeBtn, Alignment.MIDDLE_RIGHT);
		
		Label title = new Label("<h3><b>Select Relation Filter:</b></h3>", ContentMode.HTML);
		
		HorizontalLayout titleBar = new HorizontalLayout();
		titleBar.addComponent(title);
		titleBar.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout nav = new HorizontalLayout();
		nav.setSpacing(true);
		nav.setMargin(new MarginInfo(false, false, true, false));
		nav.setStyleName("predicatefilter-nav", true);

		Button doneBtn   = new Button("Done");
		Button cancelBtn = new Button("Cancel");
		
		doneBtn.addClickListener(e -> {
			shouldRefresh = true;
			window.close();
		});
		
		cancelBtn.addClickListener(e -> {
			window.close();
		});
		
		nav.addComponents(searchField, doneBtn, cancelBtn);
	
		setSpacing(true);
		addComponents(titleBar, nav, predicateSelector, selectionButtons, selectedPredicates);
	}
	
	public boolean shouldRefresh() {
		return shouldRefresh;
	}

	public Set<Predicate> getSelectedPredicates() {
		return predicateSet;
	}
}