package com.rejoice.blog.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.vo.http.jianshu.NotesAddInput;
import com.rejoice.blog.vo.http.jianshu.NotesAddOutput;
import com.rejoice.blog.vo.http.jianshu.NotesUpdateInput;
import com.rejoice.blog.vo.http.jianshu.NotesUpdateOutput;

@Service
public class JianshuService {

	@Autowired
	RestTemplate restTemplate;
	
	private static final String NOTES_URL = "https://www.jianshu.com/notes/";
	private static final String AUTHOR_NOTES_URL = "https://www.jianshu.com/author/notes/";
	private static final String NOTEBOOK_ID_PDFBOOK = "19669528";
	private static final Long COLLECTION_ID = 576368L;
	
	
	public Object post(PdfBook pdfBook,String cookies) {
		//1、新建文章
		NotesAddInput notesAddInput = new NotesAddInput();
		notesAddInput.setNotebook_id(NOTEBOOK_ID_PDFBOOK);
		notesAddInput.setAt_bottom(false);
		notesAddInput.setTitle("新文章："+System.currentTimeMillis());
		NotesAddOutput notesAddOutput = restTemplate.postForObject(
				AUTHOR_NOTES_URL
				, getHttpEntity(cookies, notesAddInput)
				, NotesAddOutput.class);
		System.err.println(JsonUtil.toJson(notesAddOutput));
		//2、更新文章
		NotesUpdateInput notesUpdateInput = new NotesUpdateInput();
		notesUpdateInput.setContent(this.getContent(pdfBook));
		notesUpdateInput.setTitle(pdfBook.getTitle());
		notesUpdateInput.setId(notesAddOutput.getId());
		NotesUpdateOutput notesUpdateOutput = restTemplate.exchange(
				AUTHOR_NOTES_URL+notesAddOutput.getId()
				, HttpMethod.PUT
				, getHttpEntity(cookies, notesUpdateInput)
				, NotesUpdateOutput.class).getBody();
		JsonUtil.toJson(notesUpdateOutput);
		//3、发布文章
		restTemplate.postForObject(
				AUTHOR_NOTES_URL+notesAddOutput.getId()+"/publicize"
				, getHttpEntity(cookies, null), Object.class);
		//4、收录到专题
		Map<String,Long> data = new HashMap<>();
		data.put("collection_id", COLLECTION_ID);
		restTemplate.postForObject(
				NOTES_URL+notesAddOutput.getId()+"/submit"
				, getHttpEntity(cookies, data)
				, Object.class);
		return notesUpdateInput;
	}
	
	private String getContent(PdfBook pdfBook) {
		String content = "<p>下载地址：&nbsp;"
				+ "<a href=\""+pdfBook.getUrl()+"\" target=\"_blank\">"
				+ pdfBook.getTitle()+" 免费下载</a></p><div class=\"image-package\">"
				+ "<img class=\"uploaded-img\" "
				+ "src=\"http://www.rejoiceblog.com/upload-images"+pdfBook.getImg()+"\" width=\"auto\" height=\"auto\">"
				+ "<br><div class=\"image-caption\"></div></div>";
		return content;
	}
	
	private HttpEntity<Object> getHttpEntity(String cookies,Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie", cookies);
		headers.set("Accept", "application/json");
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		String content = null;
		if(body != null) {
			content = JsonUtil.toJson(body);
		}
		HttpEntity<Object> httpEnitty = new HttpEntity<Object>(content, headers);
		return httpEnitty;
	}
}
