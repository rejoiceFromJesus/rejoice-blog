package com.rejoice.blog.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.bean.http.jianshu.NotesAddInput;
import com.rejoice.blog.bean.http.jianshu.NotesAddOutput;
import com.rejoice.blog.bean.http.jianshu.NotesUpdateInput;
import com.rejoice.blog.bean.http.jianshu.NotesUpdateOutput;
import com.rejoice.blog.bean.http.jianshu.UploadOutput;
import com.rejoice.blog.bean.http.jianshu.UploadTokenOutput;

@Service
public class JianshuService {
	
	private static Logger LOG = LoggerFactory.getLogger(JianshuService.class);

	@Autowired
	RestTemplate restTemplate;
	
	private ApiAccount jianshuAccount;
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	UploadService uploadService;
	
	private static final String NOTES_URL = "https://www.jianshu.com/notes/";
	private static final String AUTHOR_NOTES_URL = "https://www.jianshu.com/author/notes/";
	private static final Long COLLECTION_ID = 576368L;
	
	public void post(PdfBook pdfBook) throws Exception {
		//1、upload img
		uploadService.uploadImg(pdfBook);
		//2、post article
		postArticle(pdfBookService.getContent(pdfBook),pdfBook.getTitle());
	}

	public NotesAddOutput postArticle(String content,String title) {
		//1、新建文章
		NotesAddInput notesAddInput = new NotesAddInput();
		notesAddInput.setNotebook_id(JsonUtil.toBean(this.jianshuAccount.getMetadata(),Map.class).get("noteBookId").toString());
		notesAddInput.setAt_bottom(false);
		notesAddInput.setTitle("新文章："+System.currentTimeMillis());
		NotesAddOutput notesAddOutput = restTemplate.postForObject(
				AUTHOR_NOTES_URL
				, getHttpEntity(this.jianshuAccount.getCookies(), notesAddInput)
				, NotesAddOutput.class);
		
		updateArticle(content, title, notesAddOutput.getId());
		//4、收录到专题
		Map<String,Long> data = new HashMap<>();
		data.put("collection_id", COLLECTION_ID);
		restTemplate.postForObject(
				NOTES_URL+notesAddOutput.getId()+"/submit"
				, getHttpEntity(this.getJianshuAccount().getCookies(), data)
				, Object.class);
		return notesAddOutput;
	}

	public void updateArticle(String content, String title, String id) {
		//2、更新文章
		NotesUpdateInput notesUpdateInput = new NotesUpdateInput();
		notesUpdateInput.setContent(content);
		notesUpdateInput.setTitle(title.replace("[www.rejoiceblog.com]", ""));
		notesUpdateInput.setId(id);
		NotesUpdateOutput notesUpdateOutput = restTemplate.exchange(
				AUTHOR_NOTES_URL+id
				, HttpMethod.PUT
				, getHttpEntity(this.jianshuAccount.getCookies(), notesUpdateInput)
				, NotesUpdateOutput.class).getBody();
		JsonUtil.toJson(notesUpdateOutput);
		//3、发布文章
		restTemplate.postForObject(
				AUTHOR_NOTES_URL+id+"/publicize"
				, getHttpEntity(this.jianshuAccount.getCookies(), null), Object.class);
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

	public ApiAccount getJianshuAccount() {
		return jianshuAccount;
	}

	public void setJianshuAccount(ApiAccount jianshuAccount) {
		this.jianshuAccount = jianshuAccount;
	}

}
