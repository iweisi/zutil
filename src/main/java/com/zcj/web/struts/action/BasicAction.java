package com.zcj.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zcj.web.context.SystemContext;
import com.zcj.web.dto.Page;

public class BasicAction extends ActionSupport {

	private static final long serialVersionUID = 1404380229220104757L;

	protected final String JSON_RESULT = "jsonResult";
	protected String jsonString = "";

	protected Page page = new Page();
	protected String offset = String.valueOf(SystemContext.getDefaultOffset());
	protected String pagesize = String.valueOf(SystemContext.getDefaultPagesize());
	
	protected HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

}