package com.rejoice.blog.common.bean;


public enum CodeMsg {
	/**
	 * 通用模块
	 */
	SUCCESS("0","操作成功"),
	

	PARAM_ERROR("500101","参数不合法"),
	SIGN_ERROR("500103","sign（签名）错误"),
	TOKEN_ERROR("500104","token错误"),
	NOT_FOUND("500105","资源不存在"),
	REPETITIVE_OPERATION("500106","重复操作，资源已存在"),
	CONTRACT_NOT_EXIST("500107","合同编号不存在"),
	APPROVING("500108","审核中"),
	DISAPPROVED("500109","审核不通过"),
	
	UNAUTHENTICATED("400101","未认证"),
	UNAUTHORIZED("400102","未授权"),
	SYSTEM_ERROR("-1","服务器异常");
	private final String code;
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	
	/**
	 * 创建一个新的实例 CodeMsg.
	 *
	 * @param code
	 * @param msg
	 */
	private CodeMsg(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	

	




	
	
}