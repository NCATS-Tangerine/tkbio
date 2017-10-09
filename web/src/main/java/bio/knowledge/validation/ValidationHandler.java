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
package bio.knowledge.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

/**
 * This class handles validation of {@code AbstractField<String>} objects. I
 * have chosen this restriction for simplicity and because this is all that is
 * currently needed. In the future this class could be altered if there is need
 * for validation of sorts of Vaadin components.
 * 
 * I developed this because the validation machinery offered by Vaadin was
 * giving me too much grief. This class allows me to manage validation errors
 * exactly as I want to. Notably, I have introduced an error label which is a
 * better way to display error messages than how Vaadin does it.
 * 
 * @author Lance Hannestad
 *
 */
public class ValidationHandler {
	private Map<AbstractField<String>, List<Validator>> map = new HashMap<AbstractField<String>, List<Validator>>();
	private Map<Object, String> errors = new HashMap<Object, String>();
	private Label errorLabel;
	private boolean isControlButtonClicked = false;
	private Map<Object, Boolean> isEdited = new HashMap<Object, Boolean>();

	/**
	 * Instantiates a {@code ValidationHandler} with a control button.
	 * 
	 * @param errorLabel
	 *            Is updated to display error messages of components as the user
	 *            edits their value. It will be set to not visible if there are
	 *            no error messages to display.
	 * @param controlButton
	 *            When pressed, triggers validation of all components whether
	 *            they have been edited or not.
	 */
	public ValidationHandler(Label errorLabel, Button controlButton) {
		this(errorLabel);

		controlButton.addClickListener(e -> {
			isControlButtonClicked = true;
			validateAll();
		});
	}

	/**
	 * Instantiates a {@code ValidationHandler} without a control button.
	 * Validation errors will only be displayed as the respective components are
	 * edited.
	 * 
	 * @param errorLabel
	 *            Is updated to display error messages. It will be set to not
	 *            visible if there are no error messages to display.
	 */
	public ValidationHandler(Label errorLabel) {
		this.errorLabel = errorLabel;
		this.errorLabel.setVisible(false);
	}

	/**
	 * Adds a user-defined {@code Validator} object for the specified component.
	 * 
	 * @param component
	 * @param validator
	 */
	public void addValidator(AbstractField<String> component, Validator validator) {
		addComponent(component);
		List<Validator> validators = map.get(component);
		validators.add(validator);
	}

	/**
	 * Adds a {@code Validator} object that prohibits values that are empty for
	 * the specified component.
	 * 
	 * @param component
	 * @param nameOfField
	 *            The name of the component being validated. This is used for
	 *            error reporting.
	 */
	public void addNonEmptyValidator(AbstractField<String> component, String nameOfField) {
		addComponent(component);
		List<Validator> validators = map.get(component);
		validators.add(value -> {
			if (value.isEmpty())
				throw new InvalidValueException(nameOfField + " cannot be empty.");
		});
	}

	/**
	 * Adds a {@code Validator} object that prohibits values that match the
	 * specified regex pattern for the specified component
	 * 
	 * @param component
	 * @param regex
	 * @param errorMessage
	 *            The message to display upon failed validation
	 */
	public void addProhibitedMatchValidator(AbstractField<String> component, String regex, String errorMessage) {
		addComponent(component);
		List<Validator> validators = map.get(component);
		validators.add(value -> {
			if (value.matches(regex)) {
				throw new InvalidValueException(errorMessage);
			}
		});
	}

	/**
	 * Adds a {@code Validator} object that prohibits values that do not match
	 * the specified regex pattern for the specified component
	 * 
	 * @param component
	 * @param regex
	 * @param errorMessage
	 *            The message to display upon failed validation
	 */
	public void addRequiredMatchValidator(AbstractField<String> component, String regex, String errorMessage) {
		addComponent(component);
		List<Validator> validators = map.get(component);
		validators.add(value -> {
			if (!value.matches(regex)) {
				throw new InvalidValueException(errorMessage);
			}
		});
	}

	private void addComponent(AbstractField<String> component) {
		if (!map.containsKey(component)) {
			map.put(component, new LinkedList<Validator>());
		}

		component.addValueChangeListener(value -> {
			isEdited.put(component, Boolean.TRUE);
			validateAll();
		});

		isEdited.put(component, Boolean.FALSE);
	}

	/**
	 * Resets the state of the {@code ValidationHandler} as it was before any
	 * validation was attempted. This will remove all signs of error and clear
	 * the value of all components, even if some components are in error as a
	 * result (e.g., if they are prohibited from being empty). If a control
	 * button is being used, it will be as if it hasn't yet been clicked.
	 */
	public void resetState() {
		for (AbstractField<String> component : map.keySet()) {
			component.clear();
			clearError(component);
			isEdited.put(component, Boolean.FALSE);
			isControlButtonClicked = false;
		}
	}

	/**
	 * Attempts to validate all components, updating the error reporting as
	 * needed. It will not validate components that the user hasn't yet
	 * interacted with, unless the control button has been clicked. As such,
	 * even though there are no validation errors reported to the user
	 * interface, this method may still return false.
	 * 
	 * @return <b>true</b> if all components are valid, otherwise <b>false</b>.
	 */
	public boolean validateAll() {
		boolean isValid = true;

		for (AbstractField<String> component : map.keySet()) {
			boolean result = validate(component);

			if (!result) {
				isValid = false;
			}
		}

		return isValid;
	}

	/**
	 * Attempts to validate the specified component, updating the error
	 * reporting as needed. This method can be used to force error reporting on
	 * a component that hasn't yet been edited. This could be useful if you have
	 * added a new {@code Validator} object, and wish it to be triggered
	 * immediately. After having called this method, the error reporting will
	 * behave as if the components value had been edited.
	 * 
	 * @return <b>true</b> if component is valid, otherwise <b>false</b>.
	 */
	public boolean validateImmediately(AbstractField<String> component) {
		isEdited.put(component, Boolean.TRUE);
		return validate(component);
	}

	private boolean validate(AbstractField<String> component) {
		boolean isValid = true;

		for (Validator validator : map.get(component)) {
			try {
				validator.validate(component.getValue());
				clearError(component);
			} catch (InvalidValueException e) {
				isValid = false;
				if (this.isControlButtonClicked || isEdited.get(component)) {
					updateError(component, e.getErrorMessage());
				}
				break;
			}
		}

		if (isValid) {
			clearError(component);
		}

		return isValid;
	}

	private void updateError(AbstractField<String> component, String message) {
		component.setComponentError(new UserError(message));

		errors.put(component, message);
		errorLabel.setValue(message);
		errorLabel.setVisible(true);
	}

	private void clearError(AbstractField<String> component) {
		component.setComponentError(null);

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
