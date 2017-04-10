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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.group.GroupList;
import com.stormpath.sdk.resource.ResourceException;

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
public class UserProfile {
	
	protected final static String FACEBOOK_URL_KEY = "facebookUrl";
	protected final static String LINKEDIN_URL_KEY = "linkedInUrl";
	protected final static String TWITTER_URL_KEY = "twitterUrl";
	
	public final static String NAME_PUBLICIZED_PERMISSION = "nameIsPublic";
	public final static String EMAIL_PUBLICIZED_PERMISSION = "emailIsPublic";
	
	private final Account account;
	
	private Map<Role, Boolean> current_user_group_permission = new HashMap<Role, Boolean>();
	
	protected Account getAccount() {
		return this.account;
	}
	
	public UserProfile(Account account) {
		if (account != null) {
			this.account = account;
		} else {
			throw new RuntimeException("account cannot be null");
		}
	}

	private void save() {
		account.save();
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
		
		for (Group group : account.getGroups()) {
			String href = group.getName().split("::")[0];
			if (UserGroup.isValid(group) && account.getHref().equals(href)) {
				groupList.add(new UserGroup(group));
			}
		}
		
		return groupList;
	}
	
	public String[] getIdsOfGroupsBelongedTo() {
		List<String> idList = new ArrayList<String>();
		GroupList groupList = account.getGroups();
		
		for (Group group : account.getGroups()) {
			if (UserGroup.isValid(group)) {
				idList.add(group.getHref());
			}
		}
		
		String[] idArray = new String[idList.size()];
		
		for (int i = 0; i < idList.size(); i++) {
			idArray[i] = idList.get(i);
		}
		
		return idArray;
	}

	public String getId() {
		return account.getHref();
	}

	public void setEmail(String email) {
		String currentEmail = account.getEmail();
		account.setEmail(email);

		try {
			save();
		} catch (ResourceException e) {
			account.setEmail(currentEmail);
			throw e;
		}
	}

	public void setUsername(String username) {
		String currentUsername = account.getUsername();
		account.setUsername(username);

		try {
			save();
		} catch (ResourceException e) {
			account.setUsername(currentUsername);
			throw e;
		}
	}

	public void setFirstName(String firstName) {
		account.setGivenName(firstName);
		save();
	}

	public void setMiddleName(String middleName) {
		account.setMiddleName(middleName);
		save();
	}

	public void setLastName(String lastName) {
		account.setSurname(lastName);
		save();
	}

	public void setFacebookUrl(String facebookUrl) {
		account.getCustomData().put(FACEBOOK_URL_KEY, facebookUrl);
		save();
	}

	public void setLinkedInUrl(String linkedInUrl) {
		account.getCustomData().put(LINKEDIN_URL_KEY, linkedInUrl);
		save();
	}

	public void setTwitterUrl(String twitterUrl) {
		account.getCustomData().put(TWITTER_URL_KEY, twitterUrl);
		save();
	}

	public void setPermission(String permission, boolean permitted) {
		account.getCustomData().put(permission, permitted);
	}

	public boolean getPermission(String permission) {
		Object o = account.getCustomData().get(permission);

		if (o instanceof Boolean) {
			return (boolean) o;
		} else {
			return false;
		}
	}

	/**
	 * Gets the email of the user corresponding to the account getUserId().
	 * 
	 * @param getUserId()
	 * @return
	 */
	public String getEmail() {
		return account.getEmail();
	}

	/**
	 * Get the date that the user with this getUserId() joined.
	 * 
	 * @param getUserId()
	 * @return
	 */
	public String getDateJoined() {
		return account.getCreatedAt().toString();
	}

	public String getFacebookUrl() {
		return getCustomData(FACEBOOK_URL_KEY);
	}

	public String getLinkedInUrl() {
		return getCustomData(LINKEDIN_URL_KEY);
	}

	public String getTwitterUrl() {
		return getCustomData(TWITTER_URL_KEY);
	}

	private String getCustomData(String key) {
		if (account.getCustomData().containsKey(key)) {
			return (String) account.getCustomData().get(key);
		} else {
			return "";
		}
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
		if (account == null)
			return "Guest";
		return account.getFullName();
	}

	public String getFirstName() {
		if (account == null)
			return "";
		return account.getGivenName();
	}

	public String getMiddleName() {
		if (account == null)
			return "";
		return account.getMiddleName();
	}

	public String getLastName() {
		if (account == null)
			return "";
		return account.getSurname();
	}

	public String getUsername() {
		if (account == null)
			return "";
		return account.getUsername();
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
}
