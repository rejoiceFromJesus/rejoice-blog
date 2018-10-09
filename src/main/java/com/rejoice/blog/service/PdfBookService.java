package com.rejoice.blog.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.PdfBook;

@Service
public class PdfBookService extends BaseService<PdfBook> {

	@Autowired
	public ApiAccountService apiAccountService;
	
	@Autowired
	JianshuService jianshuService;
	
	@Autowired
	PdfBookService pdfBookService;

	public void batchImport(String books) {
		String[] bookStrArray = books.split("\n");
		String today = DateTime.now().toString(Constant.DATE_FORMAT_PATTERN1);
		for (String bookStr : bookStrArray) {
			String[] book = bookStr.split(": https://");
			PdfBook pdfBook = new PdfBook();
			pdfBook.setTitle(book[0]);
			pdfBook.setImg("/"+today+"/"+book[0]+".jpg");
			pdfBook.setUrl("https://"+book[1]);
			pdfBook.setIsPost(false);
			this.saveSelective(pdfBook);
		}
	}

	public String batchPost() {
		new Thread(()->{
			PdfBook cons = new PdfBook();
			cons.setIsPost(false);
			List<PdfBook> list = this.queryListByWhere(cons);
			ApiAccount jianshuAccount = apiAccountService.getJianshuAccount();
			for (PdfBook pdfBook : list) {
				try {
					jianshuService.post(pdfBook, jianshuAccount.getCookies());
					pdfBook.setIsPost(true);
					pdfBookService.updateByIdSelective(pdfBook);
					Thread.sleep(2000);
				} catch (Exception e) {
					LOGGER.warn("POST article failed:",e);
				}
			}
		}).start();
		return null;
	}

}
