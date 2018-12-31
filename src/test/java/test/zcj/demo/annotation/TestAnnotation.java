package test.zcj.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解模板
 * 
 * Target[声明的对象]：CONSTRUCTOR[构造方法声明]、FIELD[字段声明]、LOCAL_VARIABLE[局部变量声明]、METHOD[方法声明]、PACKAGE[包声明]、PARAMETER[参数声明]、TYPE[类、接口]
 * Retention[注解保留方式]：RUNTIME[运行存在]、SOURCE[源码显示/编译丢失]、CLASS[编译存在/运行忽略]
 * Inherited[允许子类继承]，在Target=TYPE时有效
 * Documented[生成javadoc时会包含注解]
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月25日
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TestAnnotation {

	int age() default 18;

	String desc();

	String author() default "";

}