package com.rejoice.blog.crawer;

import java.io.IOException;

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
import org.springframework.stereotype.Component;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.service.DictionaryService;

@Component
public class AllitebooksCrawer extends BookCrawer{
	
	public static final int RETRY_COUNT = 3;
	
	public static int singlePageRetryCount = RETRY_COUNT;
	public static int singleBookRetryCount = RETRY_COUNT;
	public static int CRAWER_END_PAGE = 30;
	public static int CRAWER_START_PAGE = 1;
	private static final String PAGE_URL = "http://www.allitebooks.com/page/";
	private static final Logger LOGGER = LoggerFactory.getLogger(AllitebooksCrawer.class);
	
	public void execute(){
		Dictionary endPageDict = dictionaryService.queryOneByCodeAndKey(Constant.CODE_CRAWER_BOOK_END_PAGE,Constant.DICT_KEY_ALLITEBOOKS);
		Dictionary startPageDict = dictionaryService.queryOneByCodeAndKey(Constant.CODE_CRAWER_BOOK_START_PAGE,Constant.DICT_KEY_ALLITEBOOKS);
		int endPage = CRAWER_END_PAGE;
		int startPage = CRAWER_START_PAGE;
		if(endPageDict != null && StringUtils.isNotBlank(endPageDict.getValue())) {
			endPage = Integer.parseInt(endPageDict.getValue());
		}
		if(startPageDict != null && StringUtils.isNotBlank(startPageDict.getValue())) {
			startPage = Integer.parseInt(startPageDict.getValue());
		}
		for (int i = startPage; i <= endPage; i++) {
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
			//getSinglePage(url);
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
					String fileName = pdfBookUrl.substring(pdfBookUrl.lastIndexOf("/")+1);
					this.downloadBook(pdfBookUrl,pdfBookUrl, fileName);
				}
				
			}
		} catch (Exception e) {
			LOGGER.warn("download file failed:",e);
			if(singleBookRetryCount <= 0) {
				singleBookRetryCount = RETRY_COUNT;
				return;
			}
			singleBookRetryCount--;
			//getSingleBook(pdfPageUrl);
		}
		
	}


}
