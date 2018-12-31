package com.zcj.util.coder.database;

import java.util.List;

public class Database {

	public static final String TYPE_MYSQL = "MySQL";
	public static final String TYPE_SQLSERVER = "SqlServer";
	public static final String TYPE_ORACLE = "Oracle";

	private String name;// 数据库名称
	private List<Table> tables;// 各表

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

}
