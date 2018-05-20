package com.rejoice.blog.security.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="blog.security")
public class SecurityProperties {

	private LoginType loginType = LoginType.REDIRECT;

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	
	
}
