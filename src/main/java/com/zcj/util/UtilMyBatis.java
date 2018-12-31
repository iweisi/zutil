package com.zcj.util;

import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Connection;

/**
 * 依赖：org.mybatis#mybatis#3.2.2 和 mysql#mysql-connector-java#5.1.18
 * 
 * @author zouchongjin@sina.com
 * @data 2014年7月22日
 */
public class UtilMyBatis {

	private static final Logger logger = LoggerFactory.getLogger(UtilMyBatis.class);

	public static boolean myBatisRunMySqlFile(DataSource ds, String sqlPath) {
		java.sql.Connection conn = null;
		try {
			Resources.setCharset(Charset.forName("UTF-8"));
			conn = ds.getConnection();
			ScriptRunner runner = new ScriptRunner(conn);
			runner.setLogWriter(null);
			runner.setErrorLogWriter(null);
			runner.setStopOnError(true);
			runner.runScript(Resources.getResourceAsReader(sqlPath));
			return true;
		} catch (Exception e) {
			logger.error("执行`" + sqlPath + "`错误：" + e.getMessage(), e);
			return false;
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
	}

	/**
	 * MyBatis 工具运行Sql文件执行MySQL语句
	 * 
	 * @param url
	 * @param driver
	 * @param username
	 * @param password
	 * @param sqlPath
	 *            SQL文件的路径，例："sql/version3.sql"（文件编码必须为UTF-8）
	 * @return
	 */
	public static boolean myBatisRunMySqlFile(String url, String driver, String username, String password,
			String sqlPath) {
		Connection conn = null;
		try {
			Resources.setCharset(Charset.forName("UTF-8"));
			Class.forName(driver).newInstance();
			conn = (Connection) DriverManager.getConnection(url, username, password);
			ScriptRunner runner = new ScriptRunner(conn);
			runner.setLogWriter(null);
			runner.setErrorLogWriter(null);
			runner.setStopOnError(true);
			runner.runScript(Resources.getResourceAsReader(sqlPath));
			return true;
		} catch (Exception e) {
			logger.error("执行`" + sqlPath + "`错误：" + e.getMessage(), e);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * MyBatis 工具运行Sql文件执行MySQL语句
	 * <p>
	 * 使用方法：
	 * <p>
	 * UtilMyBatis.myBatisRunMySqlFile("conf/application.properties",
	 * "jdbc.url", "jdbc.driver", "jdbc.username", "jdbc.password",
	 * "sql/version3.sql");
	 * 
	 * @param jdbcFilePath
	 *            数据库连接配置文件的路径，例："conf/application.properties"
	 * @param urlKey
	 * @param driverKey
	 * @param usernameKey
	 * @param passwordKey
	 * @param sqlPath
	 *            SQL文件的路径，例："sql/version3.sql"（文件编码必须为UTF-8）
	 * @return
	 */
	public static boolean myBatisRunMySqlFile(String jdbcFilePath, String urlKey, String driverKey, String usernameKey,
			String passwordKey, String sqlPath) {
		Connection conn = null;
		try {
			Resources.setCharset(Charset.forName("UTF-8"));
			Properties props = Resources.getResourceAsProperties(jdbcFilePath);
			String url = props.getProperty(urlKey);
			String driver = props.getProperty(driverKey);
			String username = props.getProperty(usernameKey);
			String password = props.getProperty(passwordKey);
			Class.forName(driver).newInstance();
			conn = (Connection) DriverManager.getConnection(url, username, password);
			ScriptRunner runner = new ScriptRunner(conn);
			runner.setLogWriter(null);
			runner.setErrorLogWriter(null);
			runner.setStopOnError(true);
			runner.runScript(Resources.getResourceAsReader(sqlPath));
			return true;
		} catch (Exception e) {
			logger.error("执行`" + sqlPath + "`错误：" + e.getMessage(), e);
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/** 执行SQL字符串，多语句用英文分号隔开 */
	public static boolean execute(DataSource ds, String sqlString) {
		if (UtilString.isBlank(sqlString)) {
			return false;
		}
		String[] sqlStringArray = sqlString.split(";");
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (String sql : sqlStringArray) {
				if (UtilString.isNotBlank(sql)) {
					stmt.addBatch(sql);
				}
			}
			stmt.executeBatch();
			conn.commit();
			return true;
		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}
			}
			return false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
			}
		}
	}
}
