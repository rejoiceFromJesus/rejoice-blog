package com.rejoice.blog.security.support;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.rejoice.blog.common.util.JsonUtil;

@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	SecurityProperties securityProperties;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if(LoginType.JSON.equals(securityProperties.getLoginType())) {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			PrintWriter writer = response.getWriter();
			writer.write(JsonUtil.toJson(authentication));
			return;
		}
		 String redirectUrl = null;
		/*SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        redirectUrl = (savedRequest == null) ? "" : savedRequest.getRedirectUrl();
        System.err.println(redirectUrl);
        if (!redirectUrl.isEmpty()) {
            response.sendRedirect(redirectUrl);
            return;
        }*/
		// 获取登录用户信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority(); // 用户的权限
            // 不同的用户访问不同的页面
            if ("ROLE_ADMIN".equals(role)) {
                redirectUrl = "/page/admin/index.html";
            } else {
                redirectUrl = "/";
            }
        }
        response.sendRedirect(redirectUrl);
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
