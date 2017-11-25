package bio.knowledge.service.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.user.UserRepository;
import bio.knowledge.model.user.Role;
import bio.knowledge.model.user.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private User lastLoggedIn;
	
	private boolean isLastLoggedIn(String login) {
		return lastLoggedIn != null && (lastLoggedIn.getEmail().equals(login) || lastLoggedIn.getUsername().equals(login));
	}
	
	public User createUser(String email, String username, String password) {
		User user = new User(email, username);
		save(user);
		user.setUserId(user.getDbId().toString());
		updatePassword(user, password);
		return user;
	}
	
	public void save(User user) {
		repo.save(user);
	}
	
	public void updatePassword(User user, String password) {
		String secret = encoder.encode(password);
		user.setPassword(secret);
		save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
				
		lastLoggedIn = repo.findByUsernameOrEmail(login);
		
		if (lastLoggedIn != null) {
			
			Set<GrantedAuthority> roles = new HashSet<>();
			for (Role role : lastLoggedIn.getRoles()) {
				String name = role.name();
				roles.add(new SimpleGrantedAuthority(name));
			}
			return new org.springframework.security.core.userdetails.User(
					lastLoggedIn.getUsername(), lastLoggedIn.getPassword(), roles);
			
		} else {
			return null;
		}
	}
	
	public User findByUserId(String accountId) {
		return repo.findByUserId(accountId);
	}
	
	public User findByUsernameOrEmail(String login) {
		if (isLastLoggedIn(login)) {
			return repo.findOne(lastLoggedIn.getDbId(), 1);
		}
		User user = repo.findByUsernameOrEmail(login);
		return user;
	}

}
