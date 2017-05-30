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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.language.bm.Rule.RPattern;
import org.neo4j.ogm.exception.CypherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.google.gwt.dev.shell.Messages;
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
import bio.knowledge.database.repository.user.UserRepository;
import bio.knowledge.model.user.Group;
import bio.knowledge.model.user.PasswordResetToken;
import bio.knowledge.model.user.Role;
import bio.knowledge.model.user.User;
import bio.knowledge.service.EmailConfiguration;
import bio.knowledge.service.user.GroupService;
import bio.knowledge.service.user.PasswordTokenService;
import bio.knowledge.service.user.UserService;
import bio.knowledge.service.Cache;
import bio.knowledge.service.KBQuery;
import bio.knowledge.web.ui.DesktopUI;
import bio.knowledge.web.view.LandingPageView;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticationManager {

	// I need a spring component for this.
	@Value("${application.rooturl:}")
	private String ROOT_URL;
		
	public String getRootURL() {
		return this.ROOT_URL;
	}
	
	@Autowired
	private AuthenticationContext context;

	@Autowired
	private MailProperties mailProps;
	
	@Autowired
	private org.springframework.security.authentication.AuthenticationManager manager;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private PasswordTokenService tokenService;
		
	private final List<AuthenticationListener> authListeners = new ArrayList<AuthenticationListener>();
	
	private User currentUser;
	
	public User getCurrentUser() {
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
	
	private void notifyOfLogin(User user) {
		for (AuthenticationListener listener : this.authListeners) {
			listener.onLogin(user);
		}
	}
	
	private void notifyOfLogout() {
		for (AuthenticationListener listener : this.authListeners) {
			listener.onLogout();
		}
	}

	
	public void logout() {
		//TODO: Delete any cookies that have been put on the users computer for persistent authentication
		
        this.currentUser = null;
		notifyOfLogout();
		
		DesktopUI ui = (DesktopUI) UI.getCurrent();
		ui.clearSession();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
           new SecurityContextLogoutHandler().logout(request, null, auth);
        }
        SecurityContextHolder.clearContext();
        
		ui.getPage().setLocation(LandingPageView.NAME);
	}
	
	public void login(String usernameOrEmail, String password) throws InvalidUsernameOrPasswordException {//, AccountDisabledException, AccountNotVerifiedException, AccountIsLockedException, AccountDoesNotExistException {
				
		boolean invalidUsername = usernameOrEmail == null || usernameOrEmail.isEmpty();
		boolean invalidPassword = password == null || password.isEmpty();
		
		if (invalidUsername || invalidPassword) {
			throw new InvalidUsernameOrPasswordException("Username and password strings cannot be empty or null");
		}
		
		Authentication token = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
		Authentication auth;
		
		try {
			
			auth = manager.authenticate(token);
			notifyOfLogin(currentUser);
			SecurityContextHolder.getContext().setAuthentication(auth);
			currentUser = userService.findByUsernameOrEmail(usernameOrEmail);
			
			// because some data are visible or not visible depending on the user, 
			// we need to clear out the cache which may have been calibrated to data for a different user or no user.
			//cache.resetCache();
			//query.currentUserId(currentUser.getId());

		} catch(org.springframework.security.core.AuthenticationException e) {
			throw new InvalidUsernameOrPasswordException("Invalid username or password");
		}
	}
	
	public Group createGroup(User owner, String groupName) throws AuthenticationException {
		if (owner == null) throw new RuntimeException("Owner must not be null");
		if (groupName.isEmpty()) throw new AuthenticationException("Group name must not be empty");
	
		try {
			return groupService.createGroup(owner, groupName);
		} catch (CypherException e) {
			throw new AuthenticationException("Group name already in use");
		}
		
	}
	
	public void addMember(Group group, String usernameOrEmail) throws AccountDoesNotExistException {
		User user = userService.findByUsernameOrEmail(usernameOrEmail);
		if (user == null) throw new AccountDoesNotExistException("No such user can be found");
		groupService.addMember(group, user);
	}
	
	public void removeMember(Group group, User user) {
		groupService.removeMember(group, user);
	}
	
	public void createAccount(String username, String firstName, String lastName, String email, String password)
			throws EmailAlreadyInUseException, UsernameAlreadyInUseException, MissingNameException {
			//, PasswordTooShortException, PasswordLacksCapitalLetterOrNumberException, MissingEmailException, InvalidEmailFormatException, AccountDoesNotExistException,
		
		if (firstName.isEmpty() || lastName.isEmpty()) {
			throw new MissingNameException("The firstName and lastName cannot be null, empty or blank");
		}
		
		try {
			User user = userService.createUser(email, username, password);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.addRole(Role.CLIENT);
			userService.save(user);
		} catch (CypherException e) {
			if( userService.findByUsernameOrEmail(email) != null ) {
				throw new EmailAlreadyInUseException("Email is already in use.");
			} else {
				throw new UsernameAlreadyInUseException("Username is already in use.");				
			}			
		}
	}
	
	public User getUser(String accountId) {
		return userService.findByAccountId(accountId);
	}
	
	public void updateEmail(User user, String email) throws EmailAlreadyInUseException {
		
		String previous = user.getEmail();
		user.setEmail(email);
		
		try {
			userService.save(user);
		} catch (CypherException e) {
			user.setEmail(previous);
			throw new EmailAlreadyInUseException("Email is already in use.");
		}
	}
	
	public void updateUsername(User user, String username) throws UsernameAlreadyInUseException {
		
		String previous = user.getUsername();
		user.setUsername(username);
		
		try {
			userService.save(user);
		} catch (CypherException e) {
			user.setUsername(previous);
			throw new UsernameAlreadyInUseException("Username is already in use.");
		}
	}
	
	public void updateDetails(User user) {
		userService.save(user);
	}
	
	private String getBaseURL() {
		
		int port = request.getServerPort();
		if (request.getScheme().equals("http") && port == 80) {
		    port = -1;
		} else if (request.getScheme().equals("https") && port == 443) {
		    port = -1;
		}
		
		try {
			URL baseURL = new URL(request.getScheme(), request.getServerName(), port, "");
			return baseURL.toString();
		} catch (MalformedURLException e) {
			return "";
		}
	}
	
	public void sendPasswordResetEmail(String emailAddress)
			throws AccountDoesNotExistException, InvalidEmailFormatException {
				
		PasswordResetToken token = tokenService.generateToken(emailAddress);
		if (token == null) {
			throw new AccountDoesNotExistException("Account does not exist");
		}
		
		String name = token.getUser().getFullName();
		String href = getBaseURL() + "/#!passwordReset";
		
		Properties props = new Properties();
		props.putAll(mailProps.getProperties());
		Session session = Session.getInstance(props);
	
		String username = mailProps.getUsername();
		String password = mailProps.getPassword();
		
		try {
		
			MimeMessage email = new MimeMessage(session);
			MimeMessageHelper helper = new MimeMessageHelper(email);
			helper.setSubject("Reset your password");
			helper.setTo(emailAddress);
			helper.setText(
					"Hello " + name + "," + "<br><br>" +
					"Forgot your password? We've received a request to reset the password for this email address." + "<br><br>" + 
					"To reset your password please click on this link and enter the password reset token (token expires in 24 hours):" + "<br><br>" +
					"Link:  " + "<a href=\"" + href + "\">" + href + "</a>" + "<br><br>" +
					"Token: " + token.getString() + "<br><br>" +
					"This link takes you to a secure page where you can change your password." + "<br>" +
					"If you don't want to reset your password, please ignore this message. Your password will not be reset.",
				true);
		
			Transport.send(email, username, password);

		} catch (MessagingException e) {
			throw new InvalidEmailFormatException("Problem sending an email to this address");
		}
		
	}
	
	public void resetPassword(String tokenString, String newPassword) throws InvalidPasswordResetToken, PasswordTooShortException {//, PasswordLacksCapitalLetterOrNumberException {
		
		if (tokenString == null || tokenString.isEmpty()) {
			throw new InvalidPasswordResetToken("Token cannot be null or empty");
		}
		
		if (newPassword == null || newPassword.isEmpty()) {
			throw new PasswordTooShortException("Password must be 8 characters long");
		}
		
		PasswordResetToken token = tokenService.findByTokenString(tokenString);
		if (token == null) throw new InvalidPasswordResetToken("Token must be a valid password reset token");
		userService.updatePassword(token.getUser(), newPassword);
		tokenService.delete(token);
	}
}
