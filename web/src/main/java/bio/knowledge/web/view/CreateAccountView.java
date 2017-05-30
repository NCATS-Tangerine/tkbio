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


import java.util.regex.Pattern;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.AccountDoesNotExistException;
import bio.knowledge.authentication.exceptions.EmailAlreadyInUseException;
import bio.knowledge.authentication.exceptions.InvalidEmailFormatException;
import bio.knowledge.authentication.exceptions.MissingEmailException;
import bio.knowledge.authentication.exceptions.MissingNameException;
import bio.knowledge.authentication.exceptions.PasswordLacksCapitalLetterOrNumberException;
import bio.knowledge.authentication.exceptions.PasswordTooShortException;
import bio.knowledge.authentication.exceptions.UsernameAlreadyInUseException;
import bio.knowledge.validation.InvalidValueException;
import bio.knowledge.validation.ValidationHandler;
import bio.knowledge.validation.Validator;
import bio.knowledge.web.design.NewAccountDesign;
import bio.knowledge.web.ui.DesktopUI;

@SpringView(name = CreateAccountView.NAME)
public class CreateAccountView extends NewAccountDesign implements View {

	private static final long serialVersionUID = -8798120980776993252L;

	public static final String NAME = "createaccount";

	private Navigator navigator;
	
	private ValidationHandler validationHandler;
	
	public CreateAccountView(Navigator navigator) {
		this.navigator = navigator;
		
		validationHandler = new ValidationHandler(errorLabel, createButton);
		
		validationHandler.addNonEmptyValidator(firstnameField, "First name");
		validationHandler.addNonEmptyValidator(secondnameField, "Last name");
		validationHandler.addRequiredMatchValidator(
				emailfield,
				"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
				"Email is invalid."
		);
		validationHandler.addNonEmptyValidator(usernameField, "Username");
		validationHandler.addValidator(firstPassword, makeFirstPasswordValidator());
		validationHandler.addValidator(secondPassword, makeSecondPasswordValidator());
		
		createButton.addClickListener(makeClickListener());
		
		emailfield.setCaption("Enter your email");
	}

	private ClickListener makeClickListener() {
		return e -> {
			if (doPasswordsMatch(firstPassword, secondPassword)) {
				String firstName = firstnameField.getValue();
				String lastName = secondnameField.getValue();
				String email = emailfield.getValue();
				String password = firstPassword.getValue();
				String username = usernameField.getValue();
				
				createAccount(username, firstName, lastName, email, password);
			}
		};
	}

	private Validator makeSecondPasswordValidator() {
		return value -> {
			if (!value.equals(firstPassword.getValue())) {
				throw new InvalidValueException("Second password entry must match the first");
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

	@Override
	public void enter(ViewChangeEvent event) {
		validationHandler.resetState();
	}

	private boolean doPasswordsMatch(PasswordField p1, PasswordField p2) {
		return p1.getValue().equals(p2.getValue());
	}
	
	private void createAccount(String username, String firstName, String lastName, String email,
			String password) {
		try {
			DesktopUI ui = (DesktopUI)UI.getCurrent();

			ui.getAuthenticationManager().createAccount(username, firstName, lastName, email, password);

			Notification.show("We have created an account for you. You may now login.");
			navigator.navigateTo(LoginView.NAME);

		} catch (EmailAlreadyInUseException e) {
			validationHandler.addProhibitedMatchValidator(emailfield, Pattern.quote(email), "That email is already in use.");
			validationHandler.validateAll();
		
		} catch (MissingNameException e) {
//			Already have a validator for this
		} catch (AccountDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsernameAlreadyInUseException e) {
			validationHandler.addProhibitedMatchValidator(usernameField, Pattern.quote(username), "That username is already in use.");
			validationHandler.validateAll();
		}
	}
}
