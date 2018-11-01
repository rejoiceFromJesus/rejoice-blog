package com.rejoice.blog.vo.http.ebook300;

public class DownloadUrlInput {
	
	private String op = "download2";
	private String id;
	private String rand;
	private String down_script = "1";
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRand() {
		return rand;
	}
	public void setRand(String rand) {
		this.rand = rand;
	}
	public String getDown_script() {
		return down_script;
	}
	public void setDown_script(String down_script) {
		this.down_script = down_script;
	}
	
	
}
