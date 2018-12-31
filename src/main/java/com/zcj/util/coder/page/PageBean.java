package com.zcj.util.coder.page;

import java.util.ArrayList;
import java.util.List;

public class PageBean {

	/** 父模块中文名 */
	private String pname = "";

	/** 模块中文名 */
	private String name = "";

	/** 父模块名 */
	private String moduleName = "";

	/** 类名 */
	private String className = "";

	/** 是否弹窗方式（默认是） */
	private boolean dialog = true;

	/** 是否需要引入百度编辑器（默认否） */
	private boolean ueditor = false;
	
	/** 是否需要引入上传插件（默认否） */
	private boolean upload = false;

	/** 是否生成导出按钮（默认否） */
	private boolean export = false;

	/** 属性配置集合 */
	private List<PageColumnBean> columnList = new ArrayList<PageColumnBean>();

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
	}

	public boolean isDialog() {
		return dialog;
	}

	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}

	public boolean isUeditor() {
		return ueditor;
	}

	public void setUeditor(boolean ueditor) {
		this.ueditor = ueditor;
	}

	public List<PageColumnBean> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<PageColumnBean> columnList) {
		this.columnList = columnList;
	}

}
