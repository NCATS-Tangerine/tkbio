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

import com.vaadin.v7.data.Validator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.AccountDoesNotExistException;
import bio.knowledge.authentication.exceptions.InvalidEmailFormatException;
import bio.knowledge.web.design.ForgotPasswordDesign;
import bio.knowledge.web.ui.DesktopUI;

@SpringView(name = RecoverAccountView.NAME)
public class RecoverAccountView extends ForgotPasswordDesign implements View {
	
	private static final long serialVersionUID = -4724517078279971405L;

	public static final String NAME = "recoveraccount";

	public RecoverAccountView() {
		setupRecoverBtn();
		setupEmailField();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		clearErrorMessage();
		nextStepLayout.setVisible(false);
	}

	private void setupRecoverBtn() {
		continueButton.addClickListener(event -> {
			
			try {
				
				DesktopUI ui = (DesktopUI)UI.getCurrent();
				ui.getAuthenticationManager().sendPasswordResetEmail(emailField.getValue());
				
				nextStepLayout.setVisible(true);
				
				continueButton.setEnabled(false);

			} catch (AccountDoesNotExistException e1) {
				displayErrorMessage("There is no account on record with that email");
			} catch (InvalidEmailFormatException e2) {
				displayErrorMessage("You have entered an invalid email format");
			}
		});
	}
	
	private void setupEmailField() {
		
		emailField.addValidator(value -> {
			if (value.toString().isEmpty()) {
				return;
			}
			
			if (value instanceof String) {
		        String email = (String)value;
		        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
		            throw new Validator.InvalidValueException("The e-mail address provided is not valid!");
		    }
		});
		
		emailField.setValidationVisible(false);
		emailField.setCaption("Username (must be an email):");
		
		emailField.addValueChangeListener(event -> {
			if (!emailField.isValid()) {
				emailField.setValidationVisible(true);
				displayErrorMessage("Username must be a valid email");
			} else {
				clearErrorMessage();
			}
		});
		
		
	}
	
	private void displayErrorMessage(String message) {
		errorLabel.setVisible(true);
		errorLabel.setValue(message);
	}
	
	private void clearErrorMessage() {
		errorLabel.setVisible(false);
		errorLabel.setValue("");
	}
	
}
