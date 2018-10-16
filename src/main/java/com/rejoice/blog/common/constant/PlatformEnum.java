package com.rejoice.blog.common.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PlatformEnum implements BaseEnum{
    
	OSCHINA("OSCHINA","开源中国"),
	SYSTEM("SYSTEM","系统"),
	CHENG_TONG("CHENG_TONG","城通网盘"),
	JIANSHU("JIANSHU","简书");
	private String value;
	private String label;
	
	
	private PlatformEnum(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	
private static Map<String, PlatformEnum> LOOK_UP = new HashMap<>();
	
	static {
		PlatformEnum[] values = PlatformEnum.values();
		   for (PlatformEnum value : values) {
			   LOOK_UP.put(value.value.toString(), value);
	    }
	}
	
	public static String[] getValues() {
		return Stream.of(PlatformEnum.values()).map(PlatformEnum:: value).collect(Collectors.toList()).toArray(new String[] {});
	}
	
	public String label() {
		return this.label;
	}
	public String value() {
		return this.value;
	}
    
	public static PlatformEnum get(String value) {
		return LOOK_UP.get(value.toString());
	}
	public static String label(String value) {
		if(value == null) {
			return null;
		}
		PlatformEnum platformEnum = get(value);
		return platformEnum == null ? null : platformEnum.label;
	}
	public String getLabel() {
		return label;
	}
	public String getValue() {
		return value;
	}
	
	
}