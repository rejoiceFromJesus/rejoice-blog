package com.rejoice.blog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rejoice.blog.crawer.AllitebooksCrawer;
import com.rejoice.blog.service.PdfBookService;


@Component
public class CrawerAllitebooksTask {

	@Autowired
	AllitebooksCrawer allitebooksCrawer;
	
	@Autowired
	PdfBookService pdfBookService;
	
	@Scheduled(cron="${blog.task.crawerallitebooks}")
	public void execute() {
		allitebooksCrawer.getPdfBooks();
		allitebooksCrawer.uploadBooks();
		pdfBookService.batchPost();
	}
}
