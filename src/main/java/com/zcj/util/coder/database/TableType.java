package com.zcj.util.coder.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据库表的属性
 * 
 * @author zouchongjin@sina.com
 * @data 2018年6月7日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableType {

	/** 表前缀，默认t_xxxx_ */
	String prefix() default "";

	/** 数据库字段备注 */
	String comment() default "";
}