package com.rejoice.blog.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.Article;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.entity.PdfBook;

@Service
public class PdfBookService extends BaseService<PdfBook> {

	@Autowired
	public ApiAccountService apiAccountService;
	
	@Value("${blog.upload.images.dir}")
	public String uploadImagesDir;

	@Autowired
	JianshuService jianshuService;

	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	ArticleService articleService;

	@Autowired
	PdfBookService pdfBookService;

	public String getContent(PdfBook pdfBook) {
		String content = "<p>下载地址：&nbsp;" + "<a href=\"" + pdfBook.getUrl() + "\" target=\"_blank\">"
				+ pdfBook.getTitle() + " 免费下载</a></p><div class=\"image-package\">" + "<img class=\"uploaded-img\" "
				+ "src=\""+pdfBook.getImgUrl()
				+ "\" width=\"auto\" height=\"auto\">" + "<br><div class=\"image-caption\"></div></div>";
		return content;
	}

	public void batchImport(String books) {
		String[] bookStrArray = books.split("\n");
		String today = DateTime.now().toString(Constant.DATE_FORMAT_PATTERN1);
		for (String bookStr : bookStrArray) {
			String[] book = bookStr.split(": https://");
			PdfBook pdfBook = new PdfBook();
			pdfBook.setTitle(book[0]);
			pdfBook.setImg(book[0] + ".jpg");
			pdfBook.setImgUrl(uploadImagesDir+"/" + today + "/" + pdfBook.getImg());
			pdfBook.setUrl("https://" + book[1]);
			pdfBook.setIsPostJianshu(false);
			pdfBook.setIsPostOschina(false);
			pdfBook.setIsPostSystem(false);
			this.saveSelective(pdfBook);
		}
	}

	public String batchPost() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		new Thread(() -> {
			postBatchToJIanshu();
			postBatchToSystem(principal);
		}).start();
		return null;
	}

	@Transactional(readOnly = true)
	private void postBatchToSystem(Object principal) {
		// 1、check lock
		Dictionary dictionaryCons = new Dictionary();
		dictionaryCons.setCode(Constant.CODE_BATCH_POST_LOCK);
		dictionaryCons.setKey(Constant.DICT_KEY_SYSTEM);
		Dictionary dictionary = dictionaryService.queryOne(dictionaryCons);
		if (Constant.FALSE.equalsIgnoreCase(dictionary.getValue())) {
			// false, post articles
			// 2、set locking
			Dictionary updateDictionary = new Dictionary();
			updateDictionary.setId(dictionary.getId());
			updateDictionary.setValue(Constant.TRUE);
			dictionaryService.updateByIdSelective(updateDictionary);
			// 3、query not posted books
			PdfBook cons = new PdfBook();
			cons.setIsPostSystem(false);
			List<PdfBook> list = this.queryListByWhere(cons);
			for (PdfBook pdfBook : list) {
				try {
					// 4、post article
					this.post(pdfBook,principal);
					// 5、update book posted
					PdfBook newBook = new PdfBook();
					newBook.setId(pdfBook.getId());
					newBook.setIsPostSystem(true);
					pdfBookService.updateByIdSelective(newBook);
					Thread.sleep(50);
				} catch (Exception e) {
					LOGGER.warn("POST articles to system failed:", e);
				}
			}
			// 5、release lock
			updateDictionary.setValue(Constant.FALSE);
			dictionaryService.updateByIdSelective(updateDictionary);
		} else {
			// 2、locking, return
			LOGGER.warn("already posting articles to system, return immediately");
		}

	}

	private void post(PdfBook pdfBook, Object principal) {
		Article article = new Article();
		article.setTitle(pdfBook.getTitle());
		article.setContent(this.getContent(pdfBook));
		articleService.fillFields(article, principal);
		articleService.saveSelective(article);
	}


	@Transactional(readOnly = true)
	private void postBatchToJIanshu() {

		// 1、check lock
		Dictionary dictionaryCons = new Dictionary();
		dictionaryCons.setCode(Constant.CODE_BATCH_POST_LOCK);
		dictionaryCons.setKey(Constant.DICT_KEY_JIANSHU);
		Dictionary dictionary = dictionaryService.queryOne(dictionaryCons);
		if (Constant.FALSE.equalsIgnoreCase(dictionary.getValue())) {
			// false, post articles
			// 2、set locking
			Dictionary updateDictionary = new Dictionary();
			updateDictionary.setId(dictionary.getId());
			updateDictionary.setValue(Constant.TRUE);
			dictionaryService.updateByIdSelective(updateDictionary);
			// 3、query not posted books
			PdfBook cons = new PdfBook();
			cons.setIsPostJianshu(false);
			List<PdfBook> list = this.queryListByWhere(cons);
			ApiAccount jianshuAccount = apiAccountService.getJianshuAccount();
			for (PdfBook pdfBook : list) {
				try {
					// 4、post article
					jianshuService.post(pdfBook, jianshuAccount.getCookies());
					// 5、update book posted
					PdfBook newBook = new PdfBook();
					newBook.setId(pdfBook.getId());
					newBook.setImgUrl(pdfBook.getImgUrl());
					newBook.setIsPostJianshu(true);
					pdfBookService.updateByIdSelective(newBook);
					Thread.sleep(2000);
				} catch (Exception e) {
					LOGGER.warn("POST articles to jianshu failed:", e);
				}
			}
			// 5、release lock
			updateDictionary.setValue(Constant.FALSE);
			dictionaryService.updateByIdSelective(updateDictionary);
		} else {
			// 2、locking, return
			LOGGER.warn("already posting articles to jianshu, return immediately");
		}

	}

}
