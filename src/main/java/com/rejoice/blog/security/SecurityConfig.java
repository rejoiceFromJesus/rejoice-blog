package com.rejoice.blog.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rejoice.blog.security.support.MyAuthenticationFailureHandler;
import com.rejoice.blog.security.support.MyAuthenticationSuccessHandler;
import com.rejoice.blog.security.support.SecurityProperties;
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(value=SecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MyAppUserDetailsService myAppUserDetailsService;
	@Autowired
	MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	MyAuthenticationFailureHandler myAuthenticationFailureHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		    .authorizeRequests()
		    .anyRequest().authenticated()
		    .and().logout().permitAll()
		    .and().formLogin()
		    .loginPage("/authenticate")
		    .loginProcessingUrl("/login")
		    .successHandler(myAuthenticationSuccessHandler)
		    .failureHandler(myAuthenticationFailureHandler);
	} 
        @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder());
	}
        
        
        @Override
        public void configure(WebSecurity web) throws Exception {
        	web.ignoring().antMatchers("/article/*.html","/article/count","/category/parent-id/*","/page/*/","/authenticate","/page/admin/admin-login.html","/","/js/**","/css/**","/img/**","/plugin/**/*");
        }
        
       
        @Bean
        public PasswordEncoder passwordEncoder() {
        	return new BCryptPasswordEncoder();
        }
        
      
}