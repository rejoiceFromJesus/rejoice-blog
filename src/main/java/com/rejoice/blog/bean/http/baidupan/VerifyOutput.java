package com.rejoice.blog.bean.http.baidupan;

public class VerifyOutput {
	
	private String errno;
	private String err_msg;
	private String request_id;
	private String randsk;
	public String getErrno() {
		return errno;
	}
	public void setErrno(String errno) {
		this.errno = errno;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getRandsk() {
		return randsk;
	}
	public void setRandsk(String randsk) {
		this.randsk = randsk;
	}
	
	
}

