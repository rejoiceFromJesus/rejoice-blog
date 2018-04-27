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

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.Article;

import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

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
	public PageInfo<Article> queryListByPageAndOrder(Article t, Integer page, Integer rows, String[] sorts,
			String[] orders) throws Exception {
		// 加入分页
				PageHelper.startPage(page, rows);
				// 声明一个example
				Example example = new Example(Article.class);
				example.selectProperties("id","title","summary","readCount","commentCount","postTime","imgUrl","categoryId","author");
				if (RejoiceUtil.isNotBlank(sorts) && RejoiceUtil.isNotBlank(orders)) {
					StringBuilder sortSB = new StringBuilder();
					for (int i = 0; i < sorts.length; i++) {
						sortSB.append(StringUtil.camelhumpToUnderline(sorts[i]))
								.append(" ").append(orders[i]).append(",");
					}
					example.setOrderByClause(sortSB.substring(0, sortSB.length() - 1));
				}

				// 如果条件为null，直接返回
				if (t == null) {
					List<Article> list = this.getMapper().selectByExample(example);
					return new PageInfo<Article>(list);
				}
				
				// 声明条件
				Criteria createCriteria = example.createCriteria();
				equalOrLikeOrIn(t, createCriteria);
				
				
				List<Article> list = this.getMapper().selectByExample(example);
				return new PageInfo<Article>(list);
	}
	

}
