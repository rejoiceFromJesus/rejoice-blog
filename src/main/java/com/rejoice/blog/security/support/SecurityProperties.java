package com.rejoice.blog.security.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="blog.security")
public class SecurityProperties {

	private LoginType loginType = LoginType.REDIRECT;
	
	private Integer rememberMeSeconds;
	


	public Integer getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(Integer rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	
	
}
