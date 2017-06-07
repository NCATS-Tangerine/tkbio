package bio.knowledge.database.repository.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import bio.knowledge.model.user.Group;

public interface GroupRepository extends GraphRepository<Group> {
	
	@Query("CREATE CONSTRAINT ON (group:Group) ASSERT group.groupId IS UNIQUE")
	void createUniqueConstraintOnGroupId();	
	
	@Query("CREATE CONSTRAINT ON (group:Group) ASSERT group.fullGroupName IS UNIQUE")
	void createUniqueConstraintOnFullGroupName();
	
}
