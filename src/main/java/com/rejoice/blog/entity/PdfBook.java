package com.rejoice.blog.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class PdfBook extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String img;
	private String fileName;
	private String imgUrl;
	private String url;
	private Boolean isPostJianshu;
	private Boolean isPostSystem;
	private Boolean isPostOschina;
	private Boolean isUploadImg;
	
	
	
	
	public Boolean getIsUploadImg() {
		return isUploadImg;
	}
	public void setIsUploadImg(Boolean isUploadImg) {
		this.isUploadImg = isUploadImg;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Boolean getIsPostJianshu() {
		return isPostJianshu;
	}
	public void setIsPostJianshu(Boolean isPostJianshu) {
		this.isPostJianshu = isPostJianshu;
	}
	public Boolean getIsPostSystem() {
		return isPostSystem;
	}
	public void setIsPostSystem(Boolean isPostSystem) {
		this.isPostSystem = isPostSystem;
	}
	public Boolean getIsPostOschina() {
		return isPostOschina;
	}
	public void setIsPostOschina(Boolean isPostOschina) {
		this.isPostOschina = isPostOschina;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
