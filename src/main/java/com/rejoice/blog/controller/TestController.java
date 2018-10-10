package com.rejoice.blog.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
	    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("token", "_OrITNvuapwhPQ29D4AtyiTmAJT9BScZhQZUnk4o:vGvlcrOV7uOjVVaJuBo2YZC-LAc=:eyJzY29wZSI6ImppYW5zaHUtdXBsb2FkLWltYWdlczp1cGxvYWRfaW1hZ2VzLzcxNzYyNjgtMTM4ZGE3NDE5MzkwYzNkZi5qcGciLCJlbmRVc2VyIjoiNzE3NjI2OCIsImNhbGxiYWNrVXJsIjoiaHR0cHM6Ly93d3cuamlhbnNodS5jb20vdXBsb2FkX2ltYWdlcy9jYWxsYmFjay9kbyIsImNhbGxiYWNrQm9keSI6ImtleT0kKGtleSlcdTAwMjZmaWxlc2l6ZT0kKGZzaXplKVx1MDAyNmZpbGVmb3JtYXQ9JChpbWFnZUluZm8uZm9ybWF0KVx1MDAyNm1pbWVfdHlwZT0kKG1pbWVUeXBlKVx1MDAyNmV4aWY9JChleGlmKVx1MDAyNnVpZD0kKGVuZFVzZXIpXHUwMDI2aGVpZ2h0PSQoaW1hZ2VJbmZvLmhlaWdodClcdTAwMjZ3aWR0aD0kKGltYWdlSW5mby53aWR0aClcdTAwMjZwcm90b2NvbD0kKHg6cHJvdG9jb2wpIiwiZGVhZGxpbmUiOjE1MzkxNTg2NTYsImZzaXplTGltaXQiOjEwNDg1NzYwLCJtaW1lTGltaXQiOiJpbWFnZS9qcGc7aW1hZ2UvanBlZztpbWFnZS9naWY7aW1hZ2UvcG5nIiwidXBob3N0cyI6WyJodHRwOi8vdXAucWluaXUuY29tIiwiaHR0cDovL3VwbG9hZC5xaW5pdS5jb20iLCItSCB1cC5xaW5pdS5jb20gaHR0cDovLzE4My4xMzEuNy4xOCJdLCJnbG9iYWwiOmZhbHNlfQ==");
        map.add("key","upload_images/7176268-138da7419390c3df.jpg");
        map.add("file",resourceLoader.getResource("file:/app/rejoice-blog/upload-images/2018-10-10/Hacking Exposed 7.pdf.jpg"));
		HttpEntity entity = new HttpEntity<>(map,headers);
		Object data = restTemplate.postForObject("https://upload.qiniup.com/", entity, Object.class);
		System.err.println(data);
	}
}
