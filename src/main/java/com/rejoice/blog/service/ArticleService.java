/**
 * 系统项目名称
 * com.rejoice.blog.service
 * ArticleService.java
 * 
 * 2017年11月8日-下午5:23:15
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.rejoice.blog.entity.Article;

/**
 *
 * ArticleService
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年11月8日 下午5:23:15
 * 
 * @version 1.0.0
 *
 */
@Service
public class ArticleService extends BaseService<Article>{
	
	@Override
	public List<Article> queryListByWhereAndOrder(Article t, String[] sorts, String[] orders) throws Exception {
		// TODO Auto-generated method stub
		List<Article> list = queryListByWhereAndOrder(t, sorts, orders);
		list.forEach(item -> {
			item.setContent(StringUtils.substring(item.getContent(), 0, 200));
		});
		return list;
	}

}
