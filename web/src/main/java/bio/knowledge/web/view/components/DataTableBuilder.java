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

import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

/**
 * A factory class for building the Grid components that are used as data tables
 * throughout our project's UI.
 * 
 * @author lance
 *
 */
public class DataTableBuilder {

	/**
	 * Builds a Grid with the specified column names
	 * 
	 * @param columnNames
	 * @return
	 */
	private static Grid build(String[] columnNames) {
		Grid dataTable = new Grid();

		for (String column : columnNames) {
			dataTable.addColumn(column);
		}

		return dataTable;
	}

	/**
	 * Builds a grid with the specified column names, and sets up click
	 * listeners for the specified columns.
	 * 
	 * @param columnNames
	 * @param clickListenerMapping
	 *            The keys must be a subset of columnNames. This is a mapping of
	 *            column names to click listeners. If set to <i>null</i> then no
	 *            click listeners will be set.
	 * @return
	 */
	public static Grid build(String[] columnNames, Map<String, RendererClickListener> clickListenerMapping) {
		Grid dataTable = build(columnNames);

		if (clickListenerMapping != null) {
			for (String columnName : clickListenerMapping.keySet()) {
				Grid.Column column = dataTable.getColumn(columnName);
				ButtonRenderer selectColumnButton = new ButtonRenderer();
				selectColumnButton.addClickListener(clickListenerMapping.get(columnName));
				column.setRenderer(selectColumnButton);
			}
		}

		return dataTable;
	}

}
