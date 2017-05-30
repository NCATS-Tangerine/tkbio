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

import java.util.Map;

import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

import bio.knowledge.model.user.User;
import bio.knowledge.web.view.Registry.Mapping;

/**
 * I am refactoring all of the listview page components into here
 * @author lance
 *
 */
public class Book extends VerticalLayout {

	private static final long serialVersionUID = 8745030492769146270L;

	private final static int ROWS_TO_DISPLAY = 10;
	
	private final ListContainer listContainer;
	private Grid dataTable;
	
	
	public Book(String[] columnNames,
			Map<String, RendererClickListener> clickListenerMap,
			User user,
			Mapping mapping) {
		
		listContainer = new ListContainer(user);
		
		listContainer.setContainer(mapping.getContainer());
		listContainer.setPager(mapping.getPager());
		listContainer.setEntryCounter(mapping.getEntryCounter());
		listContainer.setHitCounter(mapping.getHitCounter());
		listContainer.setPageCounter(mapping.getPageCounter());
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(mapping.getContainer());
		
		dataTable = DataTableBuilder.build(columnNames, clickListenerMap);
		dataTable.setContainerDataSource(gpcontainer);
		
		PageSelectorBar pageSelectorBar = new PageSelectorBar(listContainer);
		PageSizeSelectorBar pageSizeSelectorBar = new PageSizeSelectorBar(listContainer, pageSelectorBar);
		
		this.addComponent(dataTable);
		this.addComponent(pageSelectorBar);
		this.addComponent(pageSizeSelectorBar);
		
		setupDataTableStyle(dataTable);
		
		fillWithData();
	}
	
	public void redraw() {
		PageSelectorBar pageSelectorBar = new PageSelectorBar(listContainer);
		PageSizeSelectorBar pageSizeSelectorBar = new PageSizeSelectorBar(listContainer, pageSelectorBar);
		
		this.removeAllComponents();
		this.addComponent(dataTable);
		this.addComponent(pageSelectorBar);
		this.addComponent(pageSizeSelectorBar);
	}

	private void setupDataTableStyle(Grid dataTable) {
		dataTable.setWidth("100%");
		dataTable.setHeightMode(HeightMode.ROW);
		dataTable.setHeightByRows(ROWS_TO_DISPLAY);
		dataTable.setImmediate(true);
		dataTable.addStyleName("results-grid");
	}
	
	private void fillWithData() {
		listContainer.setFirstPage();
		listContainer.setPageSize(10);
		
		listContainer.refresh();
	}
	
	public ListContainer getListContainer() {
		return this.listContainer;
	}
}
