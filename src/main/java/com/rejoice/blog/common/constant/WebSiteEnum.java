package com.rejoice.blog.common.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WebSiteEnum implements BaseEnum{
    
	ALLITEBBOKS("ALLITEBBOKS","http://www.allitebooks.com/"),
	EBOOK3000("EBOOK3000","http://www.ebook3000.com"),
	CTFILE("CTFILE","https://home.ctfile.com/");
	private String value;
	private String label;
	
	
	private WebSiteEnum(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	
private static Map<String, WebSiteEnum> LOOK_UP = new HashMap<>();
	
	static {
		WebSiteEnum[] values = WebSiteEnum.values();
		   for (WebSiteEnum value : values) {
			   LOOK_UP.put(value.value.toString(), value);
	    }
	}
	
	public static String[] getValues() {
		return Stream.of(WebSiteEnum.values()).map(WebSiteEnum:: value).collect(Collectors.toList()).toArray(new String[] {});
	}
	
	public String label() {
		return this.label;
	}
	public String value() {
		return this.value;
	}
    
	public static WebSiteEnum get(String value) {
		return LOOK_UP.get(value.toString());
	}
	public static String label(String value) {
		if(value == null) {
			return null;
		}
		WebSiteEnum platformEnum = get(value);
		return platformEnum == null ? null : platformEnum.label;
	}
	public String getLabel() {
		return label;
	}
	public String getValue() {
		return value;
	}
	
	
}