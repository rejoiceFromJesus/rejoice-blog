package com.rejoice.blog.bean.http.jianshu;

public class NotesUpdateInput {
	private String id;
	private Long autosave_control = 1L;
	private String title;
	private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Long getAutosave_control() {
		return autosave_control;
	}
	public void setAutosave_control(Long autosave_control) {
		this.autosave_control = autosave_control;
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
	
}
