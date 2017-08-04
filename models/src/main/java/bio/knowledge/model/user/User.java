package bio.knowledge.model.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

public class User extends Neo4jAbstractIdentifiedEntity {

	private String userId;
	private String email;
	private String username;
	private String password;
	private Long dateJoined;
	
	private Set<Role> roles = new HashSet<>();
	private Set<Permission> permissions = new HashSet<>();

	@Relationship(type="OWNER", direction="INCOMING")
	private Set<Group> groupsOwned = new HashSet<>();
	
	@Relationship(type="MEMBER", direction="INCOMING")
	private Set<Group> groupsBelongedTo = new HashSet<>();
	
	private String firstName = "";
	private String middleName = "";
	private String lastName = "";
	private String facebookUrl;
	private String linkedInUrl;
	private String twitterUrl;
	
	public User() {}
	
	public User(String email, String username) {
		this.email = email;
		this.username = username;
		this.dateJoined = getVersionDate();
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	@Override
	public String getId() {
		return getUserId();
	}
	
	@Override
	public void setId(String userId) {
		setUserId(userId);
	}
	
	@Transient
	public String getDateJoined() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); //TODO: locale/timezone?
		return dateFormat.format(new Date(dateJoined));
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public boolean hasOneOfRoles(Role[] permitted) {
		return Arrays.stream(permitted)
				.anyMatch(role -> roles.contains(role));
	}
	
	public void setPermission(Permission permission, boolean granted) {
		if (granted) {
			permissions.add(permission);
		} else {
			permissions.remove(permission);
		}
	}
	
	public boolean getPermission(Permission permission) {
		return permissions.contains(permission);
	}
	
	public void addGroupOwned(Group group) {
		groupsOwned.add(group);
	}
	
	public Set<Group> getGroupsOwned() {
		return groupsOwned;
	}
	
	public void addGroupBelongedTo(Group group) {
		groupsBelongedTo.add(group);
	}
	
	public String[] getIdsOfGroupsBelongedTo() {
		return groupsBelongedTo.stream().map(Group::getGroupId).toArray(String[]::new);
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {		
		return lastName;
	}
	
	public String getFullName() {
		String middle = middleName + (middleName.isEmpty()? "" : " ");
		return firstName + " " + middle + lastName;
	}
	
	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}
	
	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}
	
	public String getTwitterUrl() {
		return twitterUrl;
	}
	
	public void setLinkedInUrl(String linkedInUrl) {
		this.linkedInUrl = linkedInUrl;
	}

	public String getLinkedInUrl() {
		return linkedInUrl;
	}
	
	@Override
	public boolean equals(Object b) {
		if (b instanceof User) {
			return ((User) b).getId() == getId();
		}
		return false;
	}
}

