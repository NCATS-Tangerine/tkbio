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

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.resource.ResourceException;

import bio.knowledge.authentication.exceptions.AuthenticationException;

/**
 * This class wraps the Stormpath {@link com.stormpath.sdk.group.Group} class,
 * interfacing between the class and the rest of this project to suit our needs.
 * 
 * @author Lance Hannestad
 *
 */
public class UserGroup {
	private Group group;

	protected Group getGroup() {
		return this.group;
	}

	/**
	 * Can be used by the calling code to ensure that a group is valid before
	 * calling the UserGroup constructor.
	 * 
	 * @param group
	 * @return
	 */
	public static boolean isValid(Group group) {
		return group.getName().matches(".*::.*");
	}

	public static String makeValidGroupName(UserProfile groupOwner, String groupName) {
		return groupOwner.getId() + "::" + groupName;
	}
	
	public boolean contains(UserProfile userProfile) {
		for (String id : userProfile.getIdsOfGroupsBelongedTo()) {
			if (getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A wrapper for {@link com.stormpath.sdk.group.Group}. Group names must
	 * follow a particular convention, specified by the exception thrown in this
	 * method. You can ensure that a group name will be valid by using:<br><br>
	 * {@code UserGroup.makeValidGroupName(UserProfile groupOwner, String groupName);}<br><br>
	 * A group's name specifies the href of the user who owns it.
	 * 
	 * @param group
	 */
	public UserGroup(Group group) {
		if (isValid(group)) {
			this.group = group;
		} else {
			throw new RuntimeException("Invalid name: must take the form \"<creator's account's href>::<name>\"");
		}
	}

	public void addMember(UserProfile user) {
		group.addAccount(user.getAccount());
	}

	public void addMember(String hrefOrEmailOrUsername) throws AuthenticationException {
		if (hrefOrEmailOrUsername == null || hrefOrEmailOrUsername.isEmpty()) {
			throw new AuthenticationException("Username or email is required");
		}
		
		try {
			group.addAccount(hrefOrEmailOrUsername);
//		} catch (IllegalStateException e) {
//			throw new AuthenticationException("There is no account corresponding to: " + hrefOrEmailOrUsername);
		} catch (ResourceException e) {
			if (e.getCode() == 409) {
				throw new AuthenticationException("That account is already in this group.");
			} else {
				throw e;
			}
		} catch (IllegalStateException e) {
			throw new AuthenticationException("No account was found for \"" + hrefOrEmailOrUsername + "\"");
		}
	}

	public boolean removeMember(UserProfile user) {
		try {
			group.removeAccount(user.getAccount());
			return true;
		} catch (IllegalStateException e) {
			return false;
		}
	}

	public String getName() {
		String[] split = group.getName().split("::");
		return split[1];
	}

	public String getId() {
		return group.getHref();
	}

	public List<UserProfile> getMembers() {
		List<UserProfile> userProfiles = new ArrayList<UserProfile>();

		for (Account account : group.getAccounts()) {
			userProfiles.add(new UserProfile(account));
		}

		return userProfiles;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (other instanceof UserGroup) {
			UserGroup otherGroup = (UserGroup) other;
			
			return getId().equals(otherGroup.getId());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}
