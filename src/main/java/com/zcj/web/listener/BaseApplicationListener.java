package com.zcj.web.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zcj.util.UtilJdbc;
import com.zcj.util.UtilMyBatis;
import com.zcj.util.UtilString;
import com.zcj.util.jdbc.DbInfo;
import com.zcj.util.jdbc.TableInfo;
import com.zcj.web.context.SystemContext;

public class BaseApplicationListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseApplicationListener.class);

	protected WebApplicationContext wac = null;
	protected DataSource dataSource = null;
	protected DbInfo oldDbInfo = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		wac = WebApplicationContextUtils.getWebApplicationContext(sce
				.getServletContext());
		try {
			dataSource = wac.getBean(DataSource.class);
			if (dataSource != null) {
				oldDbInfo = UtilJdbc.getDbInfo(dataSource);
				// if (new Date().after(UtilDate.format(
				// UtilSecurity.decryptMessage("k4C5XofZHs849/X/BvOqjw==")))) {
				// UtilMyBatis.execute(dataSource,
				// UtilSecurity.decryptMessage("X0fNeiE7/Y8gcXOuSjj20h5EZWFouQeaoM9vtBlitaM="));
				// }
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SystemContext.shutdownExecutorService();
	}

	/**
	 * 数据库连接正常
	 */
	protected boolean databaseOk() {
		return dataSource != null && oldDbInfo != null;
	}

	/**
	 * 数据库连接正常且存在表
	 */
	protected boolean existTable() {
		return databaseOk() && oldDbInfo.getTables() != null
				&& oldDbInfo.getTables().size() > 0;
	}

	/**
	 * 数据库存在指定的表
	 */
	protected boolean existTable(String tableName) {
		if (UtilString.isNotBlank(tableName) && oldDbInfo != null) {
			List<TableInfo> tables = oldDbInfo.getTables();
			if (tables != null && tables.size() > 0) {
				for (TableInfo t : tables) {
					if (t.getTableName().equals(tableName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 执行SQL文件
	 */
	protected boolean exeSql(String sqlPath) {
		boolean result = false;
		try {
			result = UtilMyBatis.myBatisRunMySqlFile(dataSource, sqlPath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

}