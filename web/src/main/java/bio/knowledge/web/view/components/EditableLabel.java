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

import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class EditableLabel extends VerticalLayout {

	private static final long serialVersionUID = -7310113609672304357L;
	
	private Label label;
	private TextArea textField;
	
	//private String value;
	
	public EditableLabel(String value) {
		//this.value = value;
		
		this.label = new Label();
		this.textField = new TextArea();
		this.textField.setValue(value);
		this.label.setValue(value);
		
		this.addComponent(label);
		this.addComponent(textField);
		
		this.label.setContentMode(ContentMode.HTML);
		
		this.textField.setVisible(false);
		this.label.setVisible(true);
		
		this.label.setHeight(this.textField.getHeight(), this.textField.getHeightUnits());
		
		this.textField.setSizeFull();
		this.label.setSizeFull();
		
		this.textField.addBlurListener(event -> {
			label.setValue(textField.getValue());
			
			label.setVisible(true);
			textField.setVisible(false);
		});
		
		this.addLayoutClickListener(event -> {
			textField.setValue(label.getValue());
			
			label.setVisible(false);
			textField.setVisible(true);
		});
	}
	
	/*
	private void swap(AbstractField<String> a, AbstractField<String> b) {
		this.value = b.getValue();
		a.setValue(this.value);
		
		a.setVisible(true);
		b.setVisible(false);
	}
	*/
}
