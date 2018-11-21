package com.rejoice.blog.crawer;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.bean.http.baidupan.BaiduFile;
import com.rejoice.blog.bean.http.baidupan.DownloadPageOutput;
import com.rejoice.blog.bean.http.baidupan.VerifyOutput;

public class BaiduPanCrawer{

	public static void main(String[] args) throws IOException {
		
		String url ="a4h4w4PgPIO3wCwp87olHg";
		String verify_code_url = "https://pan.baidu.com/share/verify?surl="+url+"&channel=chunlei&web=1&app_id=250528&clienttype=0";
		String download_page_url = "https://pan.baidu.com/s/1"+url;
		String get_download_url = "https://pan.baidu.com/api/sharedownload?sign=${sign}&timestamp=${timestamp}&channel=chunlei&web=1&app_id=250528&bdstoken=62bd19aaccc8f980ef722cd00591780a&logid=MTU0MDM0Mzc0MDk4NzAuNjQyMDgxMTY4ODUzNDQwNQ==&clienttype=0";
		//&bdstoken=62bd19aaccc8f980ef722cd00591780a&logid=MTU0MDI4NzY5NjE4MzAuMDYxNDk0NDEyMDM2NzU0NDM=
		//1、verify code
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Referer", "https://pan.baidu.com/share/init?surl="+url);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("pwd", "jyys");
		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
		VerifyOutput verifyOutput = restTemplate.postForObject(verify_code_url, entity, VerifyOutput.class);
		System.err.println(verifyOutput.getRandsk());
		
		//2、download page
		Document document = Jsoup.connect(download_page_url).cookie("BDCLND", verifyOutput.getRandsk()).get();
		String downloadPageHtml = document.html();
		System.err.println(downloadPageHtml);
		String data = StringUtils.substringBetween(downloadPageHtml, "yunData.setData(", ");");
		System.err.println(data);
		ObjectMapper objectMapper = JsonUtil.buildObjectMapper();
		DownloadPageOutput downloadPageOutput = objectMapper.readValue(data, DownloadPageOutput.class);
		
		//3、get download url
		BaiduFile baiduFile = downloadPageOutput.getFile_list().getList().get(0);
		headers = new HttpHeaders();
		headers.set("Referer", "https://pan.baidu.com/s/1"+url);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Cookie", "BIDUPSID=BAF65CEE7DEAAEE5991B00B23E4CB074; BAIDUID=BA59279B4E0F642947EF9BEE9415AFD8:FG=1; PSTM=1537955843; pan_login_way=1; PANWEB=1; MCITY=-257%3A; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BDUSS=GhkVDE5cVNaWUFCRzNYTnVJOVVxcnpXWGE0Znp0bFdNRi1HV1RucmFlfkJRdlpiQVFBQUFBJCQAAAAAAAAAAAEAAADaYTskvrzS4zAwMQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMG1zlvBtc5bd; STOKEN=503ec35f44d89b12132388c81a019f8d55b3198e6a03b34b1771ee175e903acc; SCRC=fbee23021f225b19d92858cbf73df178; H_PS_PSSID=1427_27210_21088_18559_27400_26350_20718; cflag=15%3A3; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1540287634,1540288028,1540288205,1540289126; BDCLND=dpMmwmpHCK5HkE1BeL4GXdc8L0dMyKhvd4Iaqhw85fw%3D; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; delPer=0; PSINO=7; Hm_lvt_f5f83a6d8b15775a02760dc5f490bc47=1539164424,1540343635; Hm_lpvt_f5f83a6d8b15775a02760dc5f490bc47=1540343680; Hm_lpvt_7a3960b6f067eb0085b7f96ff5e660b0=1540343687; PANPSC=6105048913906017035%3AkR6MKMexwItnuGqF6poasun90oj%2BY%2FIs86jBhH5u6UEcnRFhEA8N7DDCjjLS5Fu910x1RB57NTQq%2BgeXEPC5npqJfc1LLU0VwP4abkSM7mMs9j4XULbP1%2BlOD9HtMllq6LqdS1RIjN376jyG3uDkPYshZ7OchQK1%2Ftbxiqw6Wy59tn9thbnTpn9ULvZboklQLbwO3wrWGjXU2gvzdj3dWg%3D%3D");
		map = new LinkedMultiValueMap<String, Object>();
		
		map.add("extra", URLEncoder.encode("{\"sekey\":\""+verifyOutput.getRandsk()+"\"}", "utf-8"));
		map.add("product", "share");
		map.add("uk", downloadPageOutput.getUk());
		map.add("fid_list", "%5B"+baiduFile.getFs_id()+"%5D");
		map.add("primaryid", downloadPageOutput.getShareid());
		map.add("encrypt", "0");
		System.err.println(objectMapper.writeValueAsString(map));
		entity = new HttpEntity<Object>(map, headers);
		String new_get_download_url = get_download_url
				.replaceAll("\\$\\{sign\\}", downloadPageOutput.getSign())
				.replaceAll("\\$\\{timestamp\\}", downloadPageOutput.getTimestamp());
		System.err.println(new_get_download_url);
		data = restTemplate.postForObject(new_get_download_url, entity, String.class);
		System.err.println(data);
		
	}
	
}
