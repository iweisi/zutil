package com.zcj.web.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * 用于AJAX请求的返回格式
 *
 * @author zouchongjin@sina.com
 * @date 2019/1/2
 */
public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = 3715411520951675027L;

    private Integer code;
    private T data;
    private String msg;
    private Integer count;

    private static final int CODE_SUCCESS = 0;// 成功
    private static final int CODE_ERROR = 1;// 失败
    private static final int CODE_NO_LOGIN = 2;// 未登录

    private static final String MSG_SUCCESS = "success";
    private static final String MSG_ERROR_PARAM = "参数错误";
    private static final String MSG_ERROR_SYSTEM = "系统错误";
    private static final String MSG_ERROR_NO_PERMISSION = "没有权限";
    private static final String MSG_ERROR_ILLEGAL = "非法操作";
    private static final String MSG_ERROR_NO_LOGIN = "请重新登录";

    public static final Gson GSON_D = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static final Gson GSON_DT = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final Gson GSON_DT_SERIALIZE_NULLS = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> AjaxResult<T> initSuccess(T data) {
        return new AjaxResult<T>(CODE_SUCCESS, MSG_SUCCESS, data, null);
    }

    public static <T> AjaxResult<T> initSuccessPage(T data, Integer count) {
        return new AjaxResult<T>(CODE_SUCCESS, MSG_SUCCESS, data, count);
    }

    public static <T> AjaxResult<T> initError(String message) {
        return new AjaxResult<T>(CODE_ERROR, message, null, null);
    }

    public static <T> AjaxResult<T> initErrorLogin() {
        return new AjaxResult<>(CODE_NO_LOGIN, MSG_ERROR_NO_LOGIN, null, null);
    }

    public static <T> AjaxResult<T> initErrorSystem() {
        return new AjaxResult<>(CODE_ERROR, MSG_ERROR_SYSTEM, null, null);
    }

    public static <T> AjaxResult<T> initErrorPermission() {
        return new AjaxResult<>(CODE_ERROR, MSG_ERROR_NO_PERMISSION, null, null);
    }

    public static <T> AjaxResult<T> initErrorIllegal() {
        return new AjaxResult<>(CODE_ERROR, MSG_ERROR_ILLEGAL, null, null);
    }

    public static <T> String initSuccessJson(T data) {
        return GSON_DT.toJson(initSuccess(data));
    }

    public static <T> String initSuccessPageJson(T data, Integer total) {
        return GSON_DT.toJson(initSuccessPage(data, total));
    }

    public static String initErrorJson(String message) {
        return GSON_D.toJson(initError(message));
    }

    public static String initErrorLoginJson() {
        return GSON_D.toJson(initErrorLogin());
    }

    public static String initErrorSystemJson() {
        return GSON_D.toJson(initErrorSystem());
    }

    public static String initErrorParamJson() {
        return GSON_D.toJson(initError(MSG_ERROR_PARAM));
    }

    public static String initErrorPermissionJson() {
        return GSON_D.toJson(initErrorPermission());
    }

    public static String initErrorIllegalJson() {
        return GSON_D.toJson(initErrorIllegal());
    }

    public boolean success() {
        return CODE_SUCCESS == this.getCode();
    }

    private AjaxResult() {
    }

    private AjaxResult(Integer code, String msg, T data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
}
