package bio.knowledge.web.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
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
public class ApplicationViewDesign extends VerticalLayout {
	protected HorizontalLayout main_area;
	protected CssLayout side_bar;
	protected NativeButton homeBtn;
	protected NativeButton faqBtn;
	// protected NativeButton aboutBtn; // deprecated 'about' as redundant to home landing page, for now
	protected NativeButton contactBtn;
	protected NativeButton loginBtn;
	protected NativeButton logoutBtn;
	protected NativeButton userAccountBtn;
	protected NativeButton manageBeaconBtn;
	
	protected Panel content;

	public ApplicationViewDesign() {
		Design.read(this);
	}
}
