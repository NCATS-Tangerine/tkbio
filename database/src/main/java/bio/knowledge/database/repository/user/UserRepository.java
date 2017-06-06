package bio.knowledge.database.repository.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.user.User;

public interface UserRepository extends GraphRepository<User>{

	@Query("CREATE CONSTRAINT ON (user:User) ASSERT user.userId IS UNIQUE")
	void createUniqueConstraintOnUserId();
	
	@Query("CREATE CONSTRAINT ON (user:User) ASSERT user.username IS UNIQUE")
	void createUniqueConstraintOnUsername();

	@Query("CREATE CONSTRAINT ON (user:User) ASSERT user.email IS UNIQUE")
	void createUniqueConstraintOnEmail();

	@Query("MATCH (user:User)"
		+ " WHERE user.userId = {userId}"
		+ " RETURN user")
	User findByUserId(@Param("userId") String userId);
	
	@Query("MATCH (user:User)"
		+ " WHERE user.email = {email}"
		+ " RETURN user")
	User findByEmail(@Param("email") String email);
	
	@Query("MATCH path = (user:User) -[*0..1]- ()"
		+ " WHERE user.username = {login} OR user.email = {login}"
		+ " RETURN user, nodes(path), relationships(path)")
	User findByUsernameOrEmail(@Param("login") String login);

}
