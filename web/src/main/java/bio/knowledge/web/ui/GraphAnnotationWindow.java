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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GraphAnnotationWindow extends Window {

	private static final long serialVersionUID = 4370377148629776171L;
	
	private ComboBox comboBox1 = new ComboBox();
	private ComboBox comboBox2 = new ComboBox();
	private Button button1 = new Button("Okay");
	private Button button2 = new Button("Cancel");
	
	public GraphAnnotationWindow(String title) {
		VerticalLayout mainLayout = new VerticalLayout();
		
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(makeTextField());
		mainLayout.addComponent(makeGridLayout());
		
		this.setContent(mainLayout);
		
		this.addStyleName("concept-search-window");
		this.center();
		this.setCaption(title);
//		this.setModal(true);
		this.setResizable(false);
	}

	private GridLayout makeGridLayout() {
		GridLayout gridLayout = new GridLayout(2, 2);
		
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(comboBox1);
		gridLayout.addComponent(comboBox2);
		gridLayout.addComponent(button1);
		gridLayout.addComponent(button2);
		
		gridLayout.setComponentAlignment(button1, Alignment.BOTTOM_RIGHT);
		gridLayout.setComponentAlignment(button2, Alignment.BOTTOM_LEFT);
		return gridLayout;
	}

	private Component makeTextField() {
		TextField textField = new TextField();
		textField.setWidth("100%");
		return textField;
	}
	
}
