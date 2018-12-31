package com.zcj.util.jdbc;

/**
 * 表信息
 * 
 * @author zouchongjin@sina.com
 * @data 2016年8月4日
 */
public class TableInfo {

	private String tableName;// 表名
	private String primaryFieldName;// 主键字段名称

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryFieldName() {
		return primaryFieldName;
	}

	public void setPrimaryFieldName(String primaryFieldName) {
		this.primaryFieldName = primaryFieldName;
	}

}
