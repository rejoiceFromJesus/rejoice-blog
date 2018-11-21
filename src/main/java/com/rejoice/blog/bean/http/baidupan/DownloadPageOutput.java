package com.rejoice.blog.bean.http.baidupan;

public class DownloadPageOutput {

	private String sign;
	private FileList file_list;
	private String shareid;
	private String uk;
	private String timestamp;
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getUk() {
		return uk;
	}
	public void setUk(String uk) {
		this.uk = uk;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public FileList getFile_list() {
		return file_list;
	}
	public void setFile_list(FileList file_list) {
		this.file_list = file_list;
	}
	public String getShareid() {
		return shareid;
	}
	public void setShareid(String shareid) {
		this.shareid = shareid;
	}
	
	
	
}
