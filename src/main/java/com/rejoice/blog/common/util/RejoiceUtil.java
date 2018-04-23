package com.rejoice.blog.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rejoice.blog.common.exception.InternalServerException;

/**
 * 预订服务工具类
* @author rejoice 
* @date 2016年6月16日 上午9:47:07 
* @version V1.0
 */
public class RejoiceUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RejoiceUtil.class);
	
	public static void emptyToNull(Object obj){
		try {
		Field[] fields = obj.getClass().getDeclaredFields();
	        for (Field field : fields) {
	            // 设置为true，可以获取声明的私有字段的值
	            field.setAccessible(true);
	            if (isBlank(field.get(obj))) {
					field.set(obj, null);
	            }
	        }
		} catch (Exception e) {
			LOGGER.error("set empty property to null failed:",e);
			throw new InternalServerException("emptyToNull failed",e);
		}
	}

	/**
	 * 
	 * formatMoneyToDouble #0.00
	 * 
	 * @param money
	 * @return
	 * double
	 */
	public static double formatMoneyToDouble(Object money){
		return Double.parseDouble(formatMoneyToDoubleString(money));
	}
	/**
	 * 
	 * formatMoneyToDoubleString #0.00
	 * 
	 * @param money
	 * @return
	 * double
	 */
	public static String formatMoneyToDoubleString(Object money){
		DecimalFormat df = new DecimalFormat("#0.00"); 
		return df.format(Double.parseDouble(money.toString()));
	}
	/**
	 * 
	 * formateMoneyToStr  #,###.##
	 * @return
	 * String
	 */
	public static String formateMoneyToStr(Object money){
		NumberFormat nf = new DecimalFormat("#,###.##");
		return nf.format(money);
	}
	
	/**
	 * 
	 * getYearMoneyDays<br/>
	 * 1 year = 12 months = 12*30 days
	 * @param totalDays
	 * @return
	 * Long[]
	 */
	public static Long[] getYearMonthDays(long totalDays){
		  long year = totalDays / 360;
	      long month = totalDays % 360/30;
	      long days =  totalDays % 360 % 30;
	      return new Long[]{year,month,days};
	}
	
	
	
	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name) {
	    StringBuilder result = new StringBuilder();
	    // 快速检查
	    if (name == null || name.isEmpty()) {
	        // 没必要转换
	        return "";
	    } else if (!name.contains("_")) {
	        // 不含下划线，仅将首字母小写
	        return name.substring(0, 1).toLowerCase() + name.substring(1);
	    }
	    // 用下划线将原始字符串分割
	    String camels[] = name.split("_");
	    for (String camel :  camels) {
	        // 跳过原始字符串中开头、结尾的下换线或双重下划线
	        if (camel.isEmpty()) {
	            continue;
	        }
	        // 处理真正的驼峰片段
	        if (result.length() == 0) {
	            // 第一个驼峰片段，全部字母都小写
	            result.append(camel.toLowerCase());
	        } else {
	            // 其他的驼峰片段，首字母大写
	            result.append(camel.substring(0, 1).toUpperCase());
	            result.append(camel.substring(1).toLowerCase());
	        }
	    }
	    return result.toString();
	}
	/**
	 * 获取指定格式的日期的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date getStrDate(String dateStr,String pattern){
		try {
			return new SimpleDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("字符串转换成日期出错："+dateStr+"-->"+pattern+" ，错误："+e.getMessage());
		}
	}
	
	/**
	 * 获取yyyyMMdd
	 * @param date
	 * @return
	 */
	public static Date getStrDate1(String dateStr){
		return getStrDate(dateStr, "yyyyMMdd");
	}
	/**
	 * 获取yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static Date getStrDate2(String dateStr){
		return getStrDate(dateStr, "yyyy-MM-dd");
	}
	/**
	 * 获取yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static Date getStrDate3(String dateStr){
		return getStrDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取指定格式的日期的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateStr(Date date,String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 获取yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String getDateStr1(Date date){
		return getDateStr(date, "yyyyMMdd");
	}
	/**
	 * 获取yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateStr2(Date date){
		return getDateStr(date, "yyyy-MM-dd");
	}
	/**
	 * 获取yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getDateStr3(Date date){
		return getDateStr(date, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * list中是否包含target
	* @param list
	* @param target
	* @return true/false
	* @author rejoice 
	* @date 2016年6月16日 上午9:57:29 
	* @version V1.0
	 * @param <T>
	 */
	public static <T> boolean contains(List<T> list,Object target){
		for(T obj: list){
			if(obj == target){
				return true;
			}
			if(target != null && obj != null){
				if(target.toString().trim().equals(obj.toString().trim())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * String数组中是否包含target
	* @param String数组
	* @param target
	* @return true/false
	* @author rejoice 
	* @date 2016年6月16日 上午9:57:29 
	* @version V1.0
	 * @param <T>
	 */
	public static boolean contains(String[] strs,Object target){
		return RejoiceUtil.contains(Arrays.asList(strs), target);
	}
	
	public static boolean contains(Integer[] ints,Object target){
		return RejoiceUtil.contains(Arrays.asList(ints), target);
	}
	
	/**
	 * 是否为空 
	 * null,""," "  return true
	 * @return
	 */
	public static boolean isBlank(Object object){
		if(null == object){
			return true;
		}
		
		if(object instanceof Collection){
			Collection collection = (Collection) object;
			return collection.isEmpty();
		}
		if(object instanceof Object[]){
			Object[] objs = (Object[]) object;
			return objs.length == 0;
		}
		return "".equals(object.toString().trim());
	}
	/**
	 * 是否不为空 
	 * null,""," " return false
	 * @return
	 */
	public static boolean isNotBlank(Object object){
		return !isBlank(object);
	}
	
	/**
	 * Object -> String<br/>
	 * desc: BigDecimal -> toPlainString()
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		if(null == obj){
			return null;
		}
		if(obj instanceof BigDecimal){
			return ((BigDecimal) obj).toPlainString();
		}
		if(obj instanceof Number){
			String.valueOf(obj);
		}
		return obj.toString();
	}
	/**
	 * String转化为秒级时间，实质上是没有时分秒则补0
	 * @param dateStr
	 * @return
	 */
	public static String toSecondTime(String dateStr) {
		if(StringUtils.isNotBlank(dateStr)){
			if (dateStr.length() <= 16) {
				if(dateStr.length() == 16){
					return dateStr + ":00";
				}else{
					return dateStr + " 00:00:00";
				}
				
			} else {
				return dateStr;
			}
		}else{
			throw new NullPointerException("传入的日期字符串为null");
		}
		
	}
	
	/**
	 * 日期字符串转为int
	 * yyyy-MM-dd -> yyyyMMdd
	 * yyyy-MM-dd HH:mm:dd -- > yyyyMMdd
	 * @param dateStr
	 * @return
	 */
	public static int toIntDate(String dateStr) {
		return dateStr.length() > 10 ? Integer.parseInt(dateStr.substring(0, 10).replace("-", "")) : Integer.parseInt(dateStr.replace("-", ""));
	}
	
	public static Integer toInteger(Object obj){
		if(null == obj || StringUtils.isBlank(obj.toString())){
			return null;
		}else{
			return new BigDecimal(obj.toString().trim()).intValue();
		}
	}
	

	public static Double toDouble(Object obj){
		if(null == obj || StringUtils.isBlank(obj.toString())){
			return null;
		}else{
			return new BigDecimal(obj.toString().trim()).doubleValue();
		}
	}
	
	public static Date toSecondDateTime(String dateStr) throws ParseException{
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		dateStr = toSecondTime(dateStr);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(dateStr);
	}
	
	public static long getTotalPage(long totalCount, int pageSize){
		return (totalCount + pageSize -1) / pageSize;
	}
	
}
