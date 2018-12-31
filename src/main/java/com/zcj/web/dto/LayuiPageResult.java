package com.zcj.web.dto;

import java.util.List;

import com.google.gson.Gson;

public class LayuiPageResult {

	private Integer code;
	private String msg;
	private Integer count;
	private List<?> data;

	public LayuiPageResult() {
		super();
	}

	public LayuiPageResult(Integer count, List<?> data) {
		this.code = 0;
		this.count = count;
		this.data = data;
	}

	public LayuiPageResult(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String converByServiceResult(ServiceResult sr) {
		return converByServiceResult(sr, ServiceResult.GSON_DT);
	}
	
	public static String converByServiceResult(ServiceResult sr, Gson gson) {
		LayuiPageResult obj = new LayuiPageResult();
		if (sr.success()) {
			Page p = (Page) sr.getD();
			obj = new LayuiPageResult(p.getTotal(), p.getRows());
			return gson.toJson(obj);
		} else {
			if (sr.getD() != null) {
				return "{\"code\":" + sr.getS() + ",\"msg\":\"" + String.valueOf(sr.getD()) + "\"}";
			} else {
				return "{\"code\":" + sr.getS() + "}";
			}
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

}
