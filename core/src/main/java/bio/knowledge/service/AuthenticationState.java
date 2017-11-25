package bio.knowledge.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticationState {
	private String userId;
	private String[] groupIds;
	
	public void setState(String userId, String[] groupIds) {
		this.userId = userId;
		this.groupIds = groupIds;
	}
	
	public void setStateNull() {
		this.userId = null;
		this.groupIds = null;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public String[] getGroupIds() {
		return this.groupIds;
	}
}
