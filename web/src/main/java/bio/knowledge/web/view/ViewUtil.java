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

import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.Renderer;

import bio.knowledge.renderer.ButtonRenderer;
import bio.knowledge.web.ui.DesktopUI;

/**
 * @author Richard
 *
 * Some common utility code to share with Views
 */
public class ViewUtil {
	
	public static final String HEADER_STYLENAME = "table-header" ;

	/**
	 * 
	 * @param view
	 * @param grid
	 * @param headerIds
	 */
	public static void formatGrid(BaseView view, Grid grid, String[] headerIds) {
		HeaderRow header = grid.getHeaderRow(0);
		for (String id : headerIds) {
			HeaderCell cell = header.getCell(id);
			String label = cell.getText();
			label = view.getMessage(label);
			cell.setText(label);
			cell.setStyleName(HEADER_STYLENAME);
		}
	}
	
	/**
	 * 
	 * @param grid
	 * @param columnId
	 * @param listener to be bound to the created button
	 */
	public static void makeButton(Grid grid, String columnId, RendererClickListener listener, String viewname) {
		Grid.Column column = grid.getColumn(columnId);
		
		String searchPhrase = ((DesktopUI) UI.getCurrent()).getDesktop().getSearch().getValue();
		
		Renderer<String> renderer;

		// We want to use our own custom ButtonRenderer for every column except
		// for these, since it messes up their CSS formatting. So we use
		// Vaadin's ButtonRenderer for these other columns.
//		if (!columnId.equals("library") && !columnId.equals("parents") && !columnId.equals("evidence")) {
//			renderer = new ButtonRenderer(listener);
//			if (searchPhrase != null && viewname.equals(ViewName.CONCEPTS_VIEW)) {
//				// #FF0000 is bright red
//				((ButtonRenderer) renderer).setHighlightProperties(searchPhrase.split(" "), "#FF0000");
//			}
//		} else {
//			renderer = new com.vaadin.ui.renderers.ButtonRenderer(listener);
//		}
		
		renderer = new com.vaadin.ui.renderers.ButtonRenderer(listener);
		
		column.setRenderer(renderer);
	}	
	/**
	 * 
	 * @param grid
	 * @param columnId
	 * @param listener
	 */
	public static void makeIconButton(Grid grid, String columnId, RendererClickListener listener) {
		Grid.Column column = grid.getColumn(columnId);
		ImageRenderer selectButton = new ImageRenderer();
		selectButton.addClickListener(listener);
		column.setRenderer(selectButton);
	}
}
