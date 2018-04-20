/**
 * 系统项目名称
 * com.rejoice.blog.service
 * CommentService.java
 * 
 * 2017年11月14日-下午1:03:15
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import com.rejoice.blog.entity.Comment;

/**
 *
 * CommentService
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月14日 下午1:03:15
 * 
 * @version 1.0.0
 *
 */
@Service
public class CommentService extends BaseService<Comment> {

	
	@Transactional(readOnly=true)
	public List<Comment> findArticleComments(Long articleId){
		Mapper<Comment> mapper = this.getMapper();
		Example example = new Example(Comment.class);
		example.createCriteria()
		.andCondition("article_id =", articleId)
		.andCondition("comment_id is", null);
		return mapper.selectByExample(example);
	}
}
