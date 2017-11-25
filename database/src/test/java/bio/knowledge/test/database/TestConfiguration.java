/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.test.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.mail.javamail.JavaMailSender ;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Richard
 *
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(
		basePackages={
			"bio.knowledge.database"
		} 
)
@EnableNeo4jRepositories( 
		basePackages = { 
				"bio.knowledge.database.repository" 
		}
)
@EnableTransactionManagement(mode=AdviceMode.PROXY,proxyTargetClass=true)
public class TestConfiguration extends Neo4jConfiguration {

	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
	   config
	       .driverConfiguration()
	       .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
	   return config;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.data.neo4j.config.Neo4jConfiguration#getSessionFactory()
	 */
	@Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory(
        		getConfiguration(),
        		"bio.knowledge.model"
        );
    }
    
    @Override
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }
    
    /*************************************************************************************
     * see http://blog.solidcraft.eu/2011/04/how-to-test-spring-session-scoped-beans.html
     *************************************************************************************/
    //@Bean
    //public SimpleThreadScope simpleThreadScope() {
    //	return new SimpleThreadScope() ;
    //}
    
    //@Autowired
    //private Scope mockSessionScope ;
    
    @Bean
    public CustomScopeConfigurer configureScope() {
    	CustomScopeConfigurer csConfig = new CustomScopeConfigurer() ;
    	Map<String,Object> scopeMap = new HashMap<String,Object>() ;
    	scopeMap.put("session", new SimpleThreadScope() ) ;
    	csConfig.setScopes(scopeMap) ;
    	return csConfig ;
    }
    
    /*
     * Administrative Mail Configuration
     */

    @Value("${spring.mail.host}")
	private String host ;
	
	@Value("${spring.mail.port}")
	private String portStr ;
	
	@Value("${spring.mail.username}")
	private String username ;
	
	@Value("${spring.mail.password}")
	private String password ;
	
	// Not sure why this isn't automatically defined in the kb2 test suite...
    @Bean
    public JavaMailSender javaMailSender() {
    	
    	JavaMailSenderImpl ms = new JavaMailSenderImpl() ;
    	
    	ms.setHost(host);
    	
    	Integer port ;
		try {
			port   = Integer.parseInt(portStr);
		} catch(NumberFormatException nfe) {
			port = 587 ; // sensible default?
		}
    	ms.setPort(port.intValue());
    	ms.setUsername(username);
    	ms.setPassword(password);
    	
    	Properties jmp = new Properties() ;
    	jmp.put("mail.smtp.auth",true) ;
    	jmp.put("mail.smtp.starttls.enable",true) ;
    	ms.setJavaMailProperties(jmp);
    	
    	return ms ;
    }
    
}
