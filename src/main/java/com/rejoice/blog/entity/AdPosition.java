package com.rejoice.blog.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rejoice.blog.common.constant.PlatformEnum;

@Table
public class AdPosition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private PlatformEnum platform;
	private String html;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PlatformEnum getPlatform() {
		return platform;
	}
	public void setPlatform(PlatformEnum platform) {
		this.platform = platform;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
