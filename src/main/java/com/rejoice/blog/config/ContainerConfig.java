package com.rejoice.blog.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ContainerConfig implements EmbeddedServletContainerCustomizer {

  @Override 
  public void customize(ConfigurableEmbeddedServletContainer container) {
    container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/page/error/error.html"));
    container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/page/error/error.html"));
    container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/page/error/error.html"));
    container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/page/error/error.html"));
    container.addErrorPages(new ErrorPage(HttpStatus.NOT_ACCEPTABLE, "/page/error/error.html"));
    container.addErrorPages(new ErrorPage(HttpStatus.REQUEST_TIMEOUT, "/page/error/error.html"));
  }

}