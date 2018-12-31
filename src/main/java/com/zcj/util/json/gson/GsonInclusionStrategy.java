package com.zcj.util.json.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * GSON 字段包含策略
 * <p>
 * 例：.setExclusionStrategies(new GsonInclusionStrategy(new
 * String[]{"name","title"}))
 * 
 * @author zouchongjin@sina.com
 * @data 2017年3月14日
 */
public class GsonInclusionStrategy implements ExclusionStrategy {

	private String[] excludeFields;

	public GsonInclusionStrategy(String[] excludeFields) {
		this.excludeFields = excludeFields;
	}

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		if (this.excludeFields == null) {
			return true;
		}

		for (String field : this.excludeFields) {
			if (field.equals(f.getName())) {
				return false;
			}
		}

		return true;
	}

	public final String[] getExcludeFields() {
		return excludeFields;
	}

}