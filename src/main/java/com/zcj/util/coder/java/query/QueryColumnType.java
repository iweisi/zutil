package com.zcj.util.coder.java.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询条件的属性（仅用于生成JAVA文件）<br>
 * <br>
 * JAVA字段类型支持的查询条件：<br>
 * String：=、like<br>
 * Integer：=、in、between<br>
 * Date：=、between、time(废弃)<br>
 * Long：=、in、between<br>
 * Float：=、in、between<br>
 * BigDecimal：=、between<br>
 * <br>
 *
 * @author zouchongjin@sina.com
 * @data 2015年12月23日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryColumnType {

    /**
     * 支持：=、like、in、time、between </br>支持同时配置多个：{ "=", "in" }
     */
    String[] value() default {};

    /**
     * 是否作为分页列表的查询条件（默认否）</br> =和like会在Action中生成同JAVA类型的一个接收参数</br> time和between会在Action中生成同JAVA类型的两个接收参数
     */
    boolean listQuery() default false;

    /**
     * 对应哪张表（默认当前表）</br>注意：如果设置了此属性，需要额外在SQL中添加关联查询语句（LEFT JOIN）
     */
    @Deprecated
    String srcTableName() default "";

    /**
     * 对应表中的哪个字段（默认当前字段）
     */
    String srcFieldName() default "";

}