package com.rejoice.blog.service;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.entity.PdfBook;

@Service
public class PdfBookService extends BaseService<PdfBook> {

	@Autowired
	public ApiAccountService apiAccountService;
	
	@Autowired
	JianshuService jianshuService;
	
	@Autowired
	DictionaryService dictionaryService;
	
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
			pdfBook.setIsPostJianshu(false);
			pdfBook.setIsPostOschina(false);
			pdfBook.setIsPostSystem(false);
			this.saveSelective(pdfBook);
		}
	}

	public String batchPost() {
		new Thread(()->{
			postBatchToJIanshu();
		}).start();
		return null;
	}
	
	@Transactional(readOnly = true)
	private void postBatchToJIanshu() {
		
		//1、check lock
		Dictionary dictionaryCons = new Dictionary();
		dictionaryCons.setCode(Constant.CODE_BATCH_POST_LOCK);
		dictionaryCons.setKey(Constant.DICT_KEY_JIANSHU);
		Dictionary dictionary = dictionaryService.queryOne(dictionaryCons );
		if(Constant.FALSE.equalsIgnoreCase(dictionary.getValue())){
			//false, post articles
			//2、set locking
			Dictionary updateDictionary = new Dictionary();
			updateDictionary.setId(dictionary.getId());
			updateDictionary.setValue(Constant.TRUE);
			dictionaryService.updateByIdSelective(updateDictionary);
			//3、query not posted books
			PdfBook cons = new PdfBook();
			cons.setIsPostJianshu(false);
			List<PdfBook> list = this.queryListByWhere(cons);
			ApiAccount jianshuAccount = apiAccountService.getJianshuAccount();
			for (PdfBook pdfBook : list) {
				try {
					//4、post article
					jianshuService.post(pdfBook, jianshuAccount.getCookies());
					//5、update book posted
					PdfBook newBook = new PdfBook();
					newBook.setId(pdfBook.getId());
					newBook.setIsPostJianshu(true);
					pdfBookService.updateByIdSelective(newBook);
					Thread.sleep(2000);
				} catch (Exception e) {
					LOGGER.warn("POST articles to jianshu failed:",e);
				}
			}
			//5、release lock
			updateDictionary.setValue(Constant.FALSE);
			dictionaryService.updateByIdSelective(updateDictionary);
		}else {
			//2、locking, return
			LOGGER.warn("already posting articles to jianshu, return immediately");
		}
		
	}

}
