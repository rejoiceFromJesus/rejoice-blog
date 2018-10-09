package com.rejoice.blog.vo.http.jianshu;

public class NotesAddInput {
	
	private String notebook_id;
	private String title;
	private Boolean at_bottom;
	public String getNotebook_id() {
		return notebook_id;
	}
	public void setNotebook_id(String notebook_id) {
		this.notebook_id = notebook_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getAt_bottom() {
		return at_bottom;
	}
	public void setAt_bottom(Boolean at_bottom) {
		this.at_bottom = at_bottom;
	}
	
}
