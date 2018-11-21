package com.rejoice.blog.bean.http.oschina;

import com.rejoice.blog.common.constant.Constant;

public class AuthorizedCodeInput {

	private String client_id;
	private String response_type = Constant.OSCHINA_RESPONSE_TYPE;
	private String scope = Constant.OSCHINA_SCOPE;
	private String user_oauth_approval = Constant.OSCHINA_USER_OAUTH_APPROVAL;
	private String email;
	private String pwd;
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getResponse_type() {
		return response_type;
	}
	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUser_oauth_approval() {
		return user_oauth_approval;
	}
	public void setUser_oauth_approval(String user_oauth_approval) {
		this.user_oauth_approval = user_oauth_approval;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
}
