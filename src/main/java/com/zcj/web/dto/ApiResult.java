package com.zcj.web.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用于REST风格API的返回格式
 *
 * @author zouchongjin@sina.com
 * @date 2019/1/2
 */
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 3715411520951675028L;

    @ApiModelProperty("返回标识码（0：成功；1：失败；2：token错误；3：签名错误）")
    private Integer code;
    @ApiModelProperty("内容")
    private T data;
    @ApiModelProperty("描述")
    private String message;
    @ApiModelProperty("数据总量")
    private Integer total;

    private static final int CODE_SUCCESS = 0;// 成功
    private static final int CODE_ERROR = 1;// 失败
    private static final int CODE_ERROR_TOKEN = 2;// token错误
    private static final int CODE_ERROR_SIGN = 3;// 签名错误

    private static final String MESSAGE_SUCCESS = "success";
    private static final String MESSAGE_ERROR_PARAM = "参数错误";
    private static final String MESSAGE_ERROR_TOKEN = "token错误";
    private static final String MESSAGE_ERROR_SIGN = "签名错误";

    private static final Gson GSON_D = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private static final Gson GSON_DT = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> ApiResult<T> initSuccess(T data) {
        return new ApiResult<T>(CODE_SUCCESS, MESSAGE_SUCCESS, data, null);
    }

    public static <T> ApiResult<T> initSuccessPage(T data, Integer total) {
        return new ApiResult<T>(CODE_SUCCESS, MESSAGE_SUCCESS, data, total);
    }

    public static <T> ApiResult<T> initError(String message) {
        return new ApiResult<T>(CODE_ERROR, message, null, null);
    }

    public static <T> ApiResult<T> initErrorSign() {
        return new ApiResult<T>(CODE_ERROR_SIGN, MESSAGE_ERROR_SIGN, null, null);
    }

    public static <T> ApiResult<T> initErrorToken() {
        return new ApiResult<T>(CODE_ERROR_TOKEN, MESSAGE_ERROR_TOKEN, null, null);
    }

    public static <T> String initSuccessJson(T d) {
        return GSON_DT.toJson(initSuccess(d));
    }

    public static <T> String initSuccessPageJson(T data, Integer total) {
        return GSON_DT.toJson(initSuccessPage(data, total));
    }

    public static String initErrorJson(String message) {
        return GSON_D.toJson(initError(message));
    }

    public static String initErrorParamJson() {
        return GSON_D.toJson(initError(MESSAGE_ERROR_PARAM));
    }

    public static String initErrorSignJson() {
        return GSON_D.toJson(initErrorSign());
    }

    public static String initErrorTokenJson() {
        return GSON_D.toJson(initErrorToken());
    }

    public boolean success() {
        return CODE_SUCCESS == this.getCode();
    }

    private ApiResult() {
    }

    private ApiResult(Integer code, String message, T data, Integer total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
