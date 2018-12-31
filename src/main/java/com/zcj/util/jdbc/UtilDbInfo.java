package com.zcj.util.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilDbInfo {
	
	private static final Logger logger = LoggerFactory.getLogger(UtilDbInfo.class);

	public static DbInfo getDbInfo(Connection con) throws SQLException {
		DbInfo dbInfo = new DbInfo();
		dbInfo.setDbName(con.getCatalog());
		DatabaseMetaData meta = con.getMetaData();
		String showTablesSql = null;
		if (meta.getDatabaseProductName().equalsIgnoreCase("mysql")) {
			dbInfo.setType(DbInfo.TYPE_MYSQL);
			showTablesSql = "show tables";
		} else if (meta.getDatabaseProductName().equalsIgnoreCase("oracle")) {
			dbInfo.setType(DbInfo.TYPE_ORACLE);
			showTablesSql = "select * from user_tables";
		}
		Statement stmt = null;
		ResultSet rs = null;// 查询所有表
		ResultSet rs1 = null;// 查询表所有字段
		List<String> tables = new ArrayList<String>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(showTablesSql);
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
			if (null != rs) {
				rs.close();
				rs = null;
			}
			List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
			for (String tableName : tables) {
				TableInfo tableInfo = new TableInfo();
				tableInfo.setTableName(tableName);
				if (DbInfo.TYPE_MYSQL.equals(dbInfo.getType())) {
					String descFieldSql = String.format("desc `%s`", tableName);
					rs1 = stmt.executeQuery(descFieldSql);
					while (rs1.next()) {
						String key = rs1.getString("KEY");
						if (!key.equalsIgnoreCase("pri"))
							continue;
						tableInfo.setPrimaryFieldName(rs1.getString("Field"));
						break;
					}
				}
				if (null != rs1) {
					rs1.close();
					rs1 = null;
				}
				tableInfoList.add(tableInfo);
			}
			dbInfo.setTables(tableInfoList);
		} finally {
			if (null != rs1)
				rs1.close();
			if (null != rs)
				rs.close();
			if (null != stmt)
				stmt.close();
		}
		return dbInfo;
	}

	public static DbInfo getDbInfo(DataSource ds) throws SQLException {
		DbInfo dbInfo;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			dbInfo = getDbInfo(conn);
			dbInfo.setDataSource(ds);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return dbInfo;
	}

}
