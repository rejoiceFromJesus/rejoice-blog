package com.rejoice.blog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rejoice.blog.crawer.AllitebooksCrawer;


@Component
public class CrawerAllitebooksTask {

	@Autowired
	AllitebooksCrawer allitebooksCrawer;
	
	@Scheduled(cron="${blog.task.crawerallitebooks}")
	public void execute() {
		allitebooksCrawer.getPdfBooks();
	}
}
