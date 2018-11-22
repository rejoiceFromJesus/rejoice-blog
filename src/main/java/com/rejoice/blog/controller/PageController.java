package com.rejoice.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rejoice.blog.common.bean.LayuiResult;
import com.rejoice.blog.common.util.JsonUtil;
import com.rejoice.blog.entity.Article;
import com.rejoice.blog.service.ArticleService;

/**
 *
 * PageController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月10日 下午5:13:13
 * 
 * @version 1.0.0
 *
 */
@RequestMapping
@Controller
public class PageController {

	@Autowired
	ArticleService articleService;
	
	@RequestMapping(value="/page/**/*.html")
	public String toPage(HttpServletRequest request){
		  // Don't repeat a pattern
	    String pattern = (String)  
	        request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);  

	    String searchTerm = new AntPathMatcher().extractPathWithinPattern(pattern, 
	        request.getServletPath());
		return searchTerm.substring(0,searchTerm.length()-5);
	}
	
	@GetMapping("/page/{pageNum}")
	public ModelAndView findPage(@PathVariable("pageNum")  Integer pageNum,String keyword){
		ModelAndView mv = new ModelAndView("index");
		Article cons = new Article();
		cons.setEnable(true);
		StringBuilder p = new StringBuilder();
		if(StringUtils.isNotBlank(keyword)) {
			cons.setLikes("title");
			cons.setTitle(keyword);
			mv.addObject("keyword",keyword);
			p.append("keyword="+keyword+"&");
			
		}
		PageInfo<Article> pageInfo = articleService.queryListByPageAndOrder(cons, pageNum, 10, "post_time desc");
	
		mv.addObject("list",pageInfo.getList());
		mv.addObject("curr",pageNum);
		mv.addObject("count", pageInfo.getTotal());
		mv.addObject("totalPage", pageInfo.getPages());
		mv.addObject("p","?"+p.toString());
		Article rankCons = new Article();
		rankCons.setEnable(true);
		mv.addObject("readRankList", articleService.queryListByPageAndOrder(rankCons, 1, 10, "read_count desc").getList());
		return mv;
	}
	
	
	@GetMapping("/")
	public ModelAndView toHome(){
		return this.findPage(1,null);
	}
}
