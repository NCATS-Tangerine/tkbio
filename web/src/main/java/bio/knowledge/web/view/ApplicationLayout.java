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

import java.util.ArrayList;
import java.util.Iterator;

import com.stormpath.sdk.account.Account;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import bio.knowledge.authentication.AuthenticationContext;
import bio.knowledge.authentication.AuthenticationListener;
import bio.knowledge.authentication.AuthenticationManager;
import bio.knowledge.authentication.UserProfile;
import bio.knowledge.web.design.ApplicationViewDesign;
import bio.knowledge.web.ui.DesktopUI;

/**
 * The main layout used in the application to help the users
 * navigate to other views such as Home, About, etc.
 * 
 * @author Colin Qiao
 * @author Lance Hannestad
 */
public class ApplicationLayout extends ApplicationViewDesign implements ViewDisplay {
	private static final long serialVersionUID = 3810076064386409110L;

	public static final String NAME = "application";
	
	private static final String SELECTED_STYLE = "selected";
	
	private Navigator navigator;
	private LoginView loginView;

	private AuthenticationContext context;

	public AuthenticationContext getContext() {
		return context;
	}

	public void setContext(AuthenticationContext context) {
		this.context = context;
	}
	
	public LoginView getLoginView() {
		return this.loginView;
	}

	/**
     * Creates the main layout for the application without any {@link ViewProvider}s
     * that supports navigating to different views by clicking the menu buttons
     * on the side bar. The views will be displayed in the content area (a panel).
     * <p>
     * <strong>Note:</strong> this constructor does not register the DesktopView 
     * as it is initialized in the DesktopUI.
	 * @param authenticationManager 
     */
	public ApplicationLayout(
			AuthenticationManager authenticationManager, 
			AuthenticationContext context
		) {
		
		this.setContext(context);
		navigator = new Navigator(UI.getCurrent(), (ViewDisplay)this);
		navigator.setErrorView(new ErrorView());
		
		setupRestrictedAccess(authenticationManager);
		
		// Register views. Note: the DesktopView is registered in the DesktopUI class.
		
		View userAccountView = new UserAccountView(navigator);
		authenticationManager.addListener((AuthenticationListener) userAccountView);
		this.loginView = new LoginView(navigator);
		
		navigator.addView(FaqView.NAME, new FaqView());
		navigator.addView(AboutView.NAME, new AboutView());
		navigator.addView(ContactView.NAME, new ContactView());
		navigator.addView(LandingPageView.NAME, new LandingPageView());
		navigator.addView(LoginView.NAME, this.loginView);
		navigator.addView(UserAccountView.NAME, userAccountView);
		navigator.addView(CreateAccountView.NAME, new CreateAccountView(navigator));
		navigator.addView(RecoverAccountView.NAME, new RecoverAccountView());
		navigator.addView(PasswordResetView.NAME, new PasswordResetView());
		
		setCommonNavigationViews();
		
		// Navigates to the landing page view if the URI fragment is empty
		if (navigator.getState().isEmpty()) {
			navigator.navigateTo(LandingPageView.NAME);
		}
		
		logoutBtn.setVisible(false);
		userAccountBtn.setVisible(false);
		
		logoutBtn.addClickListener(event -> {
			authenticationManager.logout();
//			navigator.navigateTo(LandingPageView.NAME);
		});
		
		AuthenticationListener authListener = new AuthenticationListener() {
			@Override
			public void onLogin(Account account) {
				loginBtn.setVisible(false);
				logoutBtn.setVisible(true);
				userAccountBtn.setVisible(true);
			}
			@Override
			public void onLogout() {
				loginBtn.setVisible(true);
				logoutBtn.setVisible(false);
				userAccountBtn.setVisible(false);
			}
		};
		
		this.addBeaconBtn.addClickListener(event -> {
			((DesktopUI) UI.getCurrent()).openKnowledgeBeaconWindow();
		});
		
		authenticationManager.addListener(authListener);
		
//		I had trouble getting the stormpath password reset link (which contains the password reset token) to direct
//		to the passwordReset view by default. This is how I am getting around the problem. I made the password reset link have
//		"!passwordReset" as its URI fragment, and now any time "!passwordReset" is in the URI fragment upon the instantiation
//		of this class (i.e., whenever a new page is loaded), it immediately redirects to the password reset view with the password
//		reset token portion of the URL automatically passed along.
		if (Page.getCurrent().getUriFragment() != null) {
			if (Page.getCurrent().getUriFragment().toString().equals("!passwordReset")) {
				navigator.navigateTo(PasswordResetView.NAME);
			}
		}
	}
	
