/**
 * 系统项目名称
 * cn.jrjzx.supervise.manager.common.bean
 * ApiResult.java
 * 
 * 2017年12月19日-上午11:36:47
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *
 * ApiResult
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年12月19日 上午11:36:47
 * 
 * @version 1.0.0
 *
 */
public class Result<T> {

	private String code;
	private String msg;
	@JsonIgnore
	private StringBuilder tempMsg = new StringBuilder();
	
	private T data;
	
	/**
	 * 创建一个新的实例 ApiResult.
	 *
	 * @param success
	 * @param data2
	 */
	private Result(CodeMsg codeMsg, T data) {
		code(codeMsg);
		this.data = data;
	}
	private Result(CodeMsg codeMsg) {
		code(codeMsg);
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public static <T>  Result<T> success(T data){
		return new Result<T>(CodeMsg.SUCCESS,data);
	}
	public static <T> Result<T> error(T data){
		return new Result<T>(CodeMsg.SYSTEM_ERROR,data);
	}
	
	public static  <T> Result<T> paramError(Object ... msgs){
		Result<T> result = new Result<T>(CodeMsg.PARAM_ERROR);
		return result.fillMsg(msgs);
	}
	
	public Result<T> fillMsg(Object... msgs) {
		for (int i = 0; i < msgs.length; i++) {
			this.tempMsg.append(msgs[i]+";");
		}
		if(msgs.length > 0){
			this.msg = this.tempMsg.substring(0, this.tempMsg.length()-1);
		}
	    return this;
	}
	
	public static  <T> Result<T> error(CodeMsg codeMsg){
		return new Result<T>(codeMsg);
	}
	public void code(CodeMsg code){
		this.code = code.getCode();
		this.msg = code.getMsg();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public StringBuilder getTempMsg() {
		return tempMsg;
	}
	public void setTempMsg(StringBuilder tempMsg) {
		this.tempMsg = tempMsg;
	}
	

	
	
}
