package com.zcj.util.coder.database;

/**
 * 表字段的各属性
 * 
 * @author zouchongjin@sina.com
 * @data 2015年12月23日
 */
public class TableColumn {

	/** String类型的最大数据库长度 */
	public static final int LENGTH_MAX_STRING = 4000;

	private String name;// 字段名【不能为空】
	private String type;// 字段类型【不能为空】
	private Integer length;// 字段长度
	private Boolean nullable;// 字段是否允许为空
	private String defaultVal;// 默认值
	private String comment;// 数据库字段备注
	private Boolean index;// 是否创建索引

	public TableColumn() {
		super();
	}

	public TableColumn(String name, String type, Integer length, Boolean nullable, Boolean index, String comment, String defaultVal) {
		super();
		this.name = name;
		this.type = type;
		this.length = length;
		this.nullable = nullable;
		this.index = index;
		this.comment = comment;
		this.defaultVal = defaultVal;
	}

	public String getName() {
		return name;
	}

	public Boolean getIndex() {
		return index;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public void setIndex(Boolean index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

}