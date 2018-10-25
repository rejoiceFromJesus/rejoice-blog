package com.rejoice.blog.crawer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.service.ApiAccountService;
import com.rejoice.blog.service.CrawerBookService;
import com.rejoice.blog.service.DictionaryService;
import com.rejoice.blog.service.PdfBookService;
import com.rejoice.blog.service.pdf.PdfService;

@Component
public class UploadAndPostCrawer {
	@Autowired
	CrawerBookService crawerBookService;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	protected PdfService pdfService;
	
	@Autowired
	protected ApiAccountService apiAccountService;
	
	@Value("${blog.resource.down-load.dir}")
	protected String pdfDir;
	
	protected ApiAccount chengTongAccount;
	
	protected String chengTongParams;
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Value("${blog.upload.images.dir}")
	protected String imgDir;
	
	protected static final String CT_UPLOAD_PARAMS_URL = "https://home.ctfile.com/iajax.php?item=files&action=index";
	
	protected static final String CT_UPLOAD_URL = "https://upload.ctfile.com/web/upload.do?userid=1475340&maxsize=2147483648&folderid=0&limit=2&spd=23000000&";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UploadAndPostCrawer.class);
	
	
	@Autowired
	DictionaryService dictionaryService;
	
	public void uploadBooks() {
		this.chengTongAccount = apiAccountService.getChengTongAccount();
		this.chengTongParams = this.getChengTongParams(this.chengTongAccount.getCookies());
		CrawerBook cons = new CrawerBook();
		cons.setIsUpload(false);
		List<CrawerBook> bookList = crawerBookService.queryListByWhere(cons);
		for (CrawerBook crawerBook : bookList) {
			try {
				//1、upload file
				String uploadId = this.uploadToCtCloud(crawerBook);
				//2、upload status
				CrawerBook updateCrawerBook = new CrawerBook();
				updateCrawerBook.setIsUpload(true);
				updateCrawerBook.setId(crawerBook.getId());
				crawerBookService.updateByIdSelective(updateCrawerBook);
				//3、insert pdfBooks to be posted
				PdfBook pdfBook = new PdfBook();
				pdfBook.setFileName(crawerBook.getName());
				pdfBook.setUrl("https://pipipan.ctfile.com/fs/1475340-"+uploadId);
				pdfBook.setIsPostJianshu(false);
				pdfBook.setIsPostOschina(false);
				pdfBook.setIsPostSystem(false);
				pdfBook.setTitle(crawerBook.getName()+"  免费下载");
				pdfBook.setImgUrl(crawerBook.getImg());
				pdfBook.setImg(crawerBook.getName()+".jpg");
				pdfBookService.saveSelective(pdfBook);
			} catch (Exception e) {
				LOGGER.warn("upload file to ct cloud failed:",e);
			}
		}
	}
	
	private String getChengTongParams(String cookies) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("cookie", cookies);
		HttpEntity entity = new HttpEntity<>(headers);
		String content = restTemplate.exchange(CT_UPLOAD_PARAMS_URL,HttpMethod.GET,entity, String.class).getBody();
		content = content.substring(content.lastIndexOf("ctt="), content.lastIndexOf("', '1024mb')"));
		//Map<String, String> paramsToMap = RejoiceUtil.getParamsToMap(content);
		return content;
	}

	private String uploadToCtCloud(CrawerBook crawerBook) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		Resource resource = resourceLoader.getResource(crawerBook.getLocalPath());
		map.add("filesize", resource.contentLength());
        map.add("file",resource);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        String uploadId = restTemplate.postForObject(CT_UPLOAD_URL+this.chengTongParams, httpEntity, String.class);
        return uploadId;
	}
	
	/**
	 * delete all pdf files if all pdf_books posted to jianshu
	 * @throws IOException 
	 */
	public void deletePdfInDisk() {
		try {
			PdfBook cons = new PdfBook();
			cons.setIsPostJianshu(false);
			Integer count = pdfBookService.queryCount(cons);
			if(count <= 0) {
				File pdfFolder = resourceLoader.getResource(pdfDir).getFile();
				File[] listFiles = pdfFolder.listFiles();
				for (File file : listFiles) {
					file.delete();
				}
			}
		} catch (Exception e) {
			LOGGER.warn("delete files failed:",e);
		}
	}
	
}
