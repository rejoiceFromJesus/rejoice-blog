/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.interceptor
 * SessionInterceptor.java
 * 
 * 2017年6月10日-下午5:05:58
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.rejoice.blog.common.constant.Constant;

/**
 *
 * SessionInterceptor
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月10日 下午5:05:58
 * 
 * @version 1.0.0
 *
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

	private static Logger LOGGER = LoggerFactory
			.getLogger(SessionInterceptor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
			if(request.getSession().getAttribute(Constant.SESSION_KEY) == null) {
			LOGGER.warn("visit path[" + request.getRequestURI()
					+ "]:no session, redirect to /login");
			/*response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			  response.setHeader("Location",request.getContextPath() + "/page/login.html");*/
			//change requeat method to get
			response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
			response.sendRedirect(request.getContextPath() + "/page/login.html");
			return false;
		}
	
		// 测试用模拟用户session
		/*	User user = new User();
			user.setUsername("admin");
			user.setId(1);
			user.setCompanyId(1);
			request.getSession().setAttribute(Constant.SESSION_KEY, user);
		response.setHeader("Access-Control-Allow-Origin", "*");// 测试用，允许跨域
*/		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
