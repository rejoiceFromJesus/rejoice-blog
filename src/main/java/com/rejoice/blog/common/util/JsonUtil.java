package com.rejoice.blog.common.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
* @ClassName: JsonUtil 
* @Description: json工具类
* @author rejoice  948870341@qq.com 
* @date 2016年11月11日 下午12:16:59 
*
 */
@Component
@Lazy(false)
public final class JsonUtil {

	
	@Autowired
	private ObjectMapper mapper;
	
	private static ObjectMapper mapperTemp = null;


	/**
	 * 获取jackson json lib的ObjectMapper对象
	 * 
	 * @return -- ObjectMapper对象
	 */
	public static ObjectMapper getMapper() {
		return mapperTemp;
	}


	/**
	 * 将json转成java bean
	 * 
	 * @param <T>
	 *            -- 多态类型
	 * @param json
	 *            -- json字符串
	 * @param clazz
	 *            -- java bean类型(Class)
	 * @return -- java bean对象
	 */
	public static <T> T toBean(String json, Class<T> clazz) {

		T rtv = null;
		try {
			rtv = mapperTemp.readValue(json, clazz);
		} catch (Exception ex) {
			throw new IllegalArgumentException("json字符串转成java bean异常", ex);
		}
		return rtv;
	}


	/**
	 * 将java bean转成json
	 * 
	 * @param bean
	 *            -- java bean
	 * @return -- json 字符串
	 */
	public static String toJson(Object bean) {

		String rtv = null;
		try {
			rtv = mapperTemp.writeValueAsString(bean);
		} catch (Exception ex) {
			throw new IllegalArgumentException("java bean转成json字符串异常", ex);
		}
		return rtv;
	}
	
	@PostConstruct
	public void init(){
		mapperTemp = mapper;
	}
}