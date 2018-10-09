package com.rejoice.blog.vo.http.jianshu;

public class NotesUpdateOutput {
	private String id;
	private String content_updated_at;
	private String content_size_status;
	private String last_compiled_at;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent_updated_at() {
		return content_updated_at;
	}
	public void setContent_updated_at(String content_updated_at) {
		this.content_updated_at = content_updated_at;
	}
	public String getContent_size_status() {
		return content_size_status;
	}
	public void setContent_size_status(String content_size_status) {
		this.content_size_status = content_size_status;
	}
	public String getLast_compiled_at() {
		return last_compiled_at;
	}
	public void setLast_compiled_at(String last_compiled_at) {
		this.last_compiled_at = last_compiled_at;
	}
	
}
