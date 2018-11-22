/**
 * 系统项目名称
 * cn.jrjzx.supervision.smallloan.controller
 * BaseController.java
 * 
 * 2017年6月9日-下午2:28:19
 *  2017金融街在线公司-版权所有
 *
 */
package com.rejoice.blog.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.rejoice.blog.common.bean.EasyUIResult;
import com.rejoice.blog.common.bean.LayuiResult;
import com.rejoice.blog.common.bean.Result;
import com.rejoice.blog.common.exception.ResourceNotFoundException;
import com.rejoice.blog.service.BaseService;

/**
 *
 * BaseController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月9日 下午2:28:19
 * 
 * @version 1.0.0
 *
 */ 
public class BaseController<T,S> { 

	@Autowired
	BaseService<T> baseService;
	
	@SuppressWarnings("unused")
	private Class<T> clazz;
	
	
	@SuppressWarnings("unchecked")
	protected S getService(){
		return (S) baseService;
	}
	
  @SuppressWarnings("unchecked")
    public BaseController() {
        super();
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType ptype = (ParameterizedType) type;
        this.clazz = (Class<T>) ptype.getActualTypeArguments()[0];
    }

	
	@GetMapping("/all")
	public ResponseEntity<List<T>> findAll(T t){
		return ResponseEntity.ok(baseService.queryListByWhere(t));
	}
	@GetMapping("/count")
	public ResponseEntity<Integer> findCount(T t){
		return ResponseEntity.ok(baseService.queryCount(t));
	}
	@GetMapping("/page") 
	public LayuiResult findByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "30") Integer rows,String[] sort,String[] order, T t) throws Exception{
		PageInfo<T> pageInfo = baseService.queryListByPageAndOrder(t, page, rows,sort,order);
		LayuiResult layuiResult = new LayuiResult(pageInfo.getTotal(), pageInfo.getList());
		return layuiResult;
	}
	@PutMapping
	public void update(@RequestBody T t) throws Exception{
		baseService.updateByIdSelective(t);
	}     
	
	@PostMapping 
	public Result<Void> save(@RequestBody T t) throws Exception{
		baseService.saveSelective(t);
		return Result.success(null);
	}
	@DeleteMapping("/{ids}")
	public void delete(@PathVariable("ids") String ids) throws Exception{
		List<String>  idsArray = Arrays.asList(ids.split(","));
		baseService.deleteByIds(idsArray);
	}
	@GetMapping("/{id}")
	public T getById(@PathVariable("id") Serializable id){
		T t = baseService.queryByID(id);
		if(t == null){
			throw new ResourceNotFoundException();
		}
		return t;
	} 
}
