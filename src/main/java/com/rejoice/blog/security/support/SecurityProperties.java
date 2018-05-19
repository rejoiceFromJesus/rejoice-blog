package com.rejoice.blog.security.support;

public class SecurityProperties {

	private LoginType loginType = LoginType.REDIRECT;

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	
	
}
