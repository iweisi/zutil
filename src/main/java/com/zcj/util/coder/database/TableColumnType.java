package com.zcj.util.coder.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表字段的属性（仅用于生成SQL文件）</br>
 * </br>
 * MySQL支持的JAVA字段类型：Integer、Long、String、Date、Float、BigDecimal</br>
 * SqlServer支持的JAVA字段类型：Integer、Long、String、Date</br>
 * Oracle支持的JAVA字段类型：Integer、Long、String、Date</br>
 * </br>
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月23日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColumnType {

	/** 生成SQL和JAVA代码时排除此字段（默认false[如果字段以show_开头，也自动排除]） */
	boolean exclude() default false;

	/** 字段类型，可取值：text。（默认空串[按JAVA类型自动生成]） */
	String type() default "";

	/** 字段长度（默认0[按JAVA类型自动生成，String长度为100]） */
	int length() default 0;

	/** 字段允许为空（默认允许空） */
	boolean nullable() default true;

	/** 默认值（设了默认值后同时会设置了字段不允许空） */
	String defaultVal() default "";

	/** 数据库字段备注 */
	String comment() default "";

	/** 字段是否创建索引（默认否） */
	boolean index() default false;

}