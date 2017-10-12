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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.InvalidPasswordResetToken;
import bio.knowledge.authentication.exceptions.PasswordTooShortException;
import bio.knowledge.validation.InvalidValueException;
import bio.knowledge.validation.ValidationHandler;
import bio.knowledge.validation.Validator;
import bio.knowledge.web.design.ResetPasswordDesign;
import bio.knowledge.web.ui.DesktopUI;

@SpringView(name = PasswordResetView.NAME)
public class PasswordResetView extends ResetPasswordDesign implements View {

	private static final long serialVersionUID = 8181376937595758934L;

	public static final String NAME = "passwordReset";
	
	private String tokenString;
		
	private ValidationHandler validationHandler;
		
	public PasswordResetView() {
		
		validationHandler = new ValidationHandler(errorLabel, continueButton);
		validationHandler.addValidator(firstPassword, makeFirstPasswordValidator());
		validationHandler.addValidator(secondPassword, makeSecondPasswordValidator());
		
		continueButton.addClickListener(makeClickListener());
	
	}
	
	private ClickListener makeClickListener() {
		return event -> {
			if (doPasswordsMatch(firstPassword, secondPassword)) {
				
				DesktopUI ui = (DesktopUI)UI.getCurrent();
				String password = firstPassword.getValue();
				
				try {
					ui.getAuthenticationManager().resetPassword(tokenString, password);
					Notification.show("We have changed your password for you. You may now login.");
					ui.getApplicationNavigator().navigateTo(LoginView.NAME);
					
				} catch (InvalidPasswordResetToken e1) {
					Notification.show("Reset link has expired.", Type.WARNING_MESSAGE);
				} catch (PasswordTooShortException e2) {
					//Already handled by validation
				}
			}
		};
	}

	private Validator makeFirstPasswordValidator() {
		return value -> {
			boolean hasUppercase = !value.equals(value.toLowerCase());
			boolean hasLowercase = !value.equals(value.toUpperCase());
			boolean containsNumber = value.matches(".*[0-9].*");
			
			if (!hasUppercase) throw new InvalidValueException("Password must contain an uppercase letter");
			if (!hasLowercase) throw new InvalidValueException("Password must contain a lowercase letter");
			if (!containsNumber) throw new InvalidValueException("Password must contain a number");
			if (value.length() < 8) throw new InvalidValueException("Password must be eight characters long");
		};
	}
	
	private Validator makeSecondPasswordValidator() {
		return value -> {
			if (!value.equals(firstPassword.getValue())) {
				throw new InvalidValueException("Second password entry must match the first");
			}
		};
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		tokenString = event.getParameters();
		validationHandler.resetState();		
		
		if (! DesktopUI.getCurrent().getAuthenticationManager().isValidPasswordToken(tokenString)) {
			Notification.show("Reset link has expired.", Type.WARNING_MESSAGE);
		}
	}
	
	private boolean doPasswordsMatch(PasswordField p1, PasswordField p2) {
		return p1.getValue().equals(p2.getValue());
	}
}
