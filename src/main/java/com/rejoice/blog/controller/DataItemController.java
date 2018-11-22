package com.rejoice.blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.common.bean.Result;
import com.rejoice.blog.common.constant.BaseEnum;

@RequestMapping("/data-item")
@RestController
public class DataItemController {
	
	public static final String ENUM_PACKAGE;
	static {
		String baseEnumName = BaseEnum.class.getName();
		ENUM_PACKAGE = baseEnumName.substring(0, baseEnumName.lastIndexOf(".")+1);
	}

	@GetMapping("/enum/{enumClassSimpleName}")
	public Result<List<Map<String,Object>>> enumList(@PathVariable String enumClassSimpleName) throws Exception{
		Class<Enum> enumClass = (Class<Enum>) Class.forName(ENUM_PACKAGE+enumClassSimpleName);
		List<BaseEnum> enumList = EnumUtils.getEnumList( enumClass);
		List<Map<String, Object>> dataList = enumList.stream().map(item -> {
			Map<String, Object> map = new HashMap<>();
			map.put("value", item);
			map.put("label", item.label());
			return map;
		}).collect(Collectors.toList());
		return Result.success(dataList);
	}
}
