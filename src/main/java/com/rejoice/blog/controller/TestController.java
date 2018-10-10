package com.rejoice.blog.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.support.ResourceHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ResourceLoader resourceLoader;
	
	@GetMapping("/upload")
	public void testUpload() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Cookie", " oscid=inZ7aH3QlhfhJlDeONY6h%2BHU7kKPY8Yco88jsmyMEhbbGZpW70RZS6tutkpi3HHv2jryiHsiK12xd%2BXTi9HBN%2FFmzsJ1MTrL%2Bs%2BhFJgUAaV%2BGm3mlNur936Py5cwadKEgyVnyLGl3TWcW4m%2B9R8WLw%3D%3D");
	    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
	    String ckCsrfToken = "";
        map.add("ckCsrfToken", "0T7J2qmkhb71408GRfZbOJHtgTD2IEgPa0i7S9rz");
      Resource resource2 = resourceLoader.getResource("file:/app/rejoice-blog/upload-images/2018-10-10/Hacking Exposed 7.pdf.jpg");
       map.add("upload",resource2);
		HttpEntity entity = new HttpEntity<>(map,headers);
		Object data = restTemplate.postForObject("https://my.oschina.net/u/3415536/space/ckeditor_dialog_img_upload", entity, Object.class);
		System.err.println(data);
	}
}
