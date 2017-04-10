package bio.knowledge.web.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
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
public class AboutUserDesign extends VerticalLayout {
	protected TabSheet tabsheet;
	protected TextField firstnameField;
	protected TextField middlenameField;
	protected TextField lastnameField;
	protected TextField emailField;
	protected TextField usernameField;
	protected TextField facebookField;
	protected TextField linkedInField;
	protected TextField twitterField;
	protected Button updateButton;
	protected OptionGroup optionGroup;
	protected Label errorLabel;
	protected VerticalLayout groupLayout;

	public AboutUserDesign() {
		Design.read(this);
	}
}
