package com.rejoice.blog.crawer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.service.ApiAccountService;
import com.rejoice.blog.service.CrawerBookService;
import com.rejoice.blog.service.DictionaryService;
import com.rejoice.blog.service.PdfBookService;
import com.rejoice.blog.service.pdf.PdfService;

@Component
public class AllitebooksCrawer {
	
	@Autowired
	CrawerBookService crawerBookService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private ApiAccountService apiAccountService;
	
	@Value("${blog.resource.down-load.dir}")
	public String pdfDir;
	
	private ApiAccount chengTongAccount;
	
	private String chengTongParams;
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Value("${blog.upload.images.dir}")
	private String imgDir;
	
	public static final int RETRY_COUNT = 3;
	
	public static int singlePageRetryCount = RETRY_COUNT;
	public static int singleBookRetryCount = RETRY_COUNT;
	public static int CRAWER_MAX_PAGE = 30;
	

	private static final String PAGE_URL = "http://www.allitebooks.com/page/";
	
	private static final String CT_UPLOAD_PARAMS_URL = "https://home.ctfile.com/iajax.php?item=files&action=index";
	
	private static final String CT_UPLOAD_URL = "https://upload.ctfile.com/web/upload.do?userid=1475340&maxsize=2147483648&folderid=0&limit=2&spd=23000000&";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllitebooksCrawer.class);
	
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
				crawerBook.setIsUpload(true);
				crawerBookService.updateByIdSelective(crawerBook);
				//3、insert pdfBooks to be posted
				PdfBook pdfBook = new PdfBook();
				pdfBook.setFileName(crawerBook.getName());
				pdfBook.setUrl("https://rejoice.ctfile.com/fs/1475340-"+uploadId);
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
	
	public void getPdfBooks(){
		Dictionary maxPageDict = dictionaryService.queryOneByCodeAndKey(Constant.CODE_CRAWER_BOOK_MAX_PAGE,Constant.DICT_KEY_ALLITEBOOKS);
		int maxPage = CRAWER_MAX_PAGE;
		if(maxPageDict != null && StringUtils.isNotBlank(maxPageDict.getValue())) {
			maxPage = Integer.parseInt(maxPageDict.getValue());
		}
		for (int i = 1; i < maxPage; i++) {
			String url = PAGE_URL + i;
			boolean hasBooks = getSinglePage(url);
			if(!hasBooks) {
				LOGGER.info("NO POSTS FOUND! end current crawing!");
				break;
			}
		}
	}

	private boolean getSinglePage(String url) {
		try {
			Document document = Jsoup.connect(url).get();
			if(!document.select(".page-title").isEmpty()) {
				return false;
			}
			Elements pdfPages = document.select("article .entry-body a[href]");
			for (Element page : pdfPages) {
				String pdfPageUrl = page.attr("abs:href");
				getSingleBook(pdfPageUrl);
			}
		} catch (Exception e) {
			if(singlePageRetryCount <= 0) {
				singlePageRetryCount = RETRY_COUNT;
				return true;
			}
			singlePageRetryCount--;
			getSinglePage(url);
		}
		return true;
	}

	private void getSingleBook(String pdfPageUrl) throws IOException {
		try {
			Document pageDocement = Jsoup.connect(pdfPageUrl).get();
			Elements downloadLinks = pageDocement.select(".download-links a[href]");
			for (Element downloadLink : downloadLinks) {
				String pdfBookUrl = downloadLink.attr("abs:href");
				if (StringUtils.endsWithIgnoreCase(pdfBookUrl, ".pdf")) {
					String fileName = pdfBookUrl.substring(pdfBookUrl.lastIndexOf("/"));
					CrawerBook crawerBook = new CrawerBook();
					crawerBook.setIsUpload(false);
					crawerBook.setName(fileName.substring(1));
					//download book
					Resource resource = resourceLoader.getResource(pdfDir);
					if(!resource.exists()) {
						resource.getFile().mkdirs();
					}
					/*File file = Paths.get(relativePath).toFile();
					if(!file.exists()) {
						file.mkdirs();
					}
					*/
					
					String abslutePdfDir = resource.getFile().getAbsolutePath();
					abslutePdfDir += fileName;
					crawerBookService.download(pdfBookUrl,abslutePdfDir);
					//save book
					crawerBook.setLocalPath(pdfDir+fileName);
					crawerBook.setImg(imgDir+fileName+".jpg");
					crawerBook.setUrl(pdfBookUrl);
					crawerBookService.saveSelective(crawerBook);
					//add link and screen shot
					pdfService.addLink(crawerBook.getLocalPath());
					pdfService.screenShot(crawerBook.getLocalPath());
				}
				
			}
		} catch (Exception e) {
			LOGGER.warn("download file failed:",e);
			if(singleBookRetryCount <= 0) {
				singleBookRetryCount = RETRY_COUNT;
				return;
			}
			singleBookRetryCount--;
			getSingleBook(pdfPageUrl);
		}
		
	}


}
