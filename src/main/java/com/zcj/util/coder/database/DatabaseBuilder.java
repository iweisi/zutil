package com.zcj.util.coder.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zcj.util.UtilString;
import com.zcj.util.coder.CoderUtil;

public class DatabaseBuilder {

	public static Database initDatabase(Class<?>[] carray, String databaseType) {
		List<Table> tables = new ArrayList<Table>();
		for (Class<?> c : carray) {
			tables.add(initTable(c, databaseType));
		}
		String dname = carray[0].getName().split("\\.")[2];

		Database result = new Database();
		result.setName(dname);
		result.setTables(tables);
		return result;
	}

	private static Table initTable(Class<?> c, String databaseType) {

		String[] allName = c.getName().split("\\.");

		// 表的注释
		String tableComment = "";

		// 表名的前缀
		String tablePrefix = "t_";
		if (allName.length >= 3) {
			tablePrefix = "t_" + allName[2] + "_";
		}
		if (c.isAnnotationPresent(TableType.class)) {
			TableType tt = c.getAnnotation(TableType.class);
			if (UtilString.isNotBlank(tt.prefix())) {
				tablePrefix = tt.prefix();
			}
			if (UtilString.isNotBlank(tt.comment())) {
				tableComment = tt.comment();
			}
		}

		String tableName = tablePrefix + c.getSimpleName().toLowerCase();
		List<TableColumn> columns = new ArrayList<TableColumn>();

		List<Field> fs = CoderUtil.allField(c, true);
		for (Field f : fs) {
			String[] typeAndLength = initDefaultTypeAndLengthByField(databaseType, f);
			String tType = typeAndLength[0];
			Integer tLength = typeAndLength[1] == null ? null : Integer.valueOf(typeAndLength[1]);
			Boolean tNullable = true;
			Boolean tIndex = false;
			String tComment = null;
			String tDefacultVal = null;
			if (f.isAnnotationPresent(TableColumnType.class)) {
				TableColumnType sqlType = f.getAnnotation(TableColumnType.class);
				if (sqlType.length() != 0) {
					tLength = sqlType.length();
					// MySQL 的 int(1) 自动改成 tinyint(1)
					if (tLength == 1 && "int".equals(tType) && Database.TYPE_MYSQL.equals(databaseType)) {
						tType = "tinyint";
					}
				}
				if (!sqlType.nullable()) {
					tNullable = false;
				}
				if (sqlType.index()) {
					tIndex = true;
				}
				if (UtilString.isNotBlank(sqlType.defaultVal())) {
					tDefacultVal = sqlType.defaultVal();
					tNullable = false;
				}
				if (UtilString.isNotBlank(sqlType.comment())) {
					tComment = sqlType.comment();
				}
				if ("text".equals(sqlType.type())) {
					if (Database.TYPE_MYSQL.equals(databaseType)) {
						tType = "text";
						tLength = null;
					}
				}
			}
			columns.add(new TableColumn(f.getName(), tType, tLength, tNullable, tIndex, tComment, tDefacultVal));
		}
		return new Table(tableName, tableComment, columns);
	}

	private static String[] initDefaultTypeAndLengthByField(String databaseType, Field ff) {
		if (Database.TYPE_MYSQL.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				return new String[] { "int", "11" };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "bigint", "20" };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				return new String[] { "varchar", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "datetime", null };
			} else if ("class java.lang.Float".equals(ff.getType().toString())) {
				return new String[] { "float", null };
			} else if ("class java.math.BigDecimal".equals(ff.getType().toString())) {
				return new String[] { "decimal(10,2)", null };
			}
		} else if (Database.TYPE_SQLSERVER.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				return new String[] { "int", null };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "bigint", null };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				return new String[] { "nvarchar", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "datetime", null };
			}
		} else if (Database.TYPE_ORACLE.equals(databaseType)) {
			if ("class java.lang.Integer".equals(ff.getType().toString())) {
				return new String[] { "number", "11" };
			} else if ("class java.lang.Long".equals(ff.getType().toString())) {
				return new String[] { "number", "20" };
			} else if ("class java.lang.String".equals(ff.getType().toString())) {
				return new String[] { "nvarchar2", "100" };
			} else if ("class java.util.Date".equals(ff.getType().toString())) {
				return new String[] { "date", null };
			}
		}
		return new String[] { null, null };
	}

}
