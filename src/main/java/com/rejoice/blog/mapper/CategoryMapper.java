package com.rejoice.blog.mapper;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;

import com.rejoice.blog.entity.Category;

public interface CategoryMapper extends Mapper<Category> {
	
	public List<Category> tree();
}
