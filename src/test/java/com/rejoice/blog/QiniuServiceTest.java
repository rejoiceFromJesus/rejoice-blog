package com.rejoice.blog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.rejoice.blog.service.QiniuService;

public class QiniuServiceTest {
	public static void main(String[] args) throws FileNotFoundException {

		FileInputStream inputStream = new FileInputStream("C:\\Users\\jiongyi\\Desktop\\image\\QQ截图20180203223842.jpg");
		QiniuService qiniuService = new QiniuService();
		Map upload = qiniuService.upload(inputStream, "Q截图20180203223842.jpg");
		System.err.println(upload);
	}
}
