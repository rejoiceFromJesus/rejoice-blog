/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.controller
 * ExtendRepayController.java
 * 
 * 2017年6月8日-下午5:38:42
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.Category;
import com.rejoice.blog.service.CategoryService;

/**
 *
 * ExtendRepayController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月8日 下午5:38:42
 * 
 * @version 1.0.0
 *
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController<Category,CategoryService>{
	@GetMapping("/parent-id/{parentId}")
	public List<Category> listByParentId(@PathVariable("parentId") Integer parentId){
		Category con = new Category();
		con.setParentId(parentId);
		return this.getService().queryListByWhere(con);
	}
	
}
