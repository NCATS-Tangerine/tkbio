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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
import com.vaadin.ui.Window;

import bio.knowledge.model.SemanticGroup;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.design.SemanticFilterDesign;

@SpringView(name = SemanticFilterView.NAME)
public class SemanticFilterView extends SemanticFilterDesign implements View {

	private static final long serialVersionUID = 3768288267000228449L;
	
	public static final String NAME = "semantic_filter" ;
	
	private KBQuery query;
	private boolean shouldRefresh = false;
	
	private Window window;
	private ListSelect semgroupList;

	private String viewName = "";

	private Set<SemanticGroup> typeSet;
	private Collection<Object> itemIds; // stores item ids (including categories)

	public SemanticFilterView(Window window, String viewName, KBQuery query) { 
		this.query = query;
		this.window = window;
		this.viewName = viewName;
			
		initialize();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		initialize();
	}

	private void refreshSemanticGroups(Boolean selectAll) {
		itemIds.clear();
		for (SemanticGroup type : SemanticGroup.values()) {
			String typeDescription = type.getDescription();
			itemIds.add(typeDescription);
		}
		if(selectAll)
			semgroupList.setValue(itemIds);
		else
			semgroupList.setValue(null);
	}
	
	@SuppressWarnings("unchecked")
	private void initialize() {
		removeAllComponents();
		
		typeSet = new HashSet<SemanticGroup>();
		itemIds = new LinkedHashSet<>();
		
		setMargin(new MarginInfo(false, true, true, true));
		
		semgroupList = new ListSelect();
		semgroupList.addStyleName("filter-ListSelect");
		semgroupList.setMultiSelect(true);
		semgroupList.setMultiSelect(true);
		semgroupList.setImmediate(true);
		
        semgroupList.addValueChangeListener(e -> {
            if (e.getProperty().getValue() != null){
                semgroupList.setValue(itemIds);
            }
        });
		
		// Add semantic type categories as root items in the ListSelect.
		for (SemanticGroup type : SemanticGroup.values()) {
			String typeDescription = type.getDescription();
			semgroupList.addItem(typeDescription);
		}
		
		// get the previous selected item if applicable
		if (!viewName.equals(ViewName.CONCEPTS_VIEW)) {
			
			Object value = query.getOtherSemGroupFilterValue();
			
			if (value != null) {
				if (value instanceof Collection) {
					itemIds.clear();
					itemIds.addAll((Collection<Object>) value);
					
					semgroupList.setValue(itemIds);
				}
			}
		}
		
		Label title = new Label("<h3><b>Select Concept SemanticGroup Filters:</b></h3>", ContentMode.HTML);
		
		HorizontalLayout titleBar = new HorizontalLayout();
		titleBar.addComponent(title);
		titleBar.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout nav = new HorizontalLayout();
		nav.setSpacing(true);
		nav.setMargin(new MarginInfo(false, false, true, false));
		nav.setStyleName("semanticfilter-nav", true);
		
		Button applyBtn     = new Button("Apply");
		Button cancelBtn    = new Button("Cancel");
		Button selectAllBtn = new Button("Select All");
		Button clearAllBtn  = new Button("Clear All");
		
		cancelBtn.addClickListener(e -> {
			window.close();
		});
		
		applyBtn.addClickListener(e -> {

			Iterator<Object> iterator = itemIds.iterator();
			while (iterator.hasNext()) {
				String itemId = (String)iterator.next();
				SemanticGroup type = SemanticGroup.lookUpByDescription(itemId);
				typeSet.add(type);
			}
			
			// maybe make this as one of the functions for kbquery
			if (viewName.equals(ViewName.CONCEPTS_VIEW)) {
				query.setInitialConceptTypes(typeSet);
			} else if (viewName.equals(ViewName.RELATIONS_VIEW)) {
				query.setConceptTypes(typeSet);
			}
			
			shouldRefresh = true;
			
			this.window.close();
		});
		
		selectAllBtn.addClickListener(e -> {
			refreshSemanticGroups(true);
		});
		
		clearAllBtn.addClickListener(e -> {
			refreshSemanticGroups(false);
		});
		
		nav.addComponents(applyBtn, cancelBtn, selectAllBtn, clearAllBtn);
	
		addComponents(titleBar, nav, semgroupList);
	}
	
	public boolean shouldRefresh() {
		return shouldRefresh;
	}
	
	public ListSelect getSelector() {
		return semgroupList;
	}
}