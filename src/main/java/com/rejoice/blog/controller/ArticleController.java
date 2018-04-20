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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
		ModelAndView modelAndView = new ModelAndView("/article-detail");
		modelAndView.addObject("article",this.getService().queryByID(id));
		Comment comment = new Comment();
		comment.setArticleId(id);
		modelAndView.addObject("commentCount", commentService.queryCount(comment));
		modelAndView.addObject("comments",commentService.findArticleComments(id));
		return modelAndView;
	}

}
