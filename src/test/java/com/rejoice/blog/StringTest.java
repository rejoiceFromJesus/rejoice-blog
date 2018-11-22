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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.Charsets;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.yaml.snakeyaml.util.UriEncoder;

import com.rejoice.blog.common.constant.AdUnionEnum;
import com.rejoice.blog.common.constant.BaseEnum;

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
	
	@Test
	public void testDate() {
		System.err.println(DateTime.now());
		String abc = "Office_2019_All-in-One_For_Dummies_(Office_All-in-one_for_Dummies)[www.rejoiceblog.com].epub 免费下载";
		System.err.println(abc.replaceAll("_", " "));
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String copyToString = StreamUtils.copyToString(new FileInputStream(ResourceUtils.getFile("classpath:table-names.txt")), Charsets.UTF_8);
		String[] tableNames = copyToString.split("\r\n");
		Stream.of(tableNames)
		.map((x)->"delete from "+x+" where company_id=1")
		.forEach(System.out::println);
	}
	
	@Test
	public void testReplace() {
		String str = "abc=${abc}&aa=0&a=${abc}&c=${c}";
		str = str.replaceAll("\\$\\{c\\}", "c");
		System.err.println(str);
		str = "2018-08《写作这门手艺：普林斯顿大学写作课》[seosee.info].pdf";
		System.err.println(str.replace("[seosee.info]", ""));
		String decode = UriEncoder.encode("{\"za4T8MHB/6mhmYgXB7IntyyOUL7Cl++0jv5rFxAIFVji8GDrcf+k8g==");
		System.err.println(decode);
	}
	
	@Test
	public void testPdf() throws Exception {
		String dir = "C:\\Users\\Administrator\\Downloads\\";
		String filename ="[英语词缀与英语派生词].李平武.扫描版[www.rejoiceblog.com].pdf";
		String filePath = dir+"\\"+filename;
		String imgpath = dir+"images\\"+filename+".jpg";
		  Document document = null;
	        try {
	            float rotation = 0f;
	            //缩略图显示倍数，1表示不缩放，0.5表示缩小到50%
	            float zoom = 1f;
	            
	            document = new Document();
	            document.setFile(filePath);
	             // maxPages = document.getPageTree().getNumberOfPages();
	            
	            BufferedImage image = (BufferedImage)document.getPageImage(0, GraphicsRenderingHints.SCREEN, 
	                        Page.BOUNDARY_CROPBOX, rotation, zoom);
	            
	            Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
	            ImageWriter writer = (ImageWriter)iter.next();
	            FileOutputStream out = new FileOutputStream(new File(imgpath));
	            ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
	            
	            writer.setOutput(outImage);
	            writer.write(new IIOImage(image, null, null));
	        
	        } catch(Exception e) {
	        System.out.println( "to generate thumbnail of a book fail : "  );
	        System.out.println( e );
	        } 
	}
}
