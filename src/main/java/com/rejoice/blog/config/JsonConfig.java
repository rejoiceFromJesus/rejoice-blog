/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.config
 * JsonConfig.java
 * 
 * 2017年6月14日-上午9:30:28
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.config;

import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * JsonConfig
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月14日 上午9:30:28
 * 
 * @version 1.0.0
 *
 */
@Configuration
public class JsonConfig {


	@Bean
	public ObjectMapper mapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		//mapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		mapper.setTimeZone(timeZone);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper;
	}
}
