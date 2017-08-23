package bio.knowledge.database.repository.user;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.user.Group;

public interface GroupRepository extends GraphRepository<Group> {
	
	@Query("CREATE CONSTRAINT ON (group:Group) ASSERT group.groupId IS UNIQUE")
	void createUniqueConstraintOnGroupId();	
	
	@Query("CREATE CONSTRAINT ON (group:Group) ASSERT group.fullGroupName IS UNIQUE")
	void createUniqueConstraintOnFullGroupName();
	
	@Query("MATCH path = (owner) <-[:OWNER]- (group:Group) -[:MEMBER*0..1]-> (member)"
		+ " WHERE ID(owner) = {userId}"
		+ " RETURN group, nodes(path), rels(path)")
	Set<Group> findByOwner(@Param("userId") Long userId);
	
}
