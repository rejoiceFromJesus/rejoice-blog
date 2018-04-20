/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.response
 * ResponseVo.java
 * 
 * 2017年5月21日-下午7:21:18
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.bean;

/**
 *
 * ResponseVo
 * common return bean of controller
 * @author rejoice 948870341@qq.com
 * @date 2017年5月21日 下午7:21:18
 * 
 * @version 1.0.0
 *
 */
public class ResponseVo {
	
	private String code;
	private String message;
	private Object data;
	
	/**
	 * 创建一个新的实例 ResponseVo.
	 *
	 * @param success
	 * @param data2
	 */
	public ResponseVo(ResponseCode success, Object data) {
		code(success);
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public static ResponseVo success(Object data){
		return new ResponseVo(ResponseCode.SUCCESS,data);
	}
	
	public static ResponseVo systemError(Object data){
		return new ResponseVo(ResponseCode.SYSTEM_ERROR,data);
	}
	
	public static ResponseVo paramError(Object data){
		return new ResponseVo(ResponseCode.PARAM_ERROR,data);
	}
	public static ResponseVo businessError(Object data){
		return new ResponseVo(ResponseCode.BUSINESS_ERROR,data);
	}
	
	public void code(ResponseCode code){
		this.code = code.code();
		this.message = code.message();
	}

}
