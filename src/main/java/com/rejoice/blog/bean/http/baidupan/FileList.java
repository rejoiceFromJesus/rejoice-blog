package com.rejoice.blog.bean.http.baidupan;

import java.util.List;

public class FileList {

	private Integer errno;
	private List<BaiduFile> list;
	public Integer getErrno() {
		return errno;
	}
	public void setErrno(Integer errno) {
		this.errno = errno;
	}
	public List<BaiduFile> getList() {
		return list;
	}
	public void setList(List<BaiduFile> list) {
		this.list = list;
	}
	
}
