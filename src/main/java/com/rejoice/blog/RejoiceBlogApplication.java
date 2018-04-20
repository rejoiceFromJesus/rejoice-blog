package com.rejoice.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.rejoice.blog.mapper")
public class RejoiceBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(RejoiceBlogApplication.class, args);
	}
}
