package com.zcj.util.json.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * GSON 字段过滤策略
 * 		使用：.setExclusionStrategies(new GsonExclusionStrategy(new String[]{"descr","mapurl"}, new Class<?>[]{Date.class}))
 * 
 * @author ZCJ
 * @data 2013-3-28
 */
public class GsonExclusionStrategy implements ExclusionStrategy {
	
	private String[] excludeFields;
	private Class<?>[] excludeClasses;

	public GsonExclusionStrategy(String[] excludeFields, Class<?>[] excludeClasses) {
		this.excludeFields = excludeFields;
		this.excludeClasses = excludeClasses;
	}

	public boolean shouldSkipClass(Class<?> clazz) {
		if (this.excludeClasses == null) {
			return false;
		}

		for (Class<?> excludeClass : excludeClasses) {
			if (excludeClass.getName().equals(clazz.getName())) {
				return true;
			}
		}

		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		if (this.excludeFields == null) {
			return false;
		}

		for (String field : this.excludeFields) {
			if (field.equals(f.getName())) {
				return true;
			}
		}

		return false;
	}

	public final String[] getExcludeFields() {
		return excludeFields;
	}

	public final Class<?>[] getExcludeClasses() {
		return excludeClasses;
	}
}