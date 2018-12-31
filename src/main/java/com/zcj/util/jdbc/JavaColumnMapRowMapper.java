package com.zcj.util.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * 改造ColumnMapRowMapper，修改返回的属性类型
 * <p>
 * java.sql.Timestamp ==> java.util.Date<br>
 * java.sql.Date ==> java.util.Date<br>
 * byte[] ==> java.lang.String
 * <p>
 * {@link org.springframework.jdbc.core.ColumnMapRowMapper }
 * 
 * @author zouchongjin@sina.com
 * @data 2018年5月15日
 */
public class JavaColumnMapRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = toJavaObj(getColumnValue(rs, i));
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}

	/**
	 * 时间相关的SQL类型都转成java.util.Date
	 * 
	 * @param resultSetValue
	 * @return
	 */
	private Object toJavaObj(Object resultSetValue) {
		if (resultSetValue == null) {
			return null;
		} else if (resultSetValue instanceof java.sql.Timestamp) {
			return new Date(((java.sql.Timestamp) resultSetValue).getTime());
		} else if (resultSetValue instanceof java.sql.Date) {
			return new Date(((java.sql.Date) resultSetValue).getTime());
		} else if (resultSetValue instanceof byte[]) {
			return new String((byte[]) resultSetValue);
		}
		return resultSetValue;
	}

	protected Map<String, Object> createColumnMap(int columnCount) {
		return new LinkedCaseInsensitiveMap<Object>(columnCount);
	}

	protected String getColumnKey(String columnName) {
		return columnName;
	}

	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index);
	}

}
