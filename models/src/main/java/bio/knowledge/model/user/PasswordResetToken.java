package bio.knowledge.model.user;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

public class PasswordResetToken extends Neo4jAbstractIdentifiedEntity {

	private User user;
	private String token;
	private Long expiryDate;
	
	public PasswordResetToken() {}
	
	public PasswordResetToken(User user) {
		this.user = user;
		this.token = UUID.randomUUID().toString();
		this.expiryDate = tomorrow();
	}
	
	private static long today() {
		return (new Date()).getTime();
	}
	
	private static long tomorrow() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, 1);
		return date.getTimeInMillis();
	}
	
	public String getString() {
		return token;
	}
		
	public boolean isValid() {
		return today() < expiryDate;
	}
	
	public User getUser() {
		return user;
	}
		
}
