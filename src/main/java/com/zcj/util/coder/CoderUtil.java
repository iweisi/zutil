package com.zcj.util.coder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zcj.util.coder.database.TableColumnType;
import com.zcj.web.mybatis.entity.BasicTreeEntity;

public class CoderUtil {

	/** 判断是否为有效字段 */
	private static boolean valid(Field f, boolean exclude) {
		if (f == null || f.getModifiers() >= 8 || (f.getName().startsWith("show_") && exclude)) {
			return false;
		}
		if (exclude && f.isAnnotationPresent(TableColumnType.class)) {
			TableColumnType sqlType = f.getAnnotation(TableColumnType.class);
			if (sqlType.exclude()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取类的属性
	 * 
	 * @param exclude
	 *            是否排除 TableColumnType(exclude = true) 注释的字段和 show_ 开头的字段
	 */
	public static List<Field> allField(Class<?> c, boolean exclude) {
		List<Field> fs = new ArrayList<Field>();
		if (c.getSuperclass() == BasicTreeEntity.class) {
			Field[] f = c.getSuperclass().getDeclaredFields();
			if (f != null && f.length > 0) {
				for (Field ff : f) {
					if (valid(ff, exclude)) {
						fs.add(ff);
					}
				}
			}
		}
		Field[] mef = c.getDeclaredFields();
		if (mef != null && mef.length > 0) {
			for (Field ff : mef) {
				if (valid(ff, exclude)) {
					fs.add(ff);
				}
			}
		}
		return fs;
	}

}
