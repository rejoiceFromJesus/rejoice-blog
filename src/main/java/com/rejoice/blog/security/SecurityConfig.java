package com.rejoice.blog.security;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MyAppUserDetailsService myAppUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		    .authorizeRequests()
		    .anyRequest().authenticated()
		    .and().logout().permitAll()
		    .and().formLogin()
		    .loginPage("/page/admin/admin-login.html")
		    .loginProcessingUrl("/login");
		   // .and().httpBasic().realmName("MY APP REALM")
		    //.authenticationEntryPoint(appAuthenticationEntryPoint);
	} 
        @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder());
	}
        
        
        @Override
        public void configure(WebSecurity web) throws Exception {
        	web.ignoring().antMatchers("/","/page/*/","/page/admin/admin-login.html","/","/js/**","/css/**","/img/**","/plugin/**");
        	//web.ignoring().antMatchers("/**/*.html","/","/js/**","/css/**","/img/**","/plugin/**");
        }
        
       
        @Bean
        public PasswordEncoder passwordEncoder() {
        	return new BCryptPasswordEncoder();
        }
        
      
}