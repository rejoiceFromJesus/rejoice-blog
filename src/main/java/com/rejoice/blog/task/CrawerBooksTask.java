package com.rejoice.blog.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.crawer.BookCrawer;
import com.rejoice.blog.crawer.UploadAndPostCrawer;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.service.DictionaryService;
import com.rejoice.blog.service.PdfBookService;


@Component
public class CrawerBooksTask {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CrawerBooksTask.class);

	@Autowired
	List<BookCrawer> bookCrawerList;
	
	@Autowired
	UploadAndPostCrawer uploadAndPostCrawer;
	
	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Scheduled(cron="${blog.task.crawerallitebooks}")
	public void execute() {
		Dictionary dictCons = new Dictionary();
		dictCons.setCode(Constant.DICT_CODE_CRAWER_BOOK);
		dictCons.setKey(Constant.DICT_KEY_CRAWER_BOOK);
		Dictionary dictionary = dictionaryService.queryOne(dictCons);
		if(dictionary == null || Constant.TRUE.equalsIgnoreCase(dictionary.getValue())){
			for (BookCrawer bookCrawer : bookCrawerList) {
				try {
					bookCrawer.execute();
				} catch (Exception e) {
					LOGGER.warn("crawer books failed:",e);
				}
			}
		}
		uploadAndPostCrawer.uploadBooks();
		pdfBookService.batchPost();
		uploadAndPostCrawer.deletePdfInDisk();
		
	}
}
