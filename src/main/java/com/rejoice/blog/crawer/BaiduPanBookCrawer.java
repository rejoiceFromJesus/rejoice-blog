package com.rejoice.blog.crawer;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaiduPanBookCrawer{
	
	public static final String VERIFY_URL = "https://pan.baidu.com/share/verify?surl=dsd-Lv6BdMja8C_tZGg_Kg&t=1539519907046&channel=chunlei&web=1&app_id=250528&bdstoken=null&logid=MTUzOTUxOTkwNzA0ODAuMjIzOTA5MzY5MjY4MzAxNjM=&clienttype=0";
	
	public static void main(String[] args) throws IOException {
		new BaiduPanBookCrawer().execute("https://pan.baidu.com/s/1bV9b8EsEPuG-P40aG9FiTw","1234");
	}

	public void execute(String bookUrl, String passCode) throws IOException {
		Document document = Jsoup.connect(bookUrl).get();
		System.err.println(document.html());
	}

}
