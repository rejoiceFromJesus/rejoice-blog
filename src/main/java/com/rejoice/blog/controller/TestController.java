package com.rejoice.blog.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.service.OschinaService;
import com.rejoice.blog.vo.http.oschina.BlogSaveInput;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	OschinaService oschinaService;
	
	
	
	//@GetMapping("/oschina/post")
	public Object testPostToOschina() {
		BlogSaveInput input = new BlogSaveInput();
		input.setTitle("haha");
		input.setContent("nihaofdsfd富商大贾水电费fsd");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("cookie", "oscid=inZ7aH3QlhfhJlDeONY6h%2BHU7kKPY8Yco88jsmyMEhbbGZpW70RZS6tutkpi3HHvUXbYzATtHo%2BvuFtG28vnmX281fr%2Bc3%2FmIpKcC509iZV%2BWzWExO3DzwhmwAuLXRaFGjUc5aHZ69AahAwSwXtBXw%3D%3D");
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		Map<String, Object> bodyMap = RejoiceUtil.objectFieldsToMap(input);
		Set<Entry<String, Object>> entrySet = bodyMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			map.add(entry.getKey(), entry.getValue());
		}
		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
		return restTemplate.postForObject("https://my.oschina.net/u/3415536/blog/save", entity, Object.class);
	} 
	
	//@GetMapping("/upload-to-oschina")
	public void testUploadToOschina() throws IOException {
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
	//@GetMapping("/upload")
	public void testUpload() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("token", "_OrITNvuapwhPQ29D4AtyiTmAJT9BScZhQZUnk4o:nz8-WTwQ-I3aKYaHpENQmqBLGWM=:eyJzY29wZSI6ImppYW5zaHUtdXBsb2FkLWltYWdlczp1cGxvYWRfaW1hZ2VzLzcxNzYyNjgtZTQzYmVlMTYwNzhiMDVjNC5qcGciLCJlbmRVc2VyIjoiNzE3NjI2OCIsImNhbGxiYWNrVXJsIjoiaHR0cHM6Ly93d3cuamlhbnNodS5jb20vdXBsb2FkX2ltYWdlcy9jYWxsYmFjay9kbyIsImNhbGxiYWNrQm9keSI6ImtleT0kKGtleSlcdTAwMjZmaWxlc2l6ZT0kKGZzaXplKVx1MDAyNmZpbGVmb3JtYXQ9JChpbWFnZUluZm8uZm9ybWF0KVx1MDAyNm1pbWVfdHlwZT0kKG1pbWVUeXBlKVx1MDAyNmV4aWY9JChleGlmKVx1MDAyNnVpZD0kKGVuZFVzZXIpXHUwMDI2aGVpZ2h0PSQoaW1hZ2VJbmZvLmhlaWdodClcdTAwMjZ3aWR0aD0kKGltYWdlSW5mby53aWR0aClcdTAwMjZwcm90b2NvbD0kKHg6cHJvdG9jb2wpIiwiZGVhZGxpbmUiOjE1MzkxNzA3MjksImZzaXplTGltaXQiOjEwNDg1NzYwLCJtaW1lTGltaXQiOiJpbWFnZS9qcGc7aW1hZ2UvanBlZztpbWFnZS9naWY7aW1hZ2UvcG5nIiwidXBob3N0cyI6WyJodHRwOi8vdXAucWluaXUuY29tIiwiaHR0cDovL3VwbG9hZC5xaW5pdS5jb20iLCItSCB1cC5xaW5pdS5jb20gaHR0cDovLzE4My4xMzEuNy4xOCJdLCJnbG9iYWwiOmZhbHNlfQ==");
        map.add("key","upload_images/7176268-e43bee16078b05c4.jpg");
        map.add("file",resourceLoader.getResource("file:/app/rejoice-blog/upload-images/2018-10-10/Hacking Exposed 7.pdf.jpg"));
		HttpEntity entity = new HttpEntity<>(map,headers);
		Object data = restTemplate.postForObject("https://upload.qiniup.com/", entity, Object.class);
		System.err.println(data);
	}
	
	/*@GetMapping("/oschina/code")
	public String oschinaCode() {
		return oschinaService.getAuthroizedCode();
	}*/
}
