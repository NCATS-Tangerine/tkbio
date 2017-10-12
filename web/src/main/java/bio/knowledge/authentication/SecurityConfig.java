package bio.knowledge.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import bio.knowledge.service.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.headers()
				.xssProtection().disable()
			.authorizeRequests()
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.permitAll()
				.successHandler(new SimpleUrlAuthenticationSuccessHandler())
				.failureHandler(new SimpleUrlAuthenticationFailureHandler())
				.and()
			.logout()
				.permitAll()
				.logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler())
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
			.csrf().disable(); //Vaadin already handles CSRF protection
	}

	@Autowired
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth
			.userDetailsService(userService).passwordEncoder(encoder);
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}