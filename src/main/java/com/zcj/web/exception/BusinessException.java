package com.zcj.web.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static final String ERROR_SYSTEM = "服务器内部错误";

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
