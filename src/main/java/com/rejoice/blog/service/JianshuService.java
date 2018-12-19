package com.rejoice.blog.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.bean.http.jianshu.NotesAddInput;
import com.rejoice.blog.bean.http.jianshu.NotesAddOutput;
import com.rejoice.blog.bean.http.jianshu.NotesUpdateInput;
import com.rejoice.blog.bean.http.jianshu.NotesUpdateOutput;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.PdfBook;

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
	
	@Autowired
	private ApiAccountService apiAccountService;
	
	private static final String NOTES_URL = "https://www.jianshu.com/notes/";
	private static final String AUTHOR_NOTES_URL = "https://www.jianshu.com/author/notes/";
	public static final Long COLLECTION_ID_BOOK = 576368L;
	public static final Long COLLECTION_ID_IT = 576845L;
	public static final String NOTEBOOK_ID_IT ="19669537";
	
	public void uploadImg(PdfBook pdfBook) {
		//1、upload img
		uploadService.uploadImg(pdfBook);
		PdfBook newBook = new PdfBook();
		newBook.setId(pdfBook.getId());
		newBook.setImgUrl(pdfBook.getImgUrl());
		newBook.setIsUploadImg(true);
		pdfBookService.updateByIdSelective(newBook);
	}
	
	public void post(PdfBook pdfBook) throws Exception {
		//2、post article
		postArticle(pdfBookService.getContent(pdfBook)
				,pdfBook.getTitle()
				,JsonUtil.toBean(this.jianshuAccount.getMetadata(),Map.class).get("noteBookId").toString()
				,COLLECTION_ID_BOOK);
	}

	public NotesAddOutput postArticle(String content,String title,String noteBookId,Long collectionId) {
		//1、新建文章
		NotesAddInput notesAddInput = new NotesAddInput();
		notesAddInput.setNotebook_id(noteBookId);
		notesAddInput.setAt_bottom(false);
		notesAddInput.setTitle("新文章："+System.currentTimeMillis());
		NotesAddOutput notesAddOutput = restTemplate.postForObject(
				AUTHOR_NOTES_URL
				, getHttpEntity(this.jianshuAccount.getCookies(), notesAddInput)
				, NotesAddOutput.class);
		
		updateArticle(content, title, notesAddOutput.getId(), 1L);
		//4、收录到专题
		Map<String,Long> data = new HashMap<>();
		data.put("collection_id", collectionId);
		restTemplate.postForObject(
				NOTES_URL+notesAddOutput.getId()+"/submit"
				, getHttpEntity(this.getJianshuAccount().getCookies(), data)
				, Object.class);
		return notesAddOutput;
	}

	public void updateArticle(String content, String title, String id, Long autosave_control) {
		// 1、get account
		this.jianshuAccount = apiAccountService.getJianshuAccount();
		//2、更新文章
		NotesUpdateInput notesUpdateInput = new NotesUpdateInput();
		notesUpdateInput.setContent(content);
		notesUpdateInput.setTitle(title.replace("[www.rejoiceblog.com]", ""));
		notesUpdateInput.setId(id);
		notesUpdateInput.setAutosave_control(autosave_control);
		restTemplate.exchange(
				AUTHOR_NOTES_URL+id
				, HttpMethod.PUT
				, getHttpEntity(this.jianshuAccount.getCookies(), notesUpdateInput)
				, NotesUpdateOutput.class).getBody();
		//3、发布文章
		restTemplate.postForObject(
				AUTHOR_NOTES_URL+id+"/publicize"
				, getHttpEntity(this.jianshuAccount.getCookies(), null), Object.class);
		LOG.info("post article :{} to chengtong success", notesUpdateInput.getTitle());
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
