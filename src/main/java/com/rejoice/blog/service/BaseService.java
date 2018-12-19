package com.rejoice.blog.service;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rejoice.blog.common.exception.InternalServerException;
import com.rejoice.blog.common.util.RejoiceUtil;

@Transactional
public abstract class BaseService<T> {
	
	
	public static Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

	/**
	 * 
	 * between(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param property
	 * @param criteria
	 * @param left
	 * @param right
	 * void
	 */
	protected void between(String property, Criteria criteria, Object left,
			Object right) {
		if (RejoiceUtil.isBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andLessThanOrEqualTo(property, right);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isBlank(right)) {
			criteria.andGreaterThanOrEqualTo(property, left);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andBetween(property, left, right);
		}
	}
	
	protected void between(String property, Criteria criteria, Object left, Boolean leftInclude,
			Object right, Boolean rightInclude) {
		if (RejoiceUtil.isBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andLessThanOrEqualTo(property, right);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isBlank(right)) {
			criteria.andGreaterThan(property, left);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andLessThanOrEqualTo(property, right);
			criteria.andGreaterThan(property, left);
		}
	}

	/**
	 * 
	 * between(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param property
	 * @param criteria
	 * @param left
	 * @param right
	 * void
	 */
	protected void betweenCondition(String condition, Criteria criteria,
			Object left, Object right) {
		if (RejoiceUtil.isBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andCondition(condition + "<=" + right);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isBlank(right)) {
			criteria.andCondition(condition + ">=" + left);
		}
		if (RejoiceUtil.isNotBlank(left) && RejoiceUtil.isNotBlank(right)) {
			criteria.andCondition(condition + "<=" + right);
			criteria.andCondition(condition + ">=" + left);
		}
	}

	@Autowired
	private Mapper<T> mapper;

	public Mapper<T> getMapper() {
		return this.mapper;
	}

	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseService() {
		super();
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType ptype = (ParameterizedType) type;
		this.clazz = (Class<T>) ptype.getActualTypeArguments()[0];
	}

