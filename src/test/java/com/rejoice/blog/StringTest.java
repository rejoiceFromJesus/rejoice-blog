/**
 * 系统项目名称
 * com.rejoice.blog
 * StringTest.java
 * 
 * 2017年11月7日-下午3:44:36
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.CharSet;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

/**
 *
 * StringTest
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月7日 下午3:44:36
 * 
 * @version 1.0.0
 *
 */
public class StringTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String copyToString = StreamUtils.copyToString(new FileInputStream(ResourceUtils.getFile("classpath:table-names.txt")), Charsets.UTF_8);
		String[] tableNames = copyToString.split("\r\n");
		Stream.of(tableNames)
		.map((x)->"delete from "+x+" where company_id=1")
		.forEach(System.out::println);
	}
}
