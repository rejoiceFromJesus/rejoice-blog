package com.rejoice.blog.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.ibatis.annotations.Update;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * BaseEntity
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月9日 下午2:17:40
 * 
 * @version 1.0.0
 *
 */
public class BaseEntity {
	//****************************************query conditions start ******************************************
	@Transient
	private String likes;//like query,comma-delimited format(split with ',') if multiple likes exist
	@Transient
	private String ins;//in query,comma-delimited format(split with ',') if multiple ins exist
	private Date createTime;
	private Date updateTime;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getIns() {
		return ins;
	}
	public void setIns(String ins) {
		this.ins = ins;
	}
	
}
