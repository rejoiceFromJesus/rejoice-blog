package com.rejoice.blog.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.entity.Article;
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
	ResourceLoader resourceLoader;
	
	@Autowired
	OschinaService oschinaService;
	
	public static volatile String POST_BATCH_LOCK = "false";

	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	ArticleService articleService;

	@Autowired
	PdfBookService pdfBookService;

	public String getContent(PdfBook pdfBook) {
		String content = "<p>下载地址：&nbsp;" + "<a href=\"" + pdfBook.getUrl() + "\" target=\"_blank\">"
				+ pdfBook.getFileName() + "</a></p><div class=\"image-package\">" + "<img class=\"uploaded-img\" "
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
			pdfBook.setFileName(book[0]);
			pdfBook.setTitle(book[0]+" 免费下载");
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
		// 1、locking
		VolitateVars.POST_BATCH_LOCK = Constant.TRUE;
		//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object principal2 = new User("user", "23232323", Arrays.asList(new SimpleGrantedAuthority("rool_admin")));
		new Thread(() -> {
			// 2、post articles
			postBatchToJIanshu();
			//postBatchToOschina();
			postBatchToSystem(principal2);
			// 3、release lock
			VolitateVars.POST_BATCH_LOCK = Constant.FALSE;
		}).start();
		return null;
	}
	
	@Transactional(readOnly = true)
	public void postBatchToOschina() {
		// 1、query not posted books
		PdfBook cons = new PdfBook();
		cons.setIsPostOschina(false);
		List<PdfBook> list = this.queryListByWhere(cons);
		ApiAccount oschinaAccount = apiAccountService.getOschinaAccount();
		String accessToken = null;
		try {
			String authroizedCode = oschinaService.getAuthroizedCode();
			accessToken = oschinaService.getAccessToken(authroizedCode, oschinaAccount);
		} catch (Exception e) {
			LOGGER.warn("get oschina code or token failed:",e);
		}
		if(StringUtils.isBlank(accessToken)) {
			return;
		}
		for (PdfBook pdfBook : list) {
			//2、check lock always
			if(Constant.FALSE.equalsIgnoreCase(VolitateVars.POST_BATCH_LOCK)) {
				throw new RuntimeException("exit batch post articles cause lock release");
			}
			try {
				// 3、post article
				oschinaService.oauthPost(accessToken, pdfBook, oschinaAccount);
				// 4、update book posted
				PdfBook newBook = new PdfBook();
				newBook.setId(pdfBook.getId());
				newBook.setIsPostOschina(true);
				pdfBookService.updateByIdSelective(newBook);
				Thread.sleep(2000);
			} catch (Exception e) {
				LOGGER.warn("POST articles to system failed:", e);
			}
		}
	}

	@Transactional(readOnly = true)
	private void postBatchToSystem(Object principal) {
		// 1、query not posted books
		PdfBook cons = new PdfBook();
		cons.setIsPostSystem(false);
		List<PdfBook> list = this.queryListByWhere(cons);
		for (PdfBook pdfBook : list) {
			//2、check lock always
			if(Constant.FALSE.equalsIgnoreCase(VolitateVars.POST_BATCH_LOCK)) {
				throw new RuntimeException("exit batch post articles cause lock release");
			}
			try {
				// 3、post article
				this.post(pdfBook,principal);
				// 4、update book posted
				PdfBook newBook = new PdfBook();
				newBook.setId(pdfBook.getId());
				newBook.setIsPostSystem(true);
				pdfBookService.updateByIdSelective(newBook);
				Thread.sleep(2000);
			} catch (Exception e) {
				LOGGER.warn("POST articles to system failed:", e);
			}
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
			// 1、query not posted books
			PdfBook cons = new PdfBook();
			cons.setIsPostJianshu(false);
			List<PdfBook> list = this.queryListByWhere(cons);
			ApiAccount jianshuAccount = apiAccountService.getJianshuAccount();
			jianshuService.setJianshuAccount(jianshuAccount);
			for (PdfBook pdfBook : list) {
				//2、check lock always
				if(Constant.FALSE.equalsIgnoreCase(VolitateVars.POST_BATCH_LOCK)) {
					throw new RuntimeException("exit batch post articles cause lock release");
				}
				try {
					// 3、post article
					jianshuService.post(pdfBook);
					// 4、update book posted
					PdfBook newBook = new PdfBook();
					newBook.setId(pdfBook.getId());
					newBook.setImgUrl(pdfBook.getImgUrl());
					newBook.setIsPostJianshu(true);
					pdfBookService.updateByIdSelective(newBook);
					Thread.sleep(2000);
					//5、delete img
					Resource resource = resourceLoader.getResource(uploadImagesDir+"/"+pdfBook.getImg());
					if(resource.exists()) {
						resource.getFile().delete();
					}
				} catch (Exception e) {
					LOGGER.warn("POST articles to jianshu failed:", e);
				}
			}

	}

}
