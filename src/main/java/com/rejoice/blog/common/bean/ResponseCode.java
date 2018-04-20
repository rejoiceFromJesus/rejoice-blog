/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.response
 * ResponseStatus.java
 * 
 * 2017年5月21日-下午7:02:33
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.bean;

/**
 *
 * ResponseCode
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年5月21日 下午7:02:33
 * 
 * @version 1.0.0
 *
 */
public enum ResponseCode {
	
	SUCCESS("0","操作成功"),
	SYSTEM_ERROR("1","系统错误"),
	PARAM_ERROR("2","参数错误"),
	BUSINESS_ERROR("3","业务错误");
	
	private final String code;
	private final String message;
	
	ResponseCode(String code, String message){
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
	
	
}
