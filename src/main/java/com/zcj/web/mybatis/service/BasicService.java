package com.zcj.web.mybatis.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BasicService<T, ID extends Serializable> {

	void insert(T entity);

	void update(T entity);

	void delete(ID id);

	void deleteByIds(Collection<ID> ids);

	void cleanTable();

	T findById(ID id);

	List<T> find(String orderBy, Map<String, Object> qbuilder, Integer size);

	List<T> findByPage(String orderBy, Map<String, Object> qbuilder);

	int getTotalRows(Map<String, Object> qbuilder);

	Map<String, Object> initQbuilder(String key, Object value);

	Map<String, Object> initQbuilder(String[] keys, Object[] values);

}