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
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.RejoiceUtil;
import com.rejoice.blog.entity.AdPosition;
import com.rejoice.blog.entity.Article;

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
	
	@Autowired
	AdPositionService adPositionService;

	public void initAdsAndCards(ModelAndView mv) {
		Article rankCons = new Article();
		rankCons.setEnable(true);
		mv.addObject("readRankList", this.queryListByPageAndOrder(rankCons, 1, 8, "read_count desc").getList());
		//ad
		//ad1
		AdPosition adCons = new AdPosition();
		adCons.setPosition("ad1");
		List<AdPosition> ad1List = adPositionService.queryListByWhere(adCons);
		mv.addObject("ad1s",ad1List);
		adCons.setPosition("ad2");
		List<AdPosition> ad2List = adPositionService.queryListByWhere(adCons);
		mv.addObject("ad2s",ad2List);
	}
	
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
	
	
	
	@Override
	public PageInfo<Article> queryListByPageAndOrder(Article t, Integer page, Integer rows, String order) {
		// 加入分页
		if(rows == null) {
			rows = 10;
		}
		PageHelper.startPage(page, rows);
		// 声明一个example
		Example example = new Example(Article.class);
		example.selectProperties("id","title","summary","readCount","commentCount","postTime","imgUrl","categoryId","author");
		if (StringUtils.isNotBlank(order)) {
			example.setOrderByClause(order);
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



	public void fillFields(Article t, Object principal) {
		User user = (User) principal;
		Document doc = Jsoup.parse(t.getContent());
		t.setPostTime(DateTime.now().toString(Constant.DATE_FORMAT_PATTERN2));
		//String noHTMLString = t.getContent().replaceAll("\\<.*?\\>", "");
		t.setSummary(StringUtils.substring(doc.text(),0,100)+"...");
		Elements img = doc.select("img");
		if(!img.isEmpty()){
			t.setImgUrl(img.first().attr("src"));
		}
		t.setAuthor(user.getUsername());
	}

}
