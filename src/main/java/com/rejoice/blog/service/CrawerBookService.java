package com.rejoice.blog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.entity.CrawerBook;

@Service
public class CrawerBookService extends BaseService<CrawerBook> {

	@Autowired
	private RestTemplate restTemplate;

	public void download(String pdfBookUrl,String realPath) throws IOException {
		ResponseEntity<byte[]> response = restTemplate.exchange(pdfBookUrl, HttpMethod.GET, null, byte[].class);
		Files.write(Paths.get(realPath), response.getBody());
	}
	
	/*public String upload(CrawerBook book) throws IOException {
		String fullPath = pdfDir+"/"+DateTime.now().toString(Constant.DATE_FORMAT_PATTERN1)+"/"+fileName;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(pdfBookUrl, HttpMethod.GET, entity, byte[].class);
		Files.write(Paths.get(pdfDir), response.getBody());
		return fullPath;
	}*/

}
