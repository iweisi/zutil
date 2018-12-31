package com.zcj.web.dto;

import java.util.ArrayList;
import java.util.List;

public class UploadResult {

	private List<UploadSuccessResult> success;
	private List<UploadErrorResult> error;
	
	public UploadResult() {
		super();
		success = new ArrayList<UploadSuccessResult>();
		error = new ArrayList<UploadErrorResult>();
	}
	
	public List<UploadSuccessResult> getSuccess() {
		return success;
	}
	public void setSuccess(List<UploadSuccessResult> success) {
		this.success = success;
	}
	public List<UploadErrorResult> getError() {
		return error;
	}
	public void setError(List<UploadErrorResult> error) {
		this.error = error;
	}
	
}
