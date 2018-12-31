package com.zcj.util.coder.page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应页面里的配置（仅用于生成页面文件）
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月25日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageColumnType {

	/** 中文名 */
	String name();
	
	/** 是否可编辑（默认是） */
	boolean modify() default true;

	/** 是否必填（默认否） */
	boolean must() default false;

	/** 类型，可取值：text[默认]、textarea、select、img、date、ueditor、file */
	String type() default "text";

	/**
	 * 验证方式（当 type="text||textarea||select||date" 时可用）（默认空串[不验证]）</br>
	 * 例(grid)："min-len:4|n[仅为数字]|mobile[手机号]|max-len:10|scope:1-10[数字区间限制]|idcard|url|email|ip"等</br>
	 * 例(layui): "required（必填项）|phone（手机号）|email（邮箱）|url（网址）|number（数字）|date（日期）|identity（身份证）"等</br>
	 * 必填验证[data-check="must"]不需要设置，通过是否必填属性[PageColumnBean.must]决定是否必填，因为关系到页面的星号[*]显示
	 */
	String check() default "";

	/** 输入的最大长度（当 type="text" 时可用）（默认0[不限制]） */
	int maxlength() default 0;
	
	/**
	 * 默认值（当 type="text||textarea||img||date||ueditor" 时可用）（默认空串[无默认值]）</br>
	 * 例："${.now}"</br>
	 */
	String defaultValue() default "";

	/**
	 * 数据字典（当 type="select" 时可用）</br>
	 * 字符串类型左右请用单引号。例：{"1='管理员'","2='普通用户'"} 或 {"'A'='管理员'", "'B'='管理员'"} 或 {"'A'=1", "'B'=2"}
	 */
	String[] keyValue() default {};
	
	/** 是否表格中显示（默认否） */
	boolean grid() default false;
	
	/**
	 * 是否提供查询（当 type="text||textarea||date||ueditor||select" 时可用）（默认否）<br>
	 * 关于后台接收查询参数：如果字段名为“name”，则后台通过“searchName”接收查询参数；如果是date类型，则后台通过“searchXxxBegin”和“searchXxxEnd”接收 */
	boolean search() default false;
	
}
