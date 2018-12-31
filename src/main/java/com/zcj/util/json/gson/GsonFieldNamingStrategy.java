package com.zcj.util.json.gson;

import java.lang.reflect.Field;

import com.google.gson.FieldNamingStrategy;

/**
 * GSON 字段名称修改策略
 * 		使用.setFieldNamingStrategy(new GsonFieldNamingStrategy(new String[]{"descr"}, new String[]{"theDescr"}))
 * 
 * @author ZCJ
 * @data 2013-3-28
 */
public class GsonFieldNamingStrategy implements FieldNamingStrategy {

	private String[] jsonFieldNames;
	private String[] outFieldNames;

	public GsonFieldNamingStrategy(String[] jsonFieldNames, String[] outFieldNames) {
		this.jsonFieldNames = jsonFieldNames;
		this.outFieldNames = outFieldNames;
	}

	@Override
	public String translateName(Field f) {
		
		if (this.jsonFieldNames == null) {
			return f.getName();
		}

		for (int i = 0; i < this.jsonFieldNames.length; i++) {
			if (this.jsonFieldNames[i].equals(f.getName())) {
				return this.outFieldNames[i];
			}
		}

		return f.getName();
	}



	public String[] getJsonFieldNames() {
		return jsonFieldNames;
	}

	public String[] getOutFieldNames() {
		return outFieldNames;
	}

}
