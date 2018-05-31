package com.rejoice.blog.security.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rejoice.blog.common.constant.Constant;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
	
	@Autowired
	AuthenticationFailureHandler authenticationFailureHandler;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			if(StringUtils.equals("/login",request.getRequestURI()) && StringUtils.equalsAnyIgnoreCase("post", request.getMethod())) {
				validate(request);
			}
		
		} catch (ValidateCodeException e) {
			authenticationFailureHandler.onAuthenticationFailure(request, response, e);
			return;
		}
		doFilter(request, response, filterChain);
		
		
		
	}


	private void validate(HttpServletRequest request) throws ValidateCodeException, ServletRequestBindingException{
		String validateCode = ServletRequestUtils.getStringParameter(request, "validateCode");
		if(StringUtils.isBlank(validateCode)) {
			throw new ValidateCodeException("验证码不能为空");
		}
		String codeInSesison  = (String) request.getSession().getAttribute(Constant.SESSION_VALIDATE_CODE);
		System.err.println(codeInSesison);
		if(StringUtils.isBlank(codeInSesison)) {
			throw new ValidateCodeException("验证码已过期");
		}
		if(!StringUtils.equals(validateCode, codeInSesison)) {
			throw new ValidateCodeException("验证码不正确");
		}
		request.getSession().removeAttribute(Constant.SESSION_VALIDATE_CODE);
		
	}

}
