package com.rejoice.blog.crawer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Java1234BookCrawer extends BookCrawer{

	public static final String URL = "http://java1234.com/a/javabook/";
	public static void main(String[] args) throws Exception {
		BaiduPanBookCrawer baiduPanBookCrawer = new BaiduPanBookCrawer();
		
		
		Document doc = Jsoup.connect(URL).get();
		Elements books = doc.select(".listbox dd a");
		for (Element book : books) {
			String bookUrl = book.absUrl("href");
			Elements baiduLinks = Jsoup.connect(bookUrl).get().select(".content a[href^=https://pan.baidu.com]");
			if(!baiduLinks.isEmpty()) {
				Element baiduLink = baiduLinks.get(0);
				String baiduPanUrl = baiduLink.absUrl("href");
				String passCode = baiduLink.nextElementSibling().text();
				baiduPanBookCrawer.execute(baiduPanUrl,passCode);
			}
			Thread.sleep(500);
			
		}
		
	}
	@Override
	public void execute() {
		
	}
}
