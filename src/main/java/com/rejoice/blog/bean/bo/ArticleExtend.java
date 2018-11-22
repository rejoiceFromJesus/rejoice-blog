package com.rejoice.blog.bean.bo;

public class ArticleExtend {

	private String jianshuId;
	private Long autosave_control = 1L;
	public Long getAutosave_control() {
		return autosave_control;
	}

	public void setAutosave_control(Long autosave_control) {
		this.autosave_control = autosave_control;
	}

	public String getJianshuId() {
		return jianshuId;
	}

	public void setJianshuId(String jianshuId) {
		this.jianshuId = jianshuId;
	}
	
}
