package com.zcj.util.poi.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标注在 Bean 的 get 方法上，匹配 Bean 和 Excel。<br/>
 * 可配置属性的排序号(order=?,用于导出Excel时的显示顺序和导入Excel时的匹配顺序)。
 * 
 * @author ZCJ
 * @data 2013-7-22
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {

	int order() default 9999;
}
