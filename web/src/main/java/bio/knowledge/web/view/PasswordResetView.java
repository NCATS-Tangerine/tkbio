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

import com.vaadin.data.Validator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.InvalidPasswordResetToken;
import bio.knowledge.authentication.exceptions.PasswordLacksCapitalLetterOrNumberException;
import bio.knowledge.authentication.exceptions.PasswordTooShortException;
import bio.knowledge.web.design.ResetPasswordDesign;
import bio.knowledge.web.ui.DesktopUI;

@SpringView(name = PasswordResetView.NAME)
public class PasswordResetView extends ResetPasswordDesign implements View {

	private static final long serialVersionUID = 8181376937595758934L;

	public static final String NAME = "passwordReset?sptoken";
	
	private ClickListener clickListener;

	public PasswordResetView() { }

	
	@Override
	public void enter(ViewChangeEvent event) {
		
//		Here we are obtaining the encrypted password reset token from the password reset link (URL)
		String urlQuery = Page.getCurrent().getLocation().getQuery();
		
		urlQuery = urlQuery == null ? "" : urlQuery;
		
		String passwordResetToken = urlQuery.replaceFirst(Pattern.quote("sptoken="), "");
		
//		We don't want continueButton to have multiple ClickListeners, if we enter this view multiple times!
		if (clickListener != null) {
			continueButton.removeClickListener(clickListener);
		}
		
		clickListener = makeContinueBtnClickListener(passwordResetToken);
		continueButton.addClickListener(clickListener);
		
		setupPasswordFields();
		
		clearErrorMessage();
	}
	
	private void setupPasswordFields() {
		firstPassword.setValidationVisible(false);
		secondPassword.setValidationVisible(false);
		
		firstPassword.addValidator(value -> {
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
					displayErrorMessage("The password provided is not valid!\nMust contain a capital letter and a number.");
					throw new Validator.InvalidValueException(
							"The password provided is not valid! Must contain a capital letter and a number.");
				}
		    }
		});
		
		secondPassword.addValidator(value -> {
			if ( ! value.toString().equals(firstPassword.getValue())) {
				displayErrorMessage("Password entries do not match");
				throw new Validator.InvalidValueException("Password entries do not match");
			}
		});
		
		firstPassword.addValueChangeListener(event -> {
			if (!firstPassword.isValid() && !firstPassword.isEmpty()) {
				firstPassword.setValidationVisible(true);
			} else {
				firstPassword.setValidationVisible(false);
				clearErrorMessage();
			}
		});
		
		secondPassword.addValueChangeListener(event -> {
			if (!secondPassword.isValid() && !secondPassword.isEmpty()) {
				secondPassword.setValidationVisible(true);
			} else {
				secondPassword.setValidationVisible(false);
				clearErrorMessage();
			}
		});
	}

	private ClickListener makeContinueBtnClickListener(String passwordResetToken) {
		return event -> {
			try {

				DesktopUI ui = (DesktopUI)UI.getCurrent();
				ui.getAuthenticationManager().resetPassword(passwordResetToken, secondPassword.getValue());
				
//					We want to refresh the page and not merely navigate to the login view because
//					otherwise the password reset token will stick around in the URL. This wouldn't
//					necessarily be a bad thing, since the token cannot be reused and so for the sake
//					of security it's harmless. But, it's ugly and might confuse the user.
				Page.getCurrent().setLocation("/#!login");
				
			} catch (InvalidPasswordResetToken e1) {
				displayErrorMessage("The password reset link you followed is invalid, possibly because it has timed out.\n Please restart the recovery process");
			} catch (PasswordTooShortException e2) {
				displayErrorMessage("Your password is too short");
			} catch (PasswordLacksCapitalLetterOrNumberException e3) {
				displayErrorMessage("Your password must contain at least one capital letter and at least one number");
			}
		};
	}
	
	private void displayErrorMessage(String message) {
		errLabel.setVisible(true);
		errLabel.setValue(message);
	}
	
	private void clearErrorMessage() {
		errLabel.setVisible(false);
		errLabel.setValue("");
	}
}
