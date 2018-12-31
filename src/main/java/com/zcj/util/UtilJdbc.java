package com.zcj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.zcj.util.jdbc.DbInfo;
import com.zcj.util.jdbc.UtilDbInfo;
import com.zcj.util.json.json.JSONArray;
import com.zcj.util.json.json.JSONException;
import com.zcj.util.json.json.JSONObject;

public class UtilJdbc {

	private static final Logger logger = LoggerFactory.getLogger(UtilJdbc.class);

	public static String DATABASETYPE_SQLSERVER = DbInfo.TYPE_SQLSERVER;
	public static String DATABASETYPE_MYSQL = DbInfo.TYPE_MYSQL;
	public static String DATABASETYPE_ORACLE = DbInfo.TYPE_ORACLE;

	/** 获取数据库及表的信息 */
	public static DbInfo getDbInfo(Connection con) throws SQLException {
		return UtilDbInfo.getDbInfo(con);
	}

	/** 获取数据库及表的信息 */
	public static DbInfo getDbInfo(DataSource ds) throws SQLException {
		return UtilDbInfo.getDbInfo(ds);
	}

	/**
	 * 
	 * @param databaseType
	 *            UtilJdbc.DATABASETYPE_SQLSERVER
	 * @param ip
	 *            192.168.1.119
	 * @param port
	 *            1433
	 * @param databaseName
	 *            txl
	 * @param username
	 *            "sa"
	 * @param password
	 *            "123456"
	 * @param querySql
	 *            "select * from db_Dept"
	 * @return
	 */
	public static String query(String databaseType, String ip, int port, String databaseName, String username,
			String password, String querySql) {
		String result = null;

		ResultSet rs = null;
		Statement stmt = null;
		Connection conn = null;
		try {
			DriverManager.setLoginTimeout(1);
			if (DATABASETYPE_SQLSERVER.equals(databaseType)) {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
				conn = DriverManager.getConnection("jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName="
						+ databaseName, username, password);
			} else if (DATABASETYPE_MYSQL.equals(databaseType)) {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + databaseName, username,
						password);
			} else if (DATABASETYPE_ORACLE.equals(databaseType)) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName + "",
						username, password);
			} else {
				return result;
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			result = resultSetToJson(rs);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

	private static String resultSetToJson(ResultSet rs) throws SQLException, JSONException {
		JSONArray array = new JSONArray();

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();

			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.put(jsonObj);
		}

		return array.toString();
	}

	public static List<JsonObject> resultSetToJsonObjectForMySQL(ResultSet rs) throws SQLException {

		List<JsonObject> result = new ArrayList<JsonObject>();

		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		while (rs.next()) {
			JsonObject json = new JsonObject();
			for (int i = 1; i <= count; i++) {
				String filedName = metaData.getColumnName(i);
				if (null == rs.getObject(i)) {
					json.addProperty(filedName, "");
					continue;
				}
				switch (metaData.getColumnType(i)) {
				case Types.DATE:
				case Types.TIME:
				case Types.TIMESTAMP:
					json.addProperty(filedName,
							UtilDate.SDF_DATETIME.get().format(new Date(rs.getTimestamp(i).getTime())));
					break;
				case Types.BIGINT:
					json.addProperty(filedName, rs.getBigDecimal(i).toBigInteger());
					break;
				case Types.DECIMAL:
					json.addProperty(filedName, rs.getInt(i));
					break;
				case Types.INTEGER:
					json.addProperty(filedName, rs.getLong(i));
					break;
				case Types.DOUBLE:
					json.addProperty(filedName, rs.getDouble(i));
					break;
				case Types.FLOAT:
					json.addProperty(filedName, rs.getFloat(i));
					break;
				default:
					json.addProperty(filedName, rs.getString(i).trim());
					break;
				}
			}
			result.add(json);
		}
		return result;
	}

}
