package com.rejoice.blog.entity;

import java.util.Date;

import javax.persistence.Column;

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
	

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	
}
