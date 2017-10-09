package bio.knowledge.service.user;

import java.util.Calendar;

import org.neo4j.ogm.exception.CypherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.user.PasswordTokenRepository;
import bio.knowledge.database.repository.user.UserRepository;
import bio.knowledge.model.user.PasswordResetToken;
import bio.knowledge.model.user.User;

@Service
public class PasswordTokenService {
	
	@Autowired
	PasswordTokenRepository tokenRepo;
	
	@Autowired
	UserRepository userRepo;
	
	public PasswordResetToken generateToken(String email) {
		
		Iterable<PasswordResetToken> expired = tokenRepo.findExpiredBefore(Calendar.getInstance().getTimeInMillis());
		tokenRepo.delete(expired);
		
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return null;
		}
		
		PasswordResetToken token = null;
		while (token == null) {
			try {
				token = new PasswordResetToken(user);
			} catch (CypherException e) {
				// token already in use
			}
		}
		
		tokenRepo.save(token);
		return token;
	}
	
	public PasswordResetToken findByTokenString(String tokenString) {
		
		PasswordResetToken token = tokenRepo.findByTokenString(tokenString);
		
		if (token == null || token.isValid()) {
			return token;
		} else {
			tokenRepo.delete(token);
			return null;
		}
	}
	
	public void delete(PasswordResetToken token) {
		tokenRepo.delete(token);
	}

}
