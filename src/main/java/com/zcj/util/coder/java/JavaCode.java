package com.zcj.util.coder.java;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.zcj.util.coder.java.query.QueryColumn;

public class JavaCode {

	private String packageName;// 例：com.zopen.zboot
	private String moduleName;// 例：article
	private String className;// 例：Catalog
	private String tableName;// 例：t_catalog
	private String tableCnName;// 表中文名；例：栏目
	private List<Field> fieldList;// 数据库字段
	private List<Field> allFieldList;// 数据库字段+显示字段
	private List<QueryColumn> qbuilderList;// 查询条件属性
	private Map<String, String> fieldNameCommentMap;// 字段名和注释的映射

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getTableCnName() {
		return tableCnName;
	}

	public Map<String, String> getFieldNameCommentMap() {
		return fieldNameCommentMap;
	}

	public void setFieldNameCommentMap(Map<String, String> fieldNameCommentMap) {
		this.fieldNameCommentMap = fieldNameCommentMap;
	}

	public void setTableCnName(String tableCnName) {
		this.tableCnName = tableCnName;
	}

	public List<Field> getAllFieldList() {
		return allFieldList;
	}

	public void setAllFieldList(List<Field> allFieldList) {
		this.allFieldList = allFieldList;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}

	public List<QueryColumn> getQbuilderList() {
		return qbuilderList;
	}

	public void setQbuilderList(List<QueryColumn> qbuilderList) {
		this.qbuilderList = qbuilderList;
	}

}
