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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.vo.http.jianshu.NotesAddInput;
import com.rejoice.blog.vo.http.jianshu.NotesAddOutput;
import com.rejoice.blog.vo.http.jianshu.NotesUpdateInput;
import com.rejoice.blog.vo.http.jianshu.NotesUpdateOutput;
import com.rejoice.blog.vo.http.jianshu.UploadOutput;
import com.rejoice.blog.vo.http.jianshu.UploadTokenOutput;

@Service
public class JianshuService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(JianshuService.class);

	@Autowired
	RestTemplate restTemplate;
	
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	private static final String NOTES_URL = "https://www.jianshu.com/notes/";
	private static final String AUTHOR_NOTES_URL = "https://www.jianshu.com/author/notes/";
	private static final String NOTEBOOK_ID_PDFBOOK = "19669528";
	private static final String UPLOAD_TOKEN_URL = "https://www.jianshu.com/upload_images/token.json?filename=";
	private static final Long COLLECTION_ID = 576368L;
	private static final String UPLOAD_URL = "https://upload.qiniup.com/";
	
	
	private HttpEntity<MultiValueMap<String, Object>> getUploadEntity(UploadTokenOutput token, Object file) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("token", token.getToken());
        map.add("key", token.getKey());
        map.add("file",file);
        return new HttpEntity<MultiValueMap<String, Object>>(map, headers);
	}
	
	private UploadTokenOutput getUploadToken(String fileName,String cookies) {
		String url = UPLOAD_TOKEN_URL+fileName;
		UploadTokenOutput tokenOutput = restTemplate.exchange(url
				, HttpMethod.GET
				, this.getHttpEntity(cookies, null)
				,  UploadTokenOutput.class).getBody();
		return tokenOutput;
	}
	
	public Object post(PdfBook pdfBook,String cookies) throws Exception {
		
		try {
			//1、上传图片
			UploadTokenOutput uploadToken = this.getUploadToken(pdfBook.getImg(), cookies);
			HttpEntity<MultiValueMap<String, Object>> uploadEntity = this.getUploadEntity(uploadToken,
					resourceLoader.getResource(pdfBook.getImgUrl()));
			UploadOutput uploadFile = restTemplate.postForObject(UPLOAD_URL, uploadEntity, UploadOutput.class);
			pdfBook.setImgUrl(uploadFile.getUrl());
		} catch (Exception e) {
			LOGGER.warn("upload file to jianshu failed,release lock to exit");
			VolitateVars.POST_BATCH_LOCK = Constant.FALSE;
			throw e;
		}
		//2、新建文章
		NotesAddInput notesAddInput = new NotesAddInput();
		notesAddInput.setNotebook_id(NOTEBOOK_ID_PDFBOOK);
		notesAddInput.setAt_bottom(false);
		notesAddInput.setTitle("新文章："+System.currentTimeMillis());
		NotesAddOutput notesAddOutput = restTemplate.postForObject(
				AUTHOR_NOTES_URL
				, getHttpEntity(cookies, notesAddInput)
				, NotesAddOutput.class);
		
		//3、更新文章
		NotesUpdateInput notesUpdateInput = new NotesUpdateInput();
		notesUpdateInput.setContent(pdfBookService.getContent(pdfBook));
		notesUpdateInput.setTitle(pdfBook.getTitle());
		notesUpdateInput.setId(notesAddOutput.getId());
		NotesUpdateOutput notesUpdateOutput = restTemplate.exchange(
				AUTHOR_NOTES_URL+notesAddOutput.getId()
				, HttpMethod.PUT
				, getHttpEntity(cookies, notesUpdateInput)
				, NotesUpdateOutput.class).getBody();
		JsonUtil.toJson(notesUpdateOutput);
		//4、发布文章
		restTemplate.postForObject(
				AUTHOR_NOTES_URL+notesAddOutput.getId()+"/publicize"
				, getHttpEntity(cookies, null), Object.class);
		//5、收录到专题
		Map<String,Long> data = new HashMap<>();
		data.put("collection_id", COLLECTION_ID);
		restTemplate.postForObject(
				NOTES_URL+notesAddOutput.getId()+"/submit"
				, getHttpEntity(cookies, data)
				, Object.class);
		return notesUpdateInput;
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
