package com.rejoice.blog.security.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author jiongyi
 *  
 */

import com.rejoice.blog.common.bean.CodeMsg;
import com.rejoice.blog.common.bean.Result;
@RestController
@RequestMapping
public class SecurityController {
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * how to process when needed to login
     * <li>redirect to login.html when redirectUrl(before goto login) ends with .html</li>
     * <li>json otherwise</li>
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/authenticate")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public Result<Void> tologin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null) {
			String redirectUrl = savedRequest.getRedirectUrl();
			if(StringUtils.endsWith(redirectUrl, ".html")){
				//to login.html
				redirectStrategy.sendRedirect(request, response, "/page/admin/admin-login.html");
			}
		}
	
		return Result.error(CodeMsg.UNAUTHENTICATED);
	}

}
