package com.zcj.web.springmvc.action;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.zcj.util.UtilDate;
import com.zcj.util.UtilString;
import com.zcj.web.dto.Page;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.springmvc.support.Uploader;

public class BasicAction {

	protected Page page = new Page();

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(10000);
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	private class DateEditor extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			Date date = UtilDate.format(text);
			setValue(date);
		}
	}

	protected Map<String, Object> initQbuilder(String key, Object value) {
		Map<String, Object> query = new HashMap<String, Object>();
		if (UtilString.isNotBlank(key) && value != null) {
			query.put(key, value);
		}
		return query;
	}

	protected Map<String, Object> initQbuilder(String[] keys, Object[] values) {
		Map<String, Object> query = new HashMap<String, Object>();
		if (keys != null && keys.length > 0 && values != null && values.length > 0 && keys.length == values.length) {
			for (int i = 0; i < keys.length; i++) {
				if (UtilString.isNotBlank(keys[i]) && values[i] != null) {
					query.put(keys[i], values[i]);
				}
			}
		}
		return query;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Deprecated
	protected String RESULT_PAR_ERROR = "404";// 参数错误的返回值

	@Deprecated
	protected ServiceResult uploadFile(HttpServletRequest request, String type, String basePath) {
		return new Uploader.Builder(request).basePath(basePath).folder(type).addUnAllowsSuffixs(".jsp", ".exe").build()
				.go();
	}

	@Deprecated
	protected ServiceResult uploadFileFastDFS(HttpServletRequest request, String[] szTrackerServers,
			Map<String, Object> param) {
		return new Uploader.Builder(request).fastDFSAddServers(szTrackerServers).fastDFSParam(param).build().go();
	}

	@Deprecated
	protected ServiceResult uploadFileFastDFS(HttpServletRequest request, String[] szTrackerServers,
			Map<String, Object> param, String group) {
		return new Uploader.Builder(request).fastDFSAddServers(szTrackerServers).fastDFSGroup(group)
				.fastDFSParam(param).build().go();
	}
}