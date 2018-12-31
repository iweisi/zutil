package com.zcj.util.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.zcj.util.UtilDate;

/**
 * 改造ColumnMapRowMapper，修改返回的属性类型
 * <p>
 * java.sql.Timestamp ==> java.lang.String<br>
 * java.sql.Date ==> java.lang.String<br>
 * byte[] ==> java.lang.String
 * <p>
 * {@link org.springframework.jdbc.core.ColumnMapRowMapper }
 * 
 * @author zouchongjin@sina.com
 * @data 2018年7月12日
 */
public class JsonColumnMapRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = toJsonObj(getColumnValue(rs, i));
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}

	private Object toJsonObj(Object resultSetValue) {
		if (resultSetValue == null) {
			return null;
		} else if (resultSetValue instanceof java.sql.Timestamp) {
			return UtilDate.SDF_DATETIME.get().format(new Date(((java.sql.Timestamp) resultSetValue).getTime()));
		} else if (resultSetValue instanceof java.sql.Date) {
			return UtilDate.SDF_DATETIME.get().format(new Date(((java.sql.Date) resultSetValue).getTime()));
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
