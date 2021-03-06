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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.Navigator;
//import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import bio.knowledge.web.design.SearchDesign;

/**
 * The actual view is created using Vaadin Designer.
 * To make changes to the view, open the html file with Vaadin Designer
 * This file may be used to program the functionality of the application
 * @author Richard (adapted from ConceptSearchResults.java by Farzin)
 */
public class LibrarySearchResults extends SearchDesign {

	private static final long serialVersionUID = -830476028269997945L;

	private Logger _logger = LoggerFactory.getLogger(LibrarySearchResults.class);
	
	private SpringViewProvider viewProvider ;

	public Panel getTablePanel() {
		return tablePanel;
	}
	
	@PostConstruct
	public void initialize() {
		
		_logger.trace("Initializing LibrarySearchResults...");
		
		/*
		 * Deprecated in favor of ListView dataTableLabel
		searchTitle.setValue("<center><h2>Viewing Available Concept Maps "+ queryName +"</h2></center>");
		
		searchTitle.setContentMode(ContentMode.HTML);
		 */
		
		Navigator navigator = new Navigator(UI.getCurrent(), tablePanel);
		navigator.addProvider(viewProvider);
		navigator.navigateTo(ViewName.LIST_VIEW + "/" + ViewName.LIBRARY_VIEW);
	}

    public LibrarySearchResults( SpringViewProvider provider ) {
        viewProvider = provider ;
        initialize() ;
    }


}
