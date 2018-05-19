package com.rejoice.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@MapperScan(basePackages="com.rejoice.blog.mapper")
public class RejoiceBlogApplication {

	public static void main(String[] args) {
		System.err.println("2332232323");
		new SpringApplicationBuilder(RejoiceBlogApplication.class)
		.properties(
				"spring.config.name:application,constant,task,enabled,boot-admin,jdbc",
				"spring.config.location:classpath:/,classpath:/config/,file:./config/")
		.build().run(args);
	}
}
