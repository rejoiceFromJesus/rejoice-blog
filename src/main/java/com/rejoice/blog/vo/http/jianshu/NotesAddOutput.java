package com.rejoice.blog.vo.http.jianshu;

import java.util.Date;

public class NotesAddOutput {
	   private String id;
	   private String title;
	   private String slug;
	   private String shared;
	   private String notebook_id;
	   private String seq_in_nb;
	   private String note_type;
	   private String autosave_control;
	   private Date content_updated_at;
	   private String last_compiled_at;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getShared() {
		return shared;
	}
	public void setShared(String shared) {
		this.shared = shared;
	}
	public String getNotebook_id() {
		return notebook_id;
	}
	public void setNotebook_id(String notebook_id) {
		this.notebook_id = notebook_id;
	}
	public String getSeq_in_nb() {
		return seq_in_nb;
	}
	public void setSeq_in_nb(String seq_in_nb) {
		this.seq_in_nb = seq_in_nb;
	}
	public String getNote_type() {
		return note_type;
	}
	public void setNote_type(String note_type) {
		this.note_type = note_type;
	}
	public String getAutosave_control() {
		return autosave_control;
	}
	public void setAutosave_control(String autosave_control) {
		this.autosave_control = autosave_control;
	}
	public Date getContent_updated_at() {
		return content_updated_at;
	}
	public void setContent_updated_at(Date content_updated_at) {
		this.content_updated_at = content_updated_at;
	}
	public String getLast_compiled_at() {
		return last_compiled_at;
	}
	public void setLast_compiled_at(String last_compiled_at) {
		this.last_compiled_at = last_compiled_at;
	}
	   
	   
}
