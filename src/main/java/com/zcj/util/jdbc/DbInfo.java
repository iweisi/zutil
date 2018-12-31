package com.zcj.util.jdbc;

import java.util.List;

import javax.sql.DataSource;

/**
 * 数据库信息
 * 
 * @author zouchongjin@sina.com
 * @data 2016年8月4日
 */
public class DbInfo {

	public static final String TYPE_MYSQL = "mysql";
	public static final String TYPE_SQLSERVER = "sqlserver";
	public static final String TYPE_ORACLE = "oracle";

	private DataSource dataSource;// 数据源
	private String type;// 数据库类型
	private String dbName;// 数据库名称
	private List<TableInfo> tables;// 表集合

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public List<TableInfo> getTables() {
		return tables;
	}

	public void setTables(List<TableInfo> tables) {
		this.tables = tables;
	}

}