	/**
	 * 
	 * 
	 * @param t conditions
	 * @throws Exception
	 * void
	 */
	protected void saveOrUpdateSelective(T t){
		try {
			Field idField = ReflectionUtils.findField(t.getClass(), "id");
			idField.setAccessible(true);
			Object value = idField.get(t);
			if (value == null) {
				this.saveSelective(t);
			} else {
				this.updateByIdSelective(t);
			}	
		}
		
	    catch (Exception e) {
	    	LOGGER.error("saveOrUpdate ["+this.clazz.getName()+"] failed",e);
	    	throw new InternalServerException("保存或更新 ["+this.clazz.getName()+"] 失败！",e);
	    }
	}

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public T queryByID(Serializable id) {
		if(id == null){
			return null;
		}
		// 设置条件
		Example example = new Example(clazz);
		example.createCriteria().andEqualTo("id", id);
		/*T selectByPrimaryKey = this.getMapper().selectByPrimaryKey(id);*/
		List<T> list = this.getMapper().selectByExample(example);
		return list.size() > 0 ? list.get(0) : null;
		/*return getMapper().selectByPrimaryKey(id);*/
	}
	/**
	 * updateByConditions(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param recode
	 * @param example
	 * void
	*/
	public void updateByConditions(T record,
			T conditions) {
		Example example = new Example(clazz);
		RejoiceUtil.emptyToNull(conditions);
		example.createCriteria().andEqualTo(conditions);
		this.getMapper().updateByExampleSelective(record, example);
	}

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> queryAll() {
		Example example = new Example(this.clazz);
		return this.getMapper().selectByExample(example);
		/*        return this.getMapper().select(null);
		*/}

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> queryListByWhereAndOrder(T t, String[] sorts, String[] orders)
			throws Exception {
		// 声明一个example
		Example example = new Example(this.clazz);
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
			return this.getMapper().selectByExample(example);
		}
		example.createCriteria().andEqualTo(t);
		return this.getMapper().selectByExample(example);
	}

	/**
	 * 根据条件查询
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> queryListByWhere(T t) {
		Example example = new Example(this.clazz);
		Criteria criteria = example.createCriteria();
		equalOrLikeOrIn(t, criteria);
		return this.getMapper().selectByExample(example);
	}
	
	/**
	 * 
	 * updateSelective, update all not null properties in entity by conditions's not null properties (support equal、like、in)
	 * @param entity 
	 * @param conditions
	 * @return
	 * int
	 */
	@Transactional
	public int updateSelective(T entity,T conditions) {
		Example example = new Example(this.clazz);
		Criteria criteria = example.createCriteria();
		equalOrLikeOrIn(conditions, criteria);
		return this.getMapper().updateByExampleSelective(entity, example);
	}

	/**
	 * 查询数据总条数
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer queryCount(T t) {
		return this.getMapper().selectCount(t);
	}

	/**
	 * 根据条件分页查询
	 * 
	 * @param t
	 * @param page
	 * @param rows
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> queryPageByWhere(T t, Integer page, Integer rows) {
		// 第一个参数是起始页，第二个参数是，页面显示的数据条数
		PageHelper.startPage(page, rows);
		List<T> list = this.getMapper().select(t);
		return new PageInfo<T>(list);
	}

	/**
	 * 根据条件查询一条数据
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public T queryOne(T t) {
		return this.getMapper().selectOne(t);
	}

	/**
	 * 保存
	 * 
	 * @param t
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public void save(T t) throws Exception {
		this.getMapper().insert(t);
	}
	
	/**
	 * 保存
	 * 
	 * @param t
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public void saveBatchSelective(List<T> list){
		for (T t : list) {
			this.getMapper().insertSelective(t);
		}
	}

	/**
	 * 保存(忽略空字段）
	 * 
	 * @param t
	 */
	public void saveSelective(T t){
		this.getMapper().insertSelective(t);
	}

	/**
	 * 更新
	 * 
	 * @param t
	 */
	public void updateById(T t){
		this.getMapper().updateByPrimaryKey(t);
	}

	/**
	 * 更新（忽略空字段）
	 * 
	 * @param t
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public void updateByIdSelective(T t){
		this.getMapper().updateByPrimaryKeySelective(t);
	}

	/*  *//** 
			* 根据id删除
			* 
			* @param id
			*/
	/*
	public void deleteById(Serializable id) {
	 this.getMapper().deleteByPrimaryKey(id);
	}*/

	/**
	 * 根据ids批量删除,delete physically on harddisk
	 * 
	 * @param list
	 */
	public void deleteByIds(List<String> list) {
		// 设置条件
		Example example = new Example(clazz);
		example.createCriteria().andIn("id", list);
		// 根据条件删除
		this.getMapper().deleteByExample(example);
	}

	/**
	 * 分页查询数据，排序
	 * 
	 * @param t
	 * @param page
	 * @param rows
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> queryListByPageAndOrder(T t, Integer page, Integer rows,
			String order){
		if(rows == null) {
			rows = 20;
		}
		// 加入分页
		PageHelper.startPage(page, rows);

		// 声明一个example
		Example example = new Example(this.clazz);
		if (StringUtils.isNotBlank(order)) {
			example.setOrderByClause(order);
		}

		// 如果条件为null，直接返回
		if (t == null) {
			List<T> list = this.getMapper().selectByExample(example);
			return new PageInfo<T>(list);
		}
		// 声明条件
		Criteria createCriteria = example.createCriteria();
		equalOrLikeOrIn(t, createCriteria);
		List<T> list = this.getMapper().selectByExample(example);
		return new PageInfo<T>(list);
	}

	/**
	 * 分页查询数据，排序
	 * 
	 * @param t
	 * @param page
	 * @param rows
	 * @param order
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> queryListByPageAndOrder(T t, Integer page, Integer rows,
			String[] sorts, String[] orders){
		// 加入分页
		PageHelper.startPage(page, rows);
		// 声明一个example
		Example example = new Example(this.clazz);
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
			List<T> list = this.getMapper().selectByExample(example);
			return new PageInfo<T>(list);
		}
		
		// 声明条件
		Criteria createCriteria = example.createCriteria();
		equalOrLikeOrIn(t, createCriteria);
				
		/* // 获取t的字段
		 Field[] fields = t.getClass().getDeclaredFields();
		 for (Field field : fields) {
		     // 设置为true，可以获取声明的私有字段的值
		     field.setAccessible(true);
		     if (field.get(t) != null) {
		         // 非空的字段的值，加入到条件中
		         createCriteria.andEqualTo(field.getName(), field.get(t));
		     }
		 }
		 
		 Class<?> pclazz = t.getClass().getSuperclass();
		 fields = pclazz.getDeclaredFields();
		 for (Field field : fields) {
		     // 设置为true，可以获取声明的私有字段的值
		     field.setAccessible(true);
		     if (field.get(t) != null) {
		         // 非空的字段的值，加入到条件中
		         createCriteria.andEqualTo(field.getName(), field.get(t));
		     }
		 }*/
		
		
		List<T> list = this.getMapper().selectByExample(example);
		return new PageInfo<T>(list);
	}

	protected void equalOrLikeOrIn(T t, Criteria criteria) {
		//handle like query
		likeConditions(t,criteria);
		//in query
		inConditions(t,criteria);
		//equal by all not null fields;
		RejoiceUtil.emptyToNull(t);
		criteria.andEqualTo(t);
	}
	/**
	 * inConditions(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param t
	 * @param criteria
	 * void
	*/
	private void inConditions(T t, Criteria criteria) {
		try {
			
			Field ins = ReflectionUtils.findField(t.getClass(), "ins");
			if(null == ins) return;// no such properties
			ins.setAccessible(true);
			Object object = ins.get(t);
			if (null != object) {
				String insStr = object.toString();
				String[] inConditions = insStr.split(",");
				for (String inCondition : inConditions) {
					Field inField = ReflectionUtils.findField(t.getClass(), inCondition);
					inField.setAccessible(true);
					Object value = inField.get(t);
					if(RejoiceUtil.isNotBlank(value)){
						//add in condition
						criteria.andIn(inCondition.substring(0,inCondition.length()-1),Arrays.asList(value.toString().split(",")));
						//set inField's value to null to be excluded in the next equal quary;
						inField.set(t, null);
					}
					
				}
			}
		} catch (Exception e) {
			throw new InternalServerException(t.getClass().getName()+".ins can not be accessed");
		}
		
	}

	/**
	 * likeConditions(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param t
	 * @param createCriteria
	 * void
	*/
	protected void likeConditions(T t, Criteria criteria) {
		try {
			
			Field likes = ReflectionUtils.findField(t.getClass(), "likes");
			if(null == likes) return;// no such properties
			likes.setAccessible(true);
			Object object = likes.get(t);
			if (null != object) {
				String likesStr = object.toString();
				String[] likeConditions = likesStr.split(",");
				for (String likeCondition : likeConditions) {
					Field likeField = ReflectionUtils.findField(t.getClass(), likeCondition);
					likeField.setAccessible(true);
					Object value = likeField.get(t);
					if(RejoiceUtil.isNotBlank(value)){
						//add like condition
						criteria.andLike(likeCondition, "%"+value.toString()+"%");
						//set likeField's value to null to be excluded in the next equal quary;
						likeField.set(t, null);
					}
					
				}
			}
		} catch (Exception e) {
			throw new InternalServerException(t.getClass().getName()+".likes can not be accessed");
		}
	}



}