package com.rejoice.blog.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * InvalidParamException
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月16日 下午2:58:13
 * 
 * @version 1.0.0
 *
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException{
	
	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -8694543243602524411L;

	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException() {
		super(InternalServerException.class.getSimpleName());
	}

	
	/**
	 * 创建一个新的实例 InternalServerException.
	 *
	 * @param e
	 */
	public InternalServerException(String message,Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 创建一个新的实例 InternalServerException.
	 *
	 * @param e
	 */
	public InternalServerException(Throwable cause) {
		super(cause);
	}
	
	
}
