package com.rejoice.blog.vo.http.oschina;

public class OauthPostInput {

	private String access_token;
	private String dataType = "json";
	private String title;
	private String content;
	private String catalog = "5717683";
	private String classification = "430381";
	private String privacy = "0";
	private String deny_comment = "0";
	private String as_top = "0";
	private String isRecommend = "0";
	private String type ="1";
	private String content_type = "4";
	
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getDeny_comment() {
		return deny_comment;
	}
	public void setDeny_comment(String deny_comment) {
		this.deny_comment = deny_comment;
	}
	public String getAs_top() {
		return as_top;
	}
	public void setAs_top(String as_top) {
		this.as_top = as_top;
	}
	public String getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	
	
}
