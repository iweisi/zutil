package com.zcj.web.dto;

/**
 * 文件上传失败后的返回数据
 * 
 * @author zouchongjin@sina.com
 * @data 2014年7月11日
 */
public class UploadErrorResult {

	private String originalFilename;// 原始文件名，带后缀
	private Long size;// 文件大小
	private String inputName;// 表单的name值
	private String contentType;// 文件类型
	private String suffix;// 文件后缀名，不带.
	
	private String reason;// 上传失败原因
	
	public UploadErrorResult() {
		super();
	}
	public UploadErrorResult(String originalFilename, Long size, String inputName, String contentType, String suffix, String reason) {
		super();
		this.originalFilename = originalFilename;
		this.size = size;
		this.inputName = inputName;
		this.contentType = contentType;
		this.suffix = suffix;
		this.reason = reason;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
