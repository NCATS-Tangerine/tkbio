package bio.knowledge.model.user;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Relationship;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

public class Group extends Neo4jAbstractIdentifiedEntity {
		
	private String groupId;
	private String groupName;
	private String fullGroupName; // to ensure unique names per owner
	
	@Relationship(type="OWNER")
	private User owner;
	
	@Relationship(type="MEMBER")
	private Set<User> members = new HashSet<>();
	
	public Group() {}
	
	public Group(String groupName, User owner) {
		this.groupName = groupName;
		this.fullGroupName = owner.getUserId() + "::" + groupName;
		setOwner(owner);
		addMember(owner);
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	private void setOwner(User owner) {
		this.owner = owner;
		this.owner.addGroupOwned(this);
	}
	
	public void addMember(User user) {
		members.add(user);
		user.addGroupBelongedTo(this);
	}

	public Set<User> getMembers() {
		return members;
	}

	public void removeMember(User user) {
		members.remove(user);
	}
	
	public String toString() {
		return groupName;
	}
}
