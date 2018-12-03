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
	public static final String DICT_CODE_BATCH_POST_LOCK = "BATCH_POST_LOCK";
	public static final String DICT_CODE_CRAWER_BOOK_END_PAGE = "CRAWER_BOOK_END_PAGE";
	public static final String DICT_CODE_CRAWER_BOOK_START_PAGE = "CRAWER_BOOK_START_PAGE";
	public static final String DICT_CODE_CRAWER_BOOK = "CRAWER_BOOK";
	public static final String DICT_CODE_REPLACE_STRING = "REPLACE_STRING";
	
	
	public static final String DICT_KEY_DEFAULT = "DEFAULT";
	public static final String DICT_KEY_JIANSHU = "JIANSHU";
	public static final String DICT_KEY_OSCHINA = "OSCHINA";
	public static final String DICT_KEY_SYSTEM = "SYSTEM";
	public static final String DICT_KEY_CRAWER_BOOK = "CRAWER_BOOK";
	public static final String DICT_KEY_ALLITEBOOKS = "ALLITEBOOKS";
	public static final String DICT_KEY_EBOOK300 = "EBOOK300";
	public static final String DICT_KEY_FILE_NAME = "FILE_NAME";
	
	/**
	 * api metadata
	 */
	public static final String OSCHINA_RESPONSE_TYPE = "code";
	public static final String OSCHINA_SCOPE = "blog_api,user_api,";
	public static final String OSCHINA_USER_OAUTH_APPROVAL = "true";
	
	/**
	 * flag
	 */
	public static final String TRUE="true";
	public static final String FALSE="false";
	
}
