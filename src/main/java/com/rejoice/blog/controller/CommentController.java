/**
 * 系统项目名称
 * com.rejoice.blog.controller
 * CommentController.java
 * 
 * 2017年11月14日-下午1:03:44
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.Comment;
import com.rejoice.blog.service.CommentService;

/**
 *
 * CommentController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月14日 下午1:03:44
 * 
 * @version 1.0.0
 *
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController<Comment, CommentService> {

	@RequestMapping("/article/{articleId}/count")
	public Integer count(@PathVariable("articleId") Long articleId){
		Comment comment = new Comment();
		comment.setArticleId(articleId);
		return this.getService().queryCount(comment);
	}
}
