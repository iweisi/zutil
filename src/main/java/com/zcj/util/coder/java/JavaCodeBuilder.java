package com.zcj.util.coder.java;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zcj.util.UtilString;
import com.zcj.util.coder.CoderUtil;
import com.zcj.util.coder.database.TableColumnType;
import com.zcj.util.coder.database.TableType;
import com.zcj.util.coder.java.query.QueryBuilder;

public class JavaCodeBuilder {

	public static JavaCode initJavaCode(Class<?> className) {
		String[] allName = className.getName().split("\\.");
		if (allName.length != 6) {// com.zopen.zboot.entity.article.Catalog
			return null;
		}

		// 表名的前缀
		String tablePrefix = "t_" + allName[2] + "_";
		// 表中文名
		String tableCnName = "";
		if (className.isAnnotationPresent(TableType.class)) {
			TableType tt = className.getAnnotation(TableType.class);
			if (UtilString.isNotBlank(tt.prefix())) {
				tablePrefix = tt.prefix();
			}
			if (UtilString.isNotBlank(tt.comment())) {
				tableCnName = tt.comment();
			}
		}

		JavaCode code = new JavaCode();
		code.setPackageName(allName[0] + "." + allName[1] + "." + allName[2]);// com.zopen.zboot
		code.setModuleName(allName[4]);// article
		code.setClassName(allName[5]);// Catalog
		code.setTableName(tablePrefix + allName[5].toLowerCase());// t_catalog
		code.setTableCnName(tableCnName);
		code.setFieldList(CoderUtil.allField(className, true));
		code.setAllFieldList(CoderUtil.allField(className, false));
		code.setQbuilderList(QueryBuilder.initQueryColumnList(className));
		code.setFieldNameCommentMap(fieldNameCommentMap(className));
		return code;
	}

	private static Map<String, String> fieldNameCommentMap(Class<?> className) {
		Map<String, String> result = new HashMap<>();
		List<Field> fields = CoderUtil.allField(className, false);
		if (fields != null && fields.size() > 0) {
			for (Field f : fields) {
				if (f.isAnnotationPresent(TableColumnType.class)) {
					TableColumnType sqlType = f.getAnnotation(TableColumnType.class);
					if (UtilString.isNotBlank(sqlType.comment())) {
						result.put(f.getName(), sqlType.comment());
					}
				}
			}
		}
		return result;
	}

}
