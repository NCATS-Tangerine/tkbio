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
package bio.knowledge.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.resource.ResourceException;
import com.vaadin.ui.UI;

import bio.knowledge.authentication.exceptions.AccountDisabledException;
import bio.knowledge.authentication.exceptions.AccountDoesNotExistException;
import bio.knowledge.authentication.exceptions.AccountIsLockedException;
import bio.knowledge.authentication.exceptions.AccountNotVerifiedException;
import bio.knowledge.authentication.exceptions.AuthenticationException;
import bio.knowledge.authentication.exceptions.EmailAlreadyInUseException;
import bio.knowledge.authentication.exceptions.InvalidEmailFormatException;
import bio.knowledge.authentication.exceptions.InvalidPasswordResetToken;
import bio.knowledge.authentication.exceptions.InvalidUsernameOrPasswordException;
import bio.knowledge.authentication.exceptions.MissingEmailException;
import bio.knowledge.authentication.exceptions.MissingNameException;
import bio.knowledge.authentication.exceptions.PasswordLacksCapitalLetterOrNumberException;
import bio.knowledge.authentication.exceptions.PasswordTooShortException;
import bio.knowledge.authentication.exceptions.UsernameAlreadyInUseException;
import bio.knowledge.service.Cache;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.LandingPageView;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticationManager {

	@Autowired
	private AuthenticationContext context;

	private org.springframework.security.authentication.AuthenticationManager manager;
	
	@Autowired
	private UserManager userManager;
	
	private final List<AuthenticationListener> authListeners = new ArrayList<AuthenticationListener>();
	
	private UserProfile currentUser;
	
	public UserProfile getCurrentUser() {
		return currentUser;
	}
	
	public boolean isUserAuthenticated() {
		return currentUser != null;
	}
	
//	public boolean isAccessGranted(String viewName) {
//		Role[] permissions = ResourcePermissions.getViewPermissions( viewName ) ;
//		
//		//TODO: Should this be "permissions != null" ? If there are no permissions then we shouldn't give acccess??
//		if( permissions == null || userHasOneOfAccessRoles( permissions ) )
//			return true ;
//		else {
//			return false ;
//		}
//	}
	
	public AuthenticationManager() { }
	
	
	public void addListener(AuthenticationListener listener) {
		authListeners.add(listener);
	}
	
	private void notifyOfLogin(UserProfile user) {
		for (AuthenticationListener listener : this.authListeners) {
			listener.onLogin(user);
		}
	}
	
	private void notifyOfLogout() {
		for (AuthenticationListener listener : this.authListeners) {
			listener.onLogout();
		}
	}
	
	private List<GrantedAuthority> defaultRoles() {
		List<GrantedAuthority> defaultRoles = new ArrayList<>();
		String client = Role.CLIENT.getSpringRole();
		GrantedAuthority clientAuth = new SimpleGrantedAuthority(client); //todo: make Role -> GA?
		defaultRoles.add(clientAuth);
		return defaultRoles;
	}
	
	public void logout() {
		//TODO: Delete any cookies that have been put on the users computer for persistent authentication
		this.currentUser = null;
		notifyOfLogout();
		
		DesktopUI ui = (DesktopUI) UI.getCurrent();
		ui.clearSession();
		ui.getApplicationNavigator().navigateTo(LandingPageView.NAME);
	}
	
	public void login(String usernameOrEmail, String password)
			throws InvalidUsernameOrPasswordException, AccountDisabledException, AccountNotVerifiedException,
			AccountIsLockedException, AccountDoesNotExistException {
				
		boolean invalidUsername = usernameOrEmail == null || usernameOrEmail.isEmpty();
		boolean invalidPassword = password == null || password.isEmpty();
		
		if (invalidUsername || invalidPassword) {
			throw new InvalidUsernameOrPasswordException("Username and password strings cannot be empty or null");
		}
		
		Authentication token = new UsernamePasswordAuthenticationToken(usernameOrEmail, password); //todo: handle email or password
		Authentication auth;
		try {
			auth = manager.authenticate(token);
			notifyOfLogin(currentUser);
		} catch(org.springframework.security.core.AuthenticationException e) {
			throw new InvalidUsernameOrPasswordException(e.getMessage());
		}
		SecurityContextHolder.getContext().setAuthentication(auth);
		currentUser = userManager.loadUserByUsername(usernameOrEmail);
		
		/*UsernamePasswordRequestBuilder builder = UsernamePasswordRequests.builder();
		builder.setUsernameOrEmail(usernameOrEmail);
		builder.setPassword(password);
		
		@SuppressWarnings("rawtypes")
		AuthenticationRequest request = builder.build();
		
		try {
			AuthenticationResult result = context.getApplication().authenticateAccount(request);
			
			Account account = result.getAccount();
			currentUser = new UserProfile(account);
			
			notifyOfLogin(currentUser);
			
			// because some data are visible or not visible depending on the user, 
			// we need to clear out the cache which may have been calibrated to data for a different user or no user.
			cache.resetCache();
			
			// TODO: do we need to reset other things as well?
			// DesktopUI ui = (DesktopUI) UI.getCurrent();
			// ui.clearSession();
			
			for(GroupMembership m : account.getGroupMemberships()) {
		    	Group group = m.getGroup() ;
		    	// group named by user role
		    	String groupName = group.getName();
		    	try {
		    		// Record the user group for this user
			    	Role role = Role.lookUp(groupName) ;
			    	currentUser.setUserAccessRole(role, true) ;
		    	} catch (UserAuthenticationException uae) {
		    		// unknown group - just ignore here?
		    		continue ;
		    	}
		    }
			
			query.currentUserId(currentUser.getId());
			
		} catch (ResourceException ex) {
			if (ex.getCode() == 7100 || ex.getCode() == 2006) {
				throw new InvalidUsernameOrPasswordException(ex.getMessage());
			} else if (ex.getCode() == 7101) {
				throw new AccountDisabledException(ex.getMessage());
			} else if (ex.getCode() == 7102) {
				throw new AccountNotVerifiedException(ex.getMessage());
			} else if (ex.getCode() == 7103) {
				throw new AccountIsLockedException(ex.getMessage());
			} else if (ex.getCode() == 7104) {
				throw new AccountDoesNotExistException(ex.getMessage());
			} else if (ex.getCode() == 400){
				throw new InvalidUsernameOrPasswordException(ex.getMessage());
			} else {
				throw ex;
			}
		}*/
	}
	
	public UserGroup createGroup(UserProfile owner, String groupName) throws AuthenticationException {
		if (owner == null) throw new RuntimeException("Owner must not be null");
		if (groupName.isEmpty()) throw new AuthenticationException("Group name must not be empty");
		
		String name = UserGroup.makeValidGroupName(owner, groupName);
		userManager.createGroup(name, new ArrayList<>()); //todo: what roles here
		userManager.addUserToGroup(owner.getUsername(), name);
		return new UserGroup(name, userManager);
		
		/*Client client = context.getClient();
		Group group = client.instantiate(Group.class);
		String name = UserGroup.makeValidGroupName(owner, groupName);
		group.setName(name);
		
		try {
			context.getApplication().createGroup(group);
		} catch (ResourceException e) {
			if (e.getCode() == 2001) {
				throw new AuthenticationException("Group with that name already exists");
			}
		}
		
		Account account = owner.getAccount();

		group.addAccount(account);

		return new UserGroup(group);*/
	}
	
	public void createAccount(String username, String firstName, String lastName, String email, String password)
			throws EmailAlreadyInUseException, PasswordTooShortException,
			PasswordLacksCapitalLetterOrNumberException, MissingNameException,
			MissingEmailException, InvalidEmailFormatException, AccountDoesNotExistException,
			UsernameAlreadyInUseException {
		
		if (firstName.isEmpty() || lastName.isEmpty()) {
			throw new MissingNameException("The firstName and lastName cannot be null, empty or blank");
		}
		
		UserProfile user = new UserProfile(email, username, password, defaultRoles(), userManager);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		
		/*Client client = context.getClient();
		Account newAccount = client.instantiate(Account.class);
		newAccount.setEmail(email);
		newAccount.setPassword(password);
		newAccount.setGivenName(firstName);
		newAccount.setSurname(lastName);
		newAccount.setUsername(username);
		
		try {
			context.getApplication().createAccount(newAccount);
			assignRoleToAccount(newAccount, Role.REGISTERED_USER);
			
		} catch (ResourceException exp) {
//			TODO: make sure all error codes are covered!
			if (exp.getCode() == 2000) {
				throw new MissingEmailException(exp.getMessage());
			} else if (exp.getCode() == 2001) {
				if (exp.getMessage().contains("username")) {
					throw new UsernameAlreadyInUseException(exp.getMessage());
				} else if (exp.getMessage().contains("email")) {
					throw new EmailAlreadyInUseException(exp.getMessage());
				} else {
					throw exp;
				}
			} else if (exp.getCode() == 2006) {
				throw new InvalidEmailFormatException(exp.getMessage());
			} else if (exp.getCode() == 2007) {
				throw new PasswordTooShortException(exp.getMessage());
			} else if (exp.getCode() == 400) {
				throw new PasswordLacksCapitalLetterOrNumberException(exp.getMessage());
			} else {
				throw exp;
			}
		}*/	
	}

	/*private void assignRoleToAccount(UserProfile user, Role role) {
				
		GroupList groups = context.getApplication().getGroups();
		
		for (Group group : groups) {
			if (group.getName().equals(role.toString())) {
				group.addAccount(account);
			}
		}
	}*/ //todo: delete
	
	public void sendPasswordResetEmail(String email) throws AccountDoesNotExistException,
			InvalidEmailFormatException {
//		TODO: Once Richard has his own StormPath account, he will have to set up the password reset URI and email
//		Take a look at: https://docs.stormpath.com/java/servlet-plugin/forgot-password.html#configure-the-workflow
		try {
			context.getApplication().sendPasswordResetEmail(email);
		} catch (ResourceException e) {
			if (e.getCode() == 2016) {
				throw new AccountDoesNotExistException(e.getMessage());
			} else if (e.getCode() == 2000 || e.getCode() == 2002) {
				throw new InvalidEmailFormatException(e.getMessage());
			} else {
				throw e;
			}
		}
	}
	
	public Account resetPassword(String token, String newPassword) throws InvalidPasswordResetToken, PasswordTooShortException, PasswordLacksCapitalLetterOrNumberException {
		if (token == null || token.isEmpty()) {
			throw new InvalidPasswordResetToken("token cannot be null or empty");
		}
		
		if (newPassword == null || newPassword.isEmpty()) {
			throw new PasswordTooShortException("Password must be 8 characters long");
		}
		
		try {
			return context.getApplication().resetPassword(token, newPassword);
		} catch (ResourceException e) {
			if (e.getCode() == 404) {
				throw new InvalidPasswordResetToken(e.getMessage());
			} else if (e.getCode() == 2007) {
				throw new PasswordTooShortException(e.getMessage());
			} else if (e.getCode() == 400) {
				throw new PasswordLacksCapitalLetterOrNumberException(e.getMessage());
			} else {
				throw e;
			}
		}
	}
}
