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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageType {

	/** 父模块中文名（空串表示没有父模块） */
	String pname() default "";

	/** 模块中文名（空串表示没有模块名） */
	String name() default "";

	/** 是否弹窗方式（默认是） */
	boolean dialog() default true;

	/** 是否需要引入百度编辑器（默认否） */
	boolean ueditor() default false;

	/** 是否需要引入上传插件（默认否） */
	boolean upload() default false;

	/** 是否生成导出按钮（默认否） */
	boolean export() default false;

}
