package com.rejoice.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.rejoice.blog.bean.http.jianshu.UploadOutput;
import com.rejoice.blog.bean.http.jianshu.UploadTokenOutput;
import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.PdfBook;

@Component
public class UploadService {

	private static final Logger LOG = LoggerFactory.getLogger(UploadService.class);

	private static final String UPLOAD_TOKEN_URL = "https://www.jianshu.com/upload_images/token.json?filename=";

	private static final String UPLOAD_URL = "https://upload.qiniup.com/";
	
	private ApiAccount jianshuAccount;
	
	@Autowired
	QiniuService qiniuService;
	

	/*public static void main(String[] args) throws JsonProcessingException {
		HttpEntity<Object> httpEntity = new UploadService().getHttpEntity("232323", null);
		LOG.info("get upload token, input:{},",JsonUtil.buildObjectMapper().writeValueAsString(httpEntity));
	}*/
	
	public ApiAccount getJianshuAccount() {
		return jianshuAccount;
	}

	public void setJianshuAccount(ApiAccount jianshuAccount) {
		this.jianshuAccount = jianshuAccount;
	}

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	public ApiAccountService apiAccountService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	JianshuService jianshuService;

	public String uploadImg(MultipartFile file) {
		try {
			// 1、get account
			this.jianshuAccount = apiAccountService.getJianshuAccount();
			jianshuService.setJianshuAccount(jianshuAccount);
			// 2、upload img
			UploadTokenOutput uploadToken = this.getUploadToken(file.getOriginalFilename());
			HttpEntity<MultiValueMap<String, Object>> uploadEntity = this.getUploadEntity(uploadToken, file.getBytes());
			UploadOutput uploadFile = restTemplate.postForObject(UPLOAD_URL, uploadEntity, UploadOutput.class);
			return uploadFile.getUrl();
		} catch (Exception e) {
			LOG.warn("upload file to jianshu failed", e);
			throw new RuntimeException("上传图片失败");
		}
	}
	
	public String uploadImgToQiniu(MultipartFile file) {
		try {
			return qiniuService.upload(file.getInputStream(), file.getOriginalFilename());
		} catch (Exception e) {
			LOG.warn("upload file to qiniu failed", e);
			throw new RuntimeException("上传图片失败");
		}
	}

	private UploadTokenOutput getUploadToken(String fileName) {
		String url = UPLOAD_TOKEN_URL + fileName;
		HttpEntity<Object> entity = this.getHttpEntity(this.jianshuAccount.getCookies(), null);
		LOG.info("get upload token, input:{}",JsonUtil.toJson(entity));
		ResponseEntity<UploadTokenOutput> response = restTemplate.exchange(url, HttpMethod.GET, entity, UploadTokenOutput.class);
		LOG.info("get upload token, output:{}",JsonUtil.toJson(response));
		UploadTokenOutput tokenOutput = response.getBody();
		return tokenOutput;
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

	private HttpEntity<MultiValueMap<String, Object>> getUploadEntity(UploadTokenOutput token, Object file) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("token", token.getToken());
		map.add("key", token.getKey());
		map.add("file", file);
		return new HttpEntity<MultiValueMap<String, Object>>(map, headers);
	}

	public void uploadImg(PdfBook pdfBook) {
		try {
			UploadTokenOutput uploadToken = this.getUploadToken(pdfBook.getImg().replaceAll("&", "and"));
			Resource imgResource = resourceLoader.getResource(pdfBook.getImgUrl());
			if(imgResource.getFile().exists()) {
				HttpEntity<MultiValueMap<String, Object>> uploadEntity = this.getUploadEntity(uploadToken,
						resourceLoader.getResource(pdfBook.getImgUrl()));
				UploadOutput uploadFile = restTemplate.postForObject(UPLOAD_URL, uploadEntity, UploadOutput.class);
				pdfBook.setImgUrl(uploadFile.getUrl());
			}
		} catch (Exception e) {
			VolitateVars.POST_JIANSHU_BATCH_LOCK=Constant.FALSE;
			VolitateVars.POST_SYSTEM_BATCH_LOCK=Constant.FALSE;
			throw new RuntimeException("upload image to jianshu failed",e);
		}
	}
	
	
	public void uploadImgToQiniu(PdfBook pdfBook) {
		try {
			Resource imgResource = resourceLoader.getResource(pdfBook.getImgUrl());
			if(imgResource.getFile().exists()) {
				String url = qiniuService.upload(imgResource.getInputStream(), pdfBook.getFileName());
				pdfBook.setImgUrl(url);
			}
		} catch (Exception e) {
			VolitateVars.POST_JIANSHU_BATCH_LOCK=Constant.FALSE;
			VolitateVars.POST_SYSTEM_BATCH_LOCK=Constant.FALSE;
			throw new RuntimeException("upload image to qiniu failed",e);
		}
	}
}
