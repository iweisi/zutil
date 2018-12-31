package com.zcj.util.poi.excel;

/**
 * 用来存储Excel的每列信息，通过该对象可以获取每列对应DTO的属性和方法
 * 
 * @author ZCJ
 * @data 2013-7-22
 */
public class ExcelHeader implements Comparable<ExcelHeader> {

	private int order;// 列的顺序
	private String methodName;// 对应的get方法名称

	public int compareTo(ExcelHeader o) {
		return order > o.order ? 1 : (order < o.order ? -1 : 0);
	}

	public ExcelHeader(int order, String methodName) {
		super();
		this.order = order;
		this.methodName = methodName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
