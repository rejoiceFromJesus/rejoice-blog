/**
 * 系统项目名称
 * com.rejoice.blog.controller
 * ArticleController.java
 * 
 * 2017年11月8日-下午5:24:01
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rejoice.blog.bean.bo.ArticleExtend;
import com.rejoice.blog.bean.http.jianshu.NotesAddOutput;
import com.rejoice.blog.bean.http.jianshu.NotesUpdateInput;
import com.rejoice.blog.common.bean.LayuiResult;
import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.entity.Article;
import com.rejoice.blog.entity.Comment;
import com.rejoice.blog.service.ArticleService;
import com.rejoice.blog.service.CategoryService;
import com.rejoice.blog.service.CommentService;
import com.rejoice.blog.service.JianshuService;

/**
 *
 * ArticleController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月8日 下午5:24:01
 * 
 * @version 1.0.0
 *
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController<Article, ArticleService> {

	private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	CommentService commentService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	JianshuService jianshuService;
	
	@GetMapping("/{id}.html")
	public ModelAndView detail(@PathVariable("id") Long id){
		Article article = this.getService().queryByID(id);
		ModelAndView modelAndView = new ModelAndView("article-detail");
		modelAndView.addObject("article",article);
		Comment comment = new Comment();
		comment.setArticleId(id);
		modelAndView.addObject("commentCount", commentService.queryCount(comment));
		modelAndView.addObject("comments",commentService.findArticleComments(id));
		this.getService().initAdsAndCards(modelAndView);
		//update readCount
		Article update = new Article();
		update.setReadCount(article.getReadCount()+1);
		update.setId(id);
		this.getService().updateByIdSelective(update);
		return modelAndView;
	}
	
	
	
	
	@PostMapping("/save")
	public void saveArticle(@RequestBody Article t) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.getService().fillFields(t,principal);
		try {
			//post to jianshu
			NotesAddOutput notesAddOutput = jianshuService.postArticle(t.getContent()
					, t.getTitle()
					, JianshuService.NOTEBOOK_ID_IT
					, JianshuService.COLLECTION_ID_IT);
			ArticleExtend articleExtend = new ArticleExtend();
			articleExtend.setJianshuId(notesAddOutput.getId());
			articleExtend.setAutosave_control(1L);
			t.setExtend(JsonUtil.toJson(articleExtend));
		} catch (Exception e) {
			LOG.warn("post to jianshu failed:",e);
		}
		this.getService().saveSelective(t);
	}
	
	@PutMapping("/save")
	public void updateArticle(@RequestBody Article t, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
		//1、post to  system
		this.getService().fillFields(t,userDetails);
		String extend = this.getService().queryByID(t.getId()).getExtend();
		ArticleExtend articleExtend = null;
		try {
			 articleExtend = JsonUtil.toBean(extend, ArticleExtend.class);
			 articleExtend.setAutosave_control(articleExtend.getAutosave_control()+1);
			 t.setExtend(JsonUtil.toJson(articleExtend));
		} catch (Exception e) {
			
		}
		this.getService().updateByIdSelective(t);
		//2、post to jianshu
		if(articleExtend != null && StringUtils.isNotBlank(articleExtend.getJianshuId())) {
			try {
				jianshuService.updateArticle(t.getContent()
						, t.getTitle()
						, articleExtend.getJianshuId()
						, articleExtend.getAutosave_control());
			} catch (Exception e) {
				LOG.warn("post update to jianshu failed:",e);
			}
		}
		
	}

}
