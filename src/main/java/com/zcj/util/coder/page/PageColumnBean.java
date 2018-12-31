package com.zcj.util.coder.page;

import java.util.HashMap;
import java.util.Map;

public class PageColumnBean {

	/** 中文名 */
	private String name = "";

	/** 属性名 */
	private String fieldName = "";

	/** 是否可编辑（默认是） */
	private boolean modify = true;

	/** 是否必填（默认否） */
	private boolean must = false;

	/** 类型，可取值：text[默认]、textarea、select、img、date、ueditor */
	private String type = "text";

	/**
	 * 验证方式（当 type="text||textarea||select||date" 时可用）（默认空[不验证]）</br>
	 * 必填验证[data-check="must"]不需要设置，通过是否必填属性[PageColumnBean.must]决定是否必填，因为关系到页面的星号[*]显示
	 */
	private String check = null;

	/** 输入的最大长度（当 type="text" 时可用）（默认空[不限制]） */
	private Integer maxlength = null;

	/** 默认值（当 type="text||textarea||img||date||ueditor" 时可用）（默认空[无默认值]） */
	private String defaultValue = null;

	/** 数据字典（当 type="select" 时可用）：【key="1",value="系统管理员"】 */
	private Map<String, String> keyValue = new HashMap<String, String>();

	/** 是否表格中显示（默认否） */
	private boolean grid = false;

	/** 是否提供查询（默认否） */
	private boolean search = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean isMust() {
		return must;
	}

	public void setMust(boolean must) {
		this.must = must;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public Integer getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Map<String, String> getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(Map<String, String> keyValue) {
		this.keyValue = keyValue;
	}

	public boolean isGrid() {
		return grid;
	}

	public void setGrid(boolean grid) {
		this.grid = grid;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

}
