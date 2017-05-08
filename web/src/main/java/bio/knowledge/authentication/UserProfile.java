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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.resource.ResourceException;

import bio.knowledge.authentication.exceptions.EmailAlreadyInUseException;
import bio.knowledge.authentication.exceptions.UsernameAlreadyInUseException;

/**
 * 
 * This class wraps the stormpath Account object, and provides an interface
 * between stormpath and our project.
 * 
 * <br><br><b>This class should be immutable.</b>
 * 
 * @author Lance Hannestead
 * @author Richard Bruskiewich
 *
 */
public class UserProfile implements UserDetails {
	
	private static final long serialVersionUID = 7612194811588599365L;

	private UserManager userManager;
	
	private String userID;
	private String email;
	private String username;
	private String password;
	private String givenName;
	private String middleName;
	private String surname;
	private String dateJoined;
	private String facebookUrl;
	private String linkedInUrl;
	private String twitterUrl;
	private boolean nameIsPublic;
	private boolean emailIsPublic;
		
	private Map<Role, Boolean> current_user_group_permission = new HashMap<Role, Boolean>();
	
	public UserProfile(String email, String username, String password,
			Collection<GrantedAuthority> authorities, UserManager userManager) {
		
		this.email = email;
		this.username = username;
		this.password = password;
		for (GrantedAuthority authority : authorities) {
			current_user_group_permission.put(
					Role.lookUpSpringRole(authority.getAuthority()), true);
		}

		this.userManager = userManager;
		userManager.createUser(this);
	}
	
	private void save() {
		System.out.println(userManager);
		userManager.updateUser(this);
	}

	// package private
	void setUserAccessRole(Role role, Boolean hasRole) {
		current_user_group_permission.put(role, hasRole);
	}

	private Boolean userHasAccessRole(Role group) {
		return current_user_group_permission.getOrDefault(group, false);
	}

	public Boolean userHasOneOfAccessRoles(Role[] groups) {
		for (Role group : groups) {
			if (userHasAccessRole(group)) {
				return true;
			}
		}
		return false;
	}
	
	public List<UserGroup> getGroupsOwned() {
		List<UserGroup> groupList = new ArrayList<UserGroup>();
		
		for (String group : userManager.findGroupsForUser(username)) {
			
			String groupUserID = group.split("::")[0];
			if (UserGroup.isValid(group) && userID.equals(groupUserID)) {
				groupList.add(new UserGroup(group, userManager));
			}
		}
		
		return groupList;
	}
	
	public String[] getIdsOfGroupsBelongedTo() {
		List<String> idList = userManager.findGroupsForUser(username);
		String[] idArray = new String[idList.size()];
		
		for (int i = 0; i < idList.size(); i++) {
			idArray[i] = idList.get(i);
		}
		
		return idArray;
	}

	public String getId() {
		return userID;
	}

	public void setID(String userID) {
		this.userID = userID;
	}
	
	public void setEmail(String email) throws EmailAlreadyInUseException {
		String currentEmail = this.email;
		this.email = email;

		try {
			save();
		} catch (ResourceException e) {			
			this.email = currentEmail;
			if (e.getCode() == 409) {
				throw new EmailAlreadyInUseException(e.getMessage());
			} else {
				throw e;
			}
		}
	}

	public void setUsername(String username) throws UsernameAlreadyInUseException {
		String currentUsername = this.username;
		this.username = username;

		try {
			save();
		} catch (ResourceException e) {
			
			this.username = currentUsername;
			if (e.getCode() == 409) {
				throw new UsernameAlreadyInUseException(e.getMessage());
			} else {
				throw e;
			}
		}
	}
	
	public void setPassword(String newPassword) {
		userManager.changePassword(password, newPassword);
		this.password = newPassword;
	}

	public void setFirstName(String firstName) {
		this.givenName = firstName;
		save();
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		save();
	}

	public void setLastName(String lastName) {
		this.surname = lastName;
		save();
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
		save();
	}

	public void setLinkedInUrl(String linkedInUrl) {
		this.linkedInUrl = linkedInUrl;
		save();
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
		save();
	}

	public void setNamePublicized(boolean permitted) {
		this.nameIsPublic = permitted;
		save();
	}
	
	public boolean getNamePublicized() {
		return this.nameIsPublic;
	}
	
	public void setEmailPublicized(boolean permitted) {
		this.emailIsPublic = permitted;
	}

	public boolean getEmailPublicized() {
		return this.emailIsPublic;
	}

	/**
	 * Gets the email of the user corresponding to the account getUserId().
	 * 
	 * @param getUserId()
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Get the date that the user with this getUserId() joined.
	 * 
	 * @param getUserId()
	 * @return
	 */
	public String getDateJoined() {
		return dateJoined;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public String getLinkedInUrl() {
		return linkedInUrl;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	/**
	 * Gets the full name i.e. a single string containing the first name,
	 * followed by the middle name (if any), followed by the last name—of the
	 * user corresponding to the account getUserId().
	 * 
	 * @param getUserId()
	 * @return
	 */
	public String getFullName() {
		if (userID == null)
			return "Guest";
		return givenName + " " + middleName + " " + surname;
	}

	public String getFirstName() {
		if (userID == null)
			return "";
		return givenName;
	}

	public String getMiddleName() {
		if (userID == null)
			return "";
		return middleName;
	}

	public String getLastName() {
		if (userID == null)
			return "";		
		return surname;
	}

	@Override
	public String getUsername() {
		if (userID == null)
			return "";	
		return username;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof UserProfile) {
			UserProfile otherProfile = (UserProfile) other;
			return this.getId().equals(otherProfile.getId());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
