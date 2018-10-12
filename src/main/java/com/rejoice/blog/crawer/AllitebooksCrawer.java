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
import org.springframework.stereotype.Component;

import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.service.CrawerBookService;

@Component
public class AllitebooksCrawer {
	
	@Autowired
	CrawerBookService crawerBookService;

	private static final String PAGE_URL = "http://www.allitebooks.com/page/";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllitebooksCrawer.class);

	public void getPdfBooks(){
		for (int i = 1; i < 100000; i++) {
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
					CrawerBook crawerBook = new CrawerBook();
					crawerBook.setUrl(pdfBookUrl);
					crawerBook.setName(pdfPageUrl.split("\\/")[3]);
					crawerBookService.saveSelective(crawerBook);
				}
				
			}
		} catch (Exception e) {
			getSingleBook(pdfPageUrl);
		}
		
	}


}
