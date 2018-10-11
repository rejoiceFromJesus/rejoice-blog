package com.rejoice.blog.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.vo.http.oschina.AuthorizedCodeInput;
import com.rejoice.blog.vo.http.oschina.BlogSaveInput;

@Service
public class OschinaService {
	
	private static final String AUTHORIZE_URL = "https://www.oschina.net/action/oauth2/authorize";

	@Autowired
	private ApiAccountService apiAccountService;
	
	@Autowired
	private PdfBookService pdfBookService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static final String POST_BLOG_URL = "https://my.oschina.net/u/3415536/blog/save";
	
	
	public void post(PdfBook pdfBook,ApiAccount apiAccount) {
		BlogSaveInput input = new BlogSaveInput();
		input.setTitle(pdfBook.getTitle());
		input.setUser_code(apiAccount.getToken());
		input.setContent(pdfBookService.getContent(pdfBook));
		restTemplate.postForObject(POST_BLOG_URL, getHttpEntity(input, apiAccount.getCookies()), Object.class);
	}
	
	private String getAuthroizedCode() {
		ApiAccount oschinaAccount = apiAccountService.getOschinaAccount();
		AuthorizedCodeInput input = JsonUtil.toBean(oschinaAccount.getMetadata(), AuthorizedCodeInput.class);
		ResponseEntity<String> data = restTemplate.postForEntity(AUTHORIZE_URL, getHttpEntity(input,oschinaAccount.getCookies()), String.class);
		return data.getHeaders().get("Location").get(0);
	}
	
	private HttpEntity<Object> getHttpEntity(Object body,String cookies) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Cookie", cookies);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		Map<String, Object> bodyMap = RejoiceUtil.objectFieldsToMap(body);
		Set<Entry<String, Object>> entrySet = bodyMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			map.add(entry.getKey(), entry.getValue());
		}
		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
		return entity;
	}
}
