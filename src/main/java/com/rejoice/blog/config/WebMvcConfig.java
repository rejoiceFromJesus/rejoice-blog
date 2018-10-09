package com.rejoice.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.viewresolver.ExcelViewResolver;
import com.rejoice.blog.viewresolver.PdfViewResolver;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
  @Autowired 
  HandlerInterceptor sessionIntercepTor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
	   registry.addInterceptor(sessionIntercepTor).excludePathPatterns(Constant.PASS_PATHS).addPathPatterns("/admin/*");
  }
  
  
@Override
public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .favorPathExtension(true);
}


@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/upload-images/**").addResourceLocations("file:/app/rejoice-blog/upload-images/");
	}

/*
 * Configure View resolver to provide XLS output using Apache POI library to
 * generate XLS output for an object content
 */
@Bean
public ViewResolver excelViewResolver() {
    return new ExcelViewResolver();
}
@Bean
public ViewResolver pdfViewResolver() {
    return new PdfViewResolver();
}



}