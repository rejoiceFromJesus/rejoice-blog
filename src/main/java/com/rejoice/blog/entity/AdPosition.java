package com.rejoice.blog.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rejoice.blog.common.constant.AdUnionEnum;
import com.rejoice.blog.common.constant.PlatformEnum;

/**
 * 广告位
 * @author jiongyi
 *
 */
@Table
public class AdPosition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private AdUnionEnum adUnion;
	private String html;
	private String position;
	private String styleSheet;
	
	
	
	public String getStyleSheet() {
		return styleSheet;
	}
	public void setStyleSheet(String styleSheet) {
		this.styleSheet = styleSheet;
	}
	public String getAdUnionLabel() {
		return adUnion.label();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public AdUnionEnum getAdUnion() {
		return adUnion;
	}
	public void setAdUnion(AdUnionEnum adUnion) {
		this.adUnion = adUnion;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
}
