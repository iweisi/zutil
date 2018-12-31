package com.zcj.web.mybatis.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BasicMapper <T, ID extends Serializable> {

	String OBJECT = "object";
	String OBJECTS = "objects";
	String ID = "id";
	String IDS = "ids";
	String CODE = "code";
	String ORDERBY = "orderby";
	String START = "start";
	String SIZE = "size";
	String QBUILDER = "qbuilder";

	/**
	 * 保存数据
	 */
	void insert(@Param(OBJECT) T object);
	
	/**
	 * 批量保存数据
	 */
	void inserts(@Param(OBJECTS) Collection<T> objects);

	/**
	 * 修改数据
	 */
	void update(@Param(OBJECT) T object);

	/**
	 * 根据id字段删除数据
	 */
	void delete(@Param(ID) ID id);

	/**
	 * 根据id字段集合删除数据
	 */
	void deleteByIds(@Param(IDS) Collection<ID> ids);
	
	/**
	 * 清空表
	 */
	void cleanTable();

	/**
	 * 根据id字段查询数据
	 */
	T findById(@Param(ID) ID id);

	/**
	 * 查询表中的记录
	 */
	List<T> find(@Param(QBUILDER) Map<String, Object> qbuilder, @Param(START) Integer start, @Param(SIZE) Integer size, @Param(ORDERBY) String orderby);

	/**
	 * 查询总记录数
	 */
	int getTotalRows(@Param(QBUILDER) Map<String, Object> qbuilder);

}