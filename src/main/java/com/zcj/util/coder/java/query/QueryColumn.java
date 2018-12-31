package com.zcj.util.coder.java.query;

/**
 * 查询语句的属性
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月23日
 */
public class QueryColumn {

	private String fieldName;// 当前字段名称
	private String fieldType;// 当前字段类型，例：String、Integer、Date、Long、Float、BigDecimal

	private String oper;// 查询操作符，例：=、like、in、time、between
	private boolean listQuery = false;// 是否作为list页面的查询条件（默认否）

	private String srcTableName;// 对应哪张表
	private String srcFieldName;// 对应表中的哪个字段

	public QueryColumn() {
		super();
	}

	public QueryColumn(String fieldName, String fieldType, String oper, boolean listQuery, String srcTableName,
			String srcFieldName) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.oper = oper;
		this.listQuery = listQuery;
		this.srcTableName = srcTableName;
		this.srcFieldName = srcFieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getSrcTableName() {
		return srcTableName;
	}

	public void setSrcTableName(String srcTableName) {
		this.srcTableName = srcTableName;
	}

	public String getSrcFieldName() {
		return srcFieldName;
	}

	public void setSrcFieldName(String srcFieldName) {
		this.srcFieldName = srcFieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isListQuery() {
		return listQuery;
	}

	public void setListQuery(boolean listQuery) {
		this.listQuery = listQuery;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

}
