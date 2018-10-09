package com.rejoice.blog.service;

import org.springframework.stereotype.Service;

import com.rejoice.blog.entity.PdfBook;

@Service
public class PdfBookService extends BaseService<PdfBook> {


	public void batchImport(String books) {
		String[] bookStrArray = books.split("\n");
		for (String bookStr : bookStrArray) {
			String[] book = bookStr.split(": https://");
			PdfBook pdfBook = new PdfBook();
			pdfBook.setTitle(book[0]);
			pdfBook.setImg("/"+book[0]+".jpg");
			pdfBook.setUrl("https://"+book[1]);
			pdfBook.setIsPost(false);
			this.saveSelective(pdfBook);
		}
	}

}
