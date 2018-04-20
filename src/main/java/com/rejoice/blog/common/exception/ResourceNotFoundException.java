/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.common.exception
 * ResourceNotFoundException.java
 * 
 * 2017年7月25日-下午4:52:29
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * ResourceNotFoundException
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年7月25日 下午4:52:29
 * 
 * @version 1.0.0
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
}
