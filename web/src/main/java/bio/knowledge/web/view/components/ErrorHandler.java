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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Validatable;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.ui.Label;

public class ErrorHandler {
	private List<Object> components = new ArrayList<Object>();
	private Map<Object, String> errors = new HashMap<Object, String>();

	private Label errorLabel;

	public ErrorHandler(Label errorLabel) {
		this.errorLabel = errorLabel;
		this.errorLabel.setVisible(false);
	}

	public <T extends Validatable & Property.ValueChangeNotifier> void addComponent(T component) {
		components.add(component);
		component.addValueChangeListener(event -> validate(component));
	}

	public <T extends Validatable & Property.ValueChangeNotifier> void addComponents(T... components) {
		for (T component : components) { 
			addComponent(component);
		}
	}
	
	public boolean isAllValid() {
		for (Object component : this.components) {
			if (!validate((Validatable) component)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean validate(Validatable component) {
		try {
			component.validate();
			clearError(component);
			return true;
		} catch (Validator.InvalidValueException e) {
			updateError(component, e.getMessage());
			return false;
		}
	}

	private void updateError(Object component, String message) {
		errors.put(component, message);
		errorLabel.setValue(message);
		errorLabel.setVisible(true);
	}

	private void clearError(Object component) {
		if (errors.containsKey(component)) {
			errors.remove(component);
		}

		if (errors.isEmpty()) {
			errorLabel.setVisible(false);
		} else {
			errorLabel.setVisible(true);
			
			for (String message : errors.values()) {
				errorLabel.setValue(message);
				break;
			}
		}
	}
}
