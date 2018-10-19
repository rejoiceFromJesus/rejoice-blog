package com.rejoice.blog.crawer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.service.CrawerBookService;
import com.rejoice.blog.service.DictionaryService;
import com.rejoice.blog.service.pdf.PdfService;

public abstract class BookCrawer {
	
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
	protected PdfService pdfService;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	public abstract void execute();
	
	protected void downloadBook(String downloadUrl,String url, String name) throws IOException {
		CrawerBook cons = new CrawerBook();
		cons.setUrl(url);
		CrawerBook exist = crawerBookService.queryOne(cons);
		if(exist != null) {
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
		pdfService.addLink(crawerBook.getLocalPath());
		pdfService.screenShot(crawerBook.getLocalPath());
	}

}
