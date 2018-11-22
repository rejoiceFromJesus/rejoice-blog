package com.rejoice.blog.common.constant;

import java.util.HashMap;
import java.util.Map;

public enum AdUnionEnum implements BaseEnum{
    
	JD("京东"),
	TAOBAO("淘宝");
	private String label;
	
	
	
private AdUnionEnum(String label) {
		this.label = label;
	}
private static Map<String, AdUnionEnum> LOOK_UP = new HashMap<>();
	
	static {
		AdUnionEnum[] values = AdUnionEnum.values();
		   for (AdUnionEnum value : values) {
			   LOOK_UP.put(value.toString(), value);
	    }
	}
	
	
	public String label() {
		return this.label;
	}
    
	public static AdUnionEnum get(String value) {
		return LOOK_UP.get(value.toString());
	}
	public static String label(String value) {
		if(value == null) {
			return null;
		}
		AdUnionEnum platformEnum = get(value);
		return platformEnum == null ? null : platformEnum.label;
	}
	public String getLabel() {
		return label;
	}
}