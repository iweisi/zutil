package com.zcj.util.coder.java.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zcj.util.UtilString;
import com.zcj.util.coder.CoderUtil;
import com.zcj.util.coder.database.TableType;

public class QueryBuilder {

	public static List<QueryColumn> initQueryColumnList(Class<?> className) {
		List<QueryColumn> qbuilderList = new ArrayList<QueryColumn>();

		String[] allName = className.getName().split("\\.");
		if (allName.length < 6) {// com.zopen.zboot.entity.article.Catalog
			return null;
		}

		// 表名的前缀
		String tablePrefix = "t_" + allName[2] + "_";
		if (className.isAnnotationPresent(TableType.class)) {
			TableType tt = className.getAnnotation(TableType.class);
			if (UtilString.isNotBlank(tt.prefix())) {
				tablePrefix = tt.prefix();
			}
		}

		List<Field> fs = CoderUtil.allField(className, false);
		for (Field f : fs) {
			if (f.isAnnotationPresent(QueryColumnType.class)) {
				QueryColumnType qtype = f.getAnnotation(QueryColumnType.class);
				String[] es = qtype.value();

				String srcTableName = qtype.srcTableName();
				String srcFieldName = qtype.srcFieldName();
				if (UtilString.isBlank(srcTableName)) {
					srcTableName = tablePrefix + allName[5].toLowerCase();
				}
				if (UtilString.isBlank(srcFieldName)) {
					srcFieldName = f.getName();
				}

				String fieldType = "";
				if ("class java.lang.Integer".equals(f.getType().toString())) {
					fieldType = "Integer";
				} else if ("class java.lang.Long".equals(f.getType().toString())) {
					fieldType = "Long";
				} else if ("class java.lang.String".equals(f.getType().toString())) {
					fieldType = "String";
				} else if ("class java.util.Date".equals(f.getType().toString())) {
					fieldType = "Date";
				} else if ("class java.lang.Float".equals(f.getType().toString())) {
					fieldType = "Float";
				} else if ("class java.math.BigDecimal".equals(f.getType().toString())) {
					fieldType = "BigDecimal";
				}

				for (String e : es) {
					qbuilderList.add(new QueryColumn(f.getName(), fieldType, e, qtype.listQuery(), srcTableName,
							srcFieldName));
				}
			}
		}

		return qbuilderList;
	}

}
