package com.rejoice.blog.crawer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.service.CrawerBookService;

@Component
public class AllitebooksCrawer {
	
	@Autowired
	CrawerBookService crawerBookService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${blog.resource.down-load.dir}")
	public String pdfDir;

	
	
	public static final int RETRY_COUNT = 3;
	
	public static int singlePageRetryCount = RETRY_COUNT;
	public static int singleBookRetryCount = RETRY_COUNT;
	

	private static final String PAGE_URL = "http://www.allitebooks.com/page/";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllitebooksCrawer.class);

	
	
	public void getPdfBooks(){
		for (int i = 1; i < 30; i++) {
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
					crawerBook.setUrl(pdfBookUrl);
					crawerBook.setIsUpload(false);
					crawerBook.setName(fileName);
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
					crawerBookService.saveSelective(crawerBook);
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
