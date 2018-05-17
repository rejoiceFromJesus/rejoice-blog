package com.rejoice.blog.controller;

import javax.servlet.http.HttpServletRequest;

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
@Controller("/")
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
	public ModelAndView findPage(@PathVariable("pageNum")  Integer pageNum){
		PageInfo<Article> pageInfo = articleService.queryListByPageAndOrder(null, pageNum, null, "post_time desc");
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("list",pageInfo.getList());
		mv.addObject("curr",pageNum);
		mv.addObject("count", pageInfo.getTotal());
		return mv;
	}
	
	
	@GetMapping("/")
	public ModelAndView toHome(){
		return this.findPage(1);
	}
	
	@GetMapping("/admin")
	public String toAdminLogin(){
		return "admin/admin-login";
	}
}
