package com.rejoice.blog;

import javax.annotation.PostConstruct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
@MapperScan(basePackages="com.rejoice.blog.mapper")
public class RejoiceBlogApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(RejoiceBlogApplication.class)
		.properties(
				"spring.config.name:application,constant,task,enabled,boot-admin,jdbc",
				"spring.config.location:classpath:/,classpath:/config/,file:./config/")
		.build().run(args);
	}
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {
	}
}
