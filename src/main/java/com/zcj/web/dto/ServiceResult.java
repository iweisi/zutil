package com.zcj.web.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/** 依赖 gson-*.jar */
public class ServiceResult implements Serializable {

	private static final long serialVersionUID = -8490143934858131947L;

	private int s;
	private Object d;

	public static final String ERROR_PARAM = "参数错误";// 0
	public static final String ERROR_SYSTEM = "系统错误";// 0
	public static final String ERROR_NOPERM = "没有权限";// 3
	public static final String ERROR_LOGIN = "请重新登录";// 2

	private static final int S_ERROR = 0;// 失败标识
	private static final int S_SUCCESS = 1;// 成功标识
	private static final int S_NOLOGIN = 2;// 未登录
	private static final int S_NOPERM = 3;// 没有权限

	public static final Gson GSON_D = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	public static final Gson GSON_DT = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static final Gson GSON_D_SERIALIZENULLS = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd")
			.create();
	public static final Gson GSON_DT_SERIALIZENULLS = new GsonBuilder().serializeNulls()
			.setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static String initSuccessJson(Object d, boolean showNull) {
		if (showNull) {
			return GSON_DT_SERIALIZENULLS.toJson(initSuccess(d));
		} else {
			return initSuccessJson(d);
		}
	}

	public static ServiceResult initSuccess(Object d) {
		return new ServiceResult(S_SUCCESS, d);
	}
	
	public static ServiceResult init(int s, Object d) {
		return new ServiceResult(s, d);
	}

	public static ServiceResult initError(String d) {
		return new ServiceResult(S_ERROR, d);
	}

	public static ServiceResult initErrorParam() {
		return new ServiceResult(S_ERROR, ERROR_PARAM);
	}

	public static ServiceResult initErrorSystem() {
		return new ServiceResult(S_ERROR, ERROR_SYSTEM);
	}

	private static ServiceResult initErrorNoPerm() {
		return new ServiceResult(S_NOPERM, ERROR_NOPERM);
	}

	private static ServiceResult initErrorLogin() {
		return new ServiceResult(S_NOLOGIN, ERROR_LOGIN);
	}

	/** 返回成功JSON字符串 */
	public static String initSuccessJson(Object d) {
		return GSON_DT.toJson(initSuccess(d));
	}

	/** 返回失败JSON字符串 */
	public static String initErrorJson(String d) {
		return GSON_D.toJson(initError(d));
	}

	/** 返回失败JSON字符串--参数错误 */
	public static String initErrorParamJson() {
		return GSON_D.toJson(initError(ERROR_PARAM));
	}

	/** 返回重新登录JSON字符串 */
	public static String initErrorLoginJson() {
		return GSON_D.toJson(initErrorLogin());
	}

	/** 返回没有权限JSON字符串 */
	public static String initErrorNoPermJson() {
		return GSON_D.toJson(initErrorNoPerm());
	}

	/** 此返回结果是成功的 */
	public boolean success() {
		if (S_SUCCESS == this.getS()) {
			return true;
		} else {
			return false;
		}
	}

	private ServiceResult() {
	}

	private ServiceResult(int s, Object d) {
		this.s = s;
		this.d = d;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public Object getD() {
		return d;
	}

	public void setD(Object d) {
		this.d = d;
	}
}
