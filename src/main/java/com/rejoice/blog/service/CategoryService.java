/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.service
 * ExtendRepayPlanService.java
 * 
 * 2017年6月9日-下午2:49:27
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rejoice.blog.entity.Category;
import com.rejoice.blog.mapper.CategoryMapper;

/**
 *
 * AccountService
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月9日 下午2:49:27
 * 
 * @version 1.0.0
 *
 */
@Service
public class CategoryService extends BaseService<Category> {
	
	@Autowired
	CategoryMapper categoryMapper;
	
	public List<Category> tree(){
		return categoryMapper.tree();
	}

}