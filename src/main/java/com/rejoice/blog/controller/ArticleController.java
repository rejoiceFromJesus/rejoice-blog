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

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.Article;
import com.rejoice.blog.entity.Comment;
import com.rejoice.blog.service.ArticleService;
import com.rejoice.blog.service.CommentService;

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
	
	@Autowired
	CommentService commentService;
	
	@GetMapping("/{id}.html")
	public ModelAndView detail(@PathVariable("id") Long id){
		Article article = this.getService().queryByID(id);
		ModelAndView modelAndView = new ModelAndView("article-detail");
		modelAndView.addObject("article",article);
		Comment comment = new Comment();
		comment.setArticleId(id);
		modelAndView.addObject("commentCount", commentService.queryCount(comment));
		modelAndView.addObject("comments",commentService.findArticleComments(id));
		//update readCount
		article.setReadCount(article.getReadCount()+1);
		this.getService().updateByIdSelective(article);
		return modelAndView;
	}
	
	
	@PostMapping("/save")
	public void saveArticle(@RequestBody Article t,HttpSession session) throws Exception {
		Document doc = Jsoup.parse(t.getContent());
		t.setPostTime(DateTime.now().toString(Constant.DATE_FORMAT_PATTERN2));
		//String noHTMLString = t.getContent().replaceAll("\\<.*?\\>", "");
		t.setSummary(StringUtils.substring(doc.text(),0,100)+"...");
		Elements img = doc.select("img");
		if(!img.isEmpty()){
			t.setImgUrl(img.first().attr("src"));
		}
	
		this.getService().saveSelective(t);
	}
	
	@PutMapping("/save")
	public void updateArticle(@RequestBody Article t,HttpSession session) throws Exception {
		Document doc = Jsoup.parse(t.getContent());
		t.setPostTime(DateTime.now().toString(Constant.DATE_FORMAT_PATTERN2));
		//String noHTMLString = t.getContent().replaceAll("\\<.*?\\>", "");
		t.setSummary(StringUtils.substring(doc.text(),0,100)+"...");
		Elements img = doc.select("img");
		if(!img.isEmpty()){
			t.setImgUrl(img.first().attr("src"));
		}
		this.getService().updateByIdSelective(t);
	}

}
