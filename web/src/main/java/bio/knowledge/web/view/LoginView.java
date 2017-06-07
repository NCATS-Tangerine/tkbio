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

import java.util.concurrent.Callable;

import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.AccountDisabledException;
import bio.knowledge.authentication.exceptions.AccountDoesNotExistException;
import bio.knowledge.authentication.exceptions.AccountIsLockedException;
import bio.knowledge.authentication.exceptions.AccountNotVerifiedException;
import bio.knowledge.authentication.exceptions.InvalidUsernameOrPasswordException;
import bio.knowledge.web.design.LoginDesign;
import bio.knowledge.web.ui.DesktopUI;

@SpringView(name = LoginView.NAME)
public class LoginView extends LoginDesign implements View {

	private static final long serialVersionUID = 4980347190367145426L;

	public static final String NAME = "login";
	
	private final Navigator navigator;
	
	public LoginView(Navigator navigator) {

		this.navigator = navigator;
		
		View thisView = this;
		
		this.navigator.addViewChangeListener(
				new ViewChangeListener() {

					private static final long serialVersionUID = 1405785127719210321L;
		
					@Override
					public boolean beforeViewChange(ViewChangeEvent event) {
		//				We don't want this listener to ever block navigation.
						return true;
					}
		
					@Override
					public void afterViewChange(ViewChangeEvent event) {
						View oldView = event.getOldView();
						
						if (oldView != null) {
							if (oldView == thisView) {
								executeAfterAuthentication = null;
							}
						}
					}
					
				});
		
		this.errorLabel.setVisible(false);
		
		setupPasswordField();
		setupEmailField();
		setupLoginBtn();
		setupPasswordResetBtn();
		setupSignUpBtn();
	}

	private void setupSignUpBtn() {
		this.signUpButton.addClickListener(event -> navigator.navigateTo(CreateAccountView.NAME));
	}

	private void setupPasswordResetBtn() {
		this.forgotPasswordButton.addClickListener(event -> navigator.navigateTo(RecoverAccountView.NAME));
	}

	private void setupLoginBtn() {
		this.loginButton.setClickShortcut(KeyCode.ENTER);
		this.loginButton.addClickListener(event -> {
			String usernameStr = emailField.getValue();
			String passwordStr = passwordField.getValue();
			
			passwordField.clear();
			
			try {
				DesktopUI ui = (DesktopUI)UI.getCurrent();
				ui.getAuthenticationManager().login(usernameStr, passwordStr);
			} catch (InvalidUsernameOrPasswordException exp1) {
				System.out.println(exp1);
				displayErrorMessage("Invalid username or password");
			} catch (AccountDoesNotExistException exp2) {
				System.out.println(exp2);
				displayErrorMessage("Account does not exist");
			}

			DesktopUI ui = (DesktopUI)UI.getCurrent();
			if ( ui.getAuthenticationManager().isUserAuthenticated()) {
				// If we have navigated to LoginView through the
				// navigateToAndExecute() method:
				if (executeAfterAuthentication != null) {

					// This method cannot be responsible for whatever
					// exceptions executeAfterAuthentication.call() throws.
					// So we simply re-throw whatever exception might come
					// up.
					try {
						executeAfterAuthentication.call();
					} catch (Exception e) {
						e.printStackTrace();

						throw new RuntimeException(e.getMessage());
					}

					executeAfterAuthentication = null;
				} else {
					navigator.navigateTo(LandingPageView.NAME);
				}
			}

		});
	}

	private void setupPasswordField() {
		passwordField.setValidationVisible(false);
		passwordField.addValidator(value -> {
			if (value.toString().isEmpty()) {
				return;
			}
			
			if (value instanceof String) {
				String password = (String)value;
				boolean hasUppercase = !password.equals(password.toLowerCase());
				boolean hasLowercase = !password.equals(password.toUpperCase());
				
				boolean isValid = hasUppercase &&
								hasLowercase &&
								password.length() >= 8 &&
								password.matches(".*[0-9].*");
				
								
				if (!isValid) {
					throw new Validator.InvalidValueException("The password provided is not valid!");
				}
		    }
		});
		
		passwordField.addValueChangeListener(event -> {
			if (!passwordField.isValid()) {
				passwordField.setValidationVisible(true);
				displayErrorMessage("Password must be eight characters long, and contain\nat least one number and at least one uppercase letter");
			} else {
				clearErrorMessage();
			}
		});
	}

	private void setupEmailField() {
		emailField.setCaption("Username or email:");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {		
		errorLabel.setVisible(false);
		passwordField.clear();
		emailField.clear();
	}
	
	private void displayErrorMessage(String message) {
		errorLabel.setVisible(true);
		errorLabel.setValue(message);
	}
	
	private void clearErrorMessage() {
		errorLabel.setVisible(false);
		errorLabel.setValue("");
	}
	
//	private static Navigator staticNavigator;
	private Callable<Void> executeAfterAuthentication;

	/**
	 * Use this method to navigate to a LoginView, and then navigate
	 * back upon authentication. The caller of this method is responsible for providing a Callable
	 * object that, when called, will do what is required to navigate back.
	 * 
	 * @param function
	 */	
	public void navigateToAndExecute(Callable<Void> executeAfterAuthentication) {
		this.executeAfterAuthentication = executeAfterAuthentication;
		this.navigator.navigateTo(LoginView.NAME);
	}

}
