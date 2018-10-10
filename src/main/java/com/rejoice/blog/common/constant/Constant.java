/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.util
 * Constant.java
 * 
 * 2017年5月11日-上午9:10:39
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.common.constant;


/**
 *
 * Constant
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年5月11日 上午9:10:39
 * 
 * @version 1.0.0
 *
 */
public abstract class Constant {
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_PATTERN1 = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_PATTERN2 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMdd
	 */
	public static final String DATE_FORMAT_PATTERN3 = "yyyyMMdd";
	/**
	 * yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static final String DATE_FORMAT_PATTERN4 = "yyyy-MM-dd HH:mm:ss:SSS";
	/**
	 * yyyyMM
	 */
	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

	public static final String PREFIX="cn.jrjzx.supervision.smallloan";
	public static final String SUCCESS="success";
	public static final String SESSION_KEY = "loginUser";
	public static final String SESSION_VALIDATE_CODE = "validateCode";
	//no auth paths
	public static final String[] PASS_PATHS = new String[]{"/page/login.html","/page/error/*","/logout","/user/login","/company/active","/error","/checkCode/*","/checkCode"};

	/**
	 * distionary key code
	 */
	public static final String CODE_BATCH_POST_LOCK = "BATCH_POST_LOCK";
	public static final String DICT_KEY_DEFAULT = "DEFAULT";
	public static final String DICT_KEY_JIANSHU = "JIANSHU";
	public static final String DICT_KEY_OSCHINA = "OSCHINA";
	public static final String DICT_KEY_SYSTEM = "SYSTEM";
	
	/**
	 * flag
	 */
	public static final String TRUE="true";
	public static final String FALSE="false";
	
}
