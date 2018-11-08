package com.rejoice.blog.crawer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.service.CrawerBookService;
import com.rejoice.blog.service.DictionaryService;
import com.rejoice.blog.service.epub.EpubService;
import com.rejoice.blog.service.pdf.PdfImageService;

public abstract class BookCrawer {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(BookCrawer.class);
	
	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	CrawerBookService crawerBookService;
	
	@Value("${blog.resource.down-load.dir}")
	protected String pdfDir;


	@Value("${blog.upload.images.dir}")
	protected String imgDir;
	
	@Autowired
	protected PdfImageService pdfImageService;
	
	@Autowired
	private EpubService epubService;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	public abstract void execute();
	
	protected void downloadBook(String downloadUrl,String url, String name) throws IOException {
		CrawerBook cons = new CrawerBook();
		name = name.replace("[seosee.info]", "");
		name = name.replace(".pdf", "[www.rejoiceblog.com].pdf");
		name = name.replace(".epub", "[www.rejoiceblog.com].epub");
		name = name.replace(".mobi", "[www.rejoiceblog.com].mobi");
		name = name.replaceAll("_", " ");
		cons.setName(name);
		Integer count = crawerBookService.queryCount(cons);
		if(count > 0) {
			LOGGER.info("already exists with name=[{}]", name);
			return;
		}
		CrawerBook crawerBook = new CrawerBook();
		crawerBook.setIsUpload(false);
		crawerBook.setName(name);
		//download book
		Resource resource = resourceLoader.getResource(pdfDir);
		String abslutePdfDir = resource.getFile().getAbsolutePath();
		abslutePdfDir = abslutePdfDir+"/"+name;
		crawerBookService.download(downloadUrl,abslutePdfDir);
		//save book
		crawerBook.setLocalPath(pdfDir+"/"+name);
		crawerBook.setImg(imgDir+"/"+name+".jpg");
		crawerBook.setUrl(url);
		crawerBookService.saveSelective(crawerBook);
		
		//add link and screen shot
		String fileLocalPath = crawerBook.getLocalPath();
		pdfImageService.addLink(fileLocalPath);
		if(fileLocalPath.endsWith(".pdf")){
			pdfImageService.screenShot(crawerBook.getLocalPath());
		}else if(fileLocalPath.endsWith(".epub")) {
			epubService.screenShot(crawerBook.getLocalPath());
		}
		
	}

}
