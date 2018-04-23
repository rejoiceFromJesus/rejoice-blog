/**
 * 系统项目名称
 * com.rejoice.blog.common.bean
 * LayuiResult.java
 * 
 * 2018年4月23日-上午10:07:12
 *  2018金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.bean;

import java.util.List;

/**
 *
 * LayuiResult
 * 
 * @author rejoice 948870341@qq.com
 * @date 2018年4月23日 上午10:07:12
 * 
 * @version 1.0.0
 *
 */
public class LayuiResult {

	private Integer code = 0;
	private String msg = "操作成功";
	private Long count;
	private List<?> data;
	
	
	/**
	 * 创建一个新的实例 LayuiResult.
	 *
	 * @param count
	 * @param data
	 */
	public LayuiResult(Long count, List<?> data) {
		super();
		this.count = count;
		this.data = data;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	
}
