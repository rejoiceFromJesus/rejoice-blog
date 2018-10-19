package com.rejoice.blog.crawer;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.rejoice.blog.common.util.JsonUtil;

import groovyjarjarasm.asm.tree.TryCatchBlockNode;

@Component
public class ChengTongBookCrawer extends BookCrawer{
	
	private static final String[] CRAWER_URLS = {"http://edu.15kankan.com/","http://it.15kankan.com/"};
	private static final String GET_DOWNLOAD_URL = "https://u${uid}.pipipan.com/get_file_url.php?uid=${uid}&fid=${fid}&folder_id=0&file_chk=${file_chk}&mb=0&app=0";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChengTongBookCrawer.class);

	public void execute() {
			for (String url : CRAWER_URLS) {
				try {
					Document level1 = Jsoup.connect(url).get();
					Elements level1A = level1.select(".widget-body  a");
					for (Element element : level1A) {
						try {
							String href = element.absUrl("href");
							Document level2 = Jsoup.connect(href).get();
							Elements resourceList = level2.select("#resource-list a");
							for (Element resource : resourceList) {
								if (resource.text().endsWith(".pdf") || resource.text().endsWith(".epub")) {
									try {
										String downloadPageUrl = resource.absUrl("href");
										String[] uidFid = downloadPageUrl.split("/")[4].split("-");
										Document document = Jsoup.connect(downloadPageUrl).get();
										String downloadFun = document.select(".download-box a").first().attr("onclick");
										String[] downloadPrams = downloadFun.substring(10, downloadFun.length()-2).replace("'", "").replaceAll(" ", "").split(",");
										String getDownloadUrl = GET_DOWNLOAD_URL.replaceAll("\\$\\{uid\\}", uidFid[0])
												.replaceAll("\\$\\{fid\\}", uidFid[1])
												.replaceAll("\\$\\{file_chk\\}", downloadPrams[2]);
										HttpHeaders headers = new HttpHeaders();
										headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
										HttpEntity<Object> entity = new HttpEntity<>(headers);
										Map data = restTemplate.exchange(getDownloadUrl, HttpMethod.GET, entity, Map.class).getBody();
										if(data.get("code").equals(200)) {
											this.downloadBook(data.get("downurl").toString(), downloadPageUrl, resource.text());
										}else {
											LOGGER.warn("get downurl failed:{}", JsonUtil.toJson(data));
										}
									} catch (Exception e) {
										LOGGER.warn("crawer books failed :",e);
									}
									
								}
							}
						} catch (Exception e) {
							LOGGER.warn("crawer books failed :",e);
						}
						
					}
				} catch (Exception e) {
					LOGGER.warn("crawer cheng tong books failed:{}", url, e);
				}
			}
		
	}

}
