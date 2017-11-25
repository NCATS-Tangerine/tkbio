package bio.knowledge.web.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class NewAccountDesign extends VerticalLayout {
	protected TextField firstnameField;
	protected TextField secondnameField;
	protected TextField emailfield;
	protected TextField usernameField;
	protected PasswordField firstPassword;
	protected PasswordField secondPassword;
	protected Button createButton;
	protected Label errorLabel;

	public NewAccountDesign() {
		Design.read(this);
	}
}
