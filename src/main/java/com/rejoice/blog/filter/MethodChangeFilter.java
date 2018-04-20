package com.rejoice.blog.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import com.rejoice.blog.wrapper.CustomRequestWrapper;

@Order(1)
//重点
@WebFilter(filterName = "methodChange", urlPatterns = "*.html")
public class MethodChangeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
    	HttpServletRequest reqeust = (HttpServletRequest) servletRequest;
    	if(!StringUtils.containsAny(reqeust.getMethod(),new String[]{"get","post","GET","POST"})){
    		CustomRequestWrapper customRequest = new CustomRequestWrapper(reqeust);
    		customRequest.setMethod("POST");
    		servletRequest = customRequest;
    	}
    	filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
/*
作者：大黄蜂coder
链接：http://www.jianshu.com/p/05c8be17c80a
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/