package com.rejoice.blog.crawer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.vo.http.ebook300.DownloadUrlInput;

@Component
public class Ebook300CBookCrawer extends BookCrawer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Ebook300CBookCrawer.class);

	private static final String DOWNLOAD_BOOK_URL = "http://longfiles.com/${id}/${book}.html";

	@Value("${blog.resource.down-load.dir}")
	protected String pdfDir;

	public static int CRAWER_END_PAGE = 30;
	public static int CRAWER_START_PAGE = 1;
	private static final String PAGE_URL = "http://www.ebook3000.com/index_${page}.htm";
	private static final String[] CAREGORY_URLS = { "http://www.ebook3000.com/Database/list_5_${page}.html",
			"http://www.ebook3000.com/Engineering-Technology/list_10_${page}.html",
			"http://www.ebook3000.com/Games/list_95_${page}.html",
			"http://www.ebook3000.com/Hardware/list_7_${page}.html",
			"http://www.ebook3000.com/mobile-ebooks/list_108_${page}.html",
			"http://www.ebook3000.com/Programming/list_11_${page}.html",
			"http://www.ebook3000.com/Web_Development/list_13_${page}.html",
			"http://www.ebook3000.com/Study/list_37_${page}.html",
			"http://www.ebook3000.com/Security/list_96_${page}.html",
			"http://www.ebook3000.com/Software/list_12_${page}.html" };

	@Override
	public void execute() {
		Dictionary endPageDict = dictionaryService.queryOneByCodeAndKey(Constant.DICT_CODE_CRAWER_BOOK_END_PAGE,
				Constant.DICT_KEY_EBOOK300);
		Dictionary startPageDict = dictionaryService.queryOneByCodeAndKey(Constant.DICT_CODE_CRAWER_BOOK_START_PAGE,
				Constant.DICT_KEY_EBOOK300);
		int endPage = CRAWER_END_PAGE;
		int startPage = CRAWER_START_PAGE;
		if (endPageDict != null && StringUtils.isNotBlank(endPageDict.getValue())) {
			endPage = Integer.parseInt(endPageDict.getValue());
		}
		if (startPageDict != null && StringUtils.isNotBlank(startPageDict.getValue())) {
			startPage = Integer.parseInt(startPageDict.getValue());
		}
		for (String caregoryUrl : CAREGORY_URLS) {

			for (int i = startPage; i <= endPage; i++) {
				String url = caregoryUrl.replaceAll("\\$\\{page\\}", i + "");
				try {
					Document document = Jsoup.connect(url).get();
					Elements links = document.select("#mains_left .list_box .list_box_title a");
					for (Element link : links) {
						try {
							String pageUrl = link.absUrl("href");
							String[] strs = pageUrl.split("_");
							Document singlePageDocument = Jsoup.connect(pageUrl).get();
							String downloadPageUrl = singlePageDocument.select(".article_info + .mains_left_box").get(0)
									.select("a").get(0).absUrl("href");
							System.err.println(downloadPageUrl);
							Document downloadPage = Jsoup.connect(downloadPageUrl).get();
							String rand = downloadPage.select("input[name=rand]").val();
							System.err.println(rand);
							strs = downloadPageUrl.split("/");
							String bookName = strs[4].replaceAll(".html", "");
							if (!StringUtils.endsWithAny(bookName, ".pdf", ".epub")) {
								LOGGER.info("crawer single book fails,unsuported file :" + bookName);
								continue;
							}
							DownloadUrlInput input = new DownloadUrlInput();
							input.setId(strs[3]);
							input.setRand(rand);
							String getDownloadUrl = DOWNLOAD_BOOK_URL.replaceAll("\\$\\{id\\}", input.getId())
									.replaceAll("\\$\\{book\\}", bookName);
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
							HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(
									RejoiceUtil.objectToFormData(input), headers);
							Thread.sleep(6000);
							ResponseEntity<String> response = restTemplate.exchange(getDownloadUrl, HttpMethod.POST,
									entity, String.class);
							System.err.println(JsonUtil.buildObjectMapper().writeValueAsString(entity));
							// Files.write(Paths.get("C:\\app\\rejoice-blog\\download-pdf\\"+bookName),
							// response.getBody());
							List<String> list = response.getHeaders().get("Location");
							this.downloadBook(list.get(0), pageUrl, bookName);
						} catch (Exception e) {
							LOGGER.warn("download ebook300 failed:", e);
						}

					}
				} catch (Exception e) {
					LOGGER.warn("connect to ebook300 failed:", e);
				}

			}
		}
	}

	public static void main(String[] args) throws IOException {

	}

}
