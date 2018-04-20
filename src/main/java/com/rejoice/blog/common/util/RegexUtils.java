/**
 * 系统项目名称
 * cn.jrjzx.regulators.receive.util
 * RegexUtils.java
 * 
 * 2017年6月6日-上午11:46:48
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.util;

import java.util.regex.Pattern;

/**
 *
 * RegexUtils
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月6日 上午11:46:48
 * 
 * @version 1.0.0
 *
 */
public class RegexUtils {
	
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_PATTERN_1 = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
	
	/**
	 * yyyy-MM-dd HH:MM:ss
	 */
	public static final String DATE_PATTERN_2 = "("+DATE_PATTERN_1+") (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9]";
	
	
	/**
	 * number gt 0
	 */
	public static final String NUMBER_GT_0 = "^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$";
	/**
	 * 
	 * checkDate1,including format and validity 
	 * yyyy-MM-dd
	 * @param dateStr
	 * @return
	 * boolean
	 */
	public static boolean checkDate1(String dateStr){
		return Pattern.matches(DATE_PATTERN_1,dateStr);
	}
	
	/**
	 * 
	 * checkDate1,including format and validity 
	 * yyyy-MM-dd HH:mm:ss
	 * @param dateStr
	 * @return
	 * boolean
	 */
	public static boolean checkDate2(String dateStr){
		return Pattern.matches(DATE_PATTERN_2,dateStr);
	}
}