	private void setupRestrictedAccess(AuthenticationManager authenticationManager) {
		navigator.addViewChangeListener(new ViewChangeListener() {

			private static final long serialVersionUID = 3072524967763730143L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				View view = event.getNewView();
				
				if (view instanceof RestrictedAccessView) {
					DesktopUI ui = (DesktopUI) UI.getCurrent();
					if (ui.getAuthenticationManager().isUserAuthenticated()) {
						RestrictedAccessView restrictedView = (RestrictedAccessView) view;
						
						UserProfile userProfile = ui.getAuthenticationManager().getCurrentUser();
						
						boolean accessDenied = ! userProfile.userHasOneOfAccessRoles(restrictedView.permittedRoles());
						
						if (accessDenied) {
							Notification.show("Your account does not have permission to access this view" , Type.WARNING_MESSAGE);
							return false;
						} else {
							return true;
						}
						
					} else {
						Notification.show("Permission denied to unauthenticated users", Type.WARNING_MESSAGE);
						return false;
					}
					
				} else {
					return true;
				}
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				// DO nothing
			}
			
		});
	}
	
	private void setCommonNavigationViews() {
		setNavigationView(FaqView.NAME, FaqView.class, faqBtn);
		setNavigationView(AboutView.NAME, AboutView.class, aboutBtn);
		setNavigationView(ContactView.NAME, ContactView.class, contactBtn);
		setNavigationView(LandingPageView.NAME, LandingPageView.class, homeBtn);
		setNavigationView(LoginView.NAME, LoginView.class, loginBtn);
		setNavigationView(UserAccountView.NAME, UserAccountView.class, userAccountBtn);
	}

	/*
     * Returns the navigator used by the application layout
     * 
     * @return The navigator.
     */
	public Navigator getNavigator() {
		return navigator;
	}
	
	/**
     * Returns the <em>Home</em> button used by the application layout.
     * <p>
     * Probably do not need to do this if the Home button initialization code
     * is move entirely into the {@link DesktopView}.
     * 
     * @return The <em>Home</em> button.
     */
	public Button getHomeButton() {
		return homeBtn;
	}
	
	/**
     * Associates views to be navigated with the specified menu button.
     * 
     * The class of the view will be used to determine the currently
     * selected menu button so that the appropriate style will be applied.
     * 
     * @param viewName
     *            The name of the view.
     * @param viewClass
     * 			  The class of the view.
     * @param menuButton
     * 			  The menu button to be associated with the view.
     */
	public void setNavigationView(String viewName, 
			Class<? extends View> viewClass, Button menuButton) {
		menuButton.setData(viewClass.getName());
		menuButton.addClickListener(event -> {
			removeOpenWindows();
			navigator.navigateTo(viewName);
		});
	}
	
	private void removeOpenWindows() {
		for(Window w : new ArrayList<Window>(UI.getCurrent().getWindows()))  {
			UI.getCurrent().removeWindow(w);
		}
	}

	/**
     * Applies the <strong>selected</strong> style to the component based on the data.
     * <p>
     * This is used to set the styles for the selected menu button
     * in the side bar of the main application view.
     * 
     * @param component
     *            The component needed to set the style.
     * @param data
     * 			  The data used to be check whether this is the correct component.
     */
	private void setStyleByData(Component component, Object data) {
		if (component instanceof AbstractComponent) {
			AbstractComponent abstractComponent = (AbstractComponent) component;
			
			if (data != null && data.equals(abstractComponent.getData())) {
				component.addStyleName(SELECTED_STYLE);
			} else {
				component.removeStyleName(SELECTED_STYLE);
			}
		}
	}
	
	// This method will be called internally by the Navigator class.
	@Override
	public void showView(View view) {
	    if (view instanceof Component) {
            content.setContent((Component) view);
            
            for (final Iterator<Component> i = side_bar.iterator(); i.hasNext();) {
            	Component component = i.next();
            	
            	setStyleByData(component, view.getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("View is not a component: "
                    + view);
        }
	}
}
