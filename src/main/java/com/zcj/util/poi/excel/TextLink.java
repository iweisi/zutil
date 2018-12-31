package com.zcj.util.poi.excel;

import java.io.Serializable;

/**
 * poi导出excel拓展
 * 当数据类型的需求是1个文本带上超链接的时候 实例化此类 其中的address为文本的超链接地址 title为文本显示的名称
 * @author pks
 *
 */
public class TextLink implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -127587885194991144L;
	private String address;
	private String title;
	
	public TextLink() {
		
	}
	
	public TextLink(String address, String title) {
		super();
		this.address = address;
		this.title = title;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
