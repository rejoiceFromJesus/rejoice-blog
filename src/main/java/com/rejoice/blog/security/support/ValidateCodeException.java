package com.rejoice.blog.security.support;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException{

	public ValidateCodeException(String msg) {
		super(msg);
	}


}
