package com.rejoice.blog.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.rejoice.blog.common.constant.WebSiteEnum;

public class CrawerProgress extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date progressTime;
	private String progressId;
	private WebSiteEnum webSite;
	private Integer count;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getProgressTime() {
		return progressTime;
	}
	public void setProgressTime(Date progressTime) {
		this.progressTime = progressTime;
	}
	public String getProgressId() {
		return progressId;
	}
	public void setProgressId(String progressId) {
		this.progressId = progressId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public WebSiteEnum getWebSite() {
		return webSite;
	}
	public void setWebSite(WebSiteEnum webSite) {
		this.webSite = webSite;
	}
	
	
}
