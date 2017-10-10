package bio.knowledge.service.user;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.user.GroupRepository;
import bio.knowledge.database.repository.user.UserRepository;
import bio.knowledge.model.user.Group;
import bio.knowledge.model.user.User;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public Group createGroup(User owner, String name) {
		Group group = new Group(name, owner);
		groupRepo.save(group);
		group.setGroupId(group.getDbId().toString());
		groupRepo.save(group);
		return group;
	}

	public void addMember(Group group, User member) {
		group.addMember(member);
		groupRepo.save(group);
	}
	
	public void removeMember(Group group, User member) {
		group.removeMember(member);
		groupRepo.save(group);
	}
	
	public void loadGroupMembers(User user) {
		for (Group group : user.getGroupsOwned()) {
			groupRepo.findOne(group.getDbId());
		};
	}

}
