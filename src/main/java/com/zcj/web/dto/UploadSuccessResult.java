package com.zcj.web.dto;

/**
 * 文件上传成功后的返回数据
 * 
 * @author zouchongjin@sina.com
 * @data 2014年7月11日
 */
public class UploadSuccessResult {

	private String originalFilename;// 原始文件名，带后缀
	private Long size;// 文件大小
	private String inputName;// 表单的name值
	private String contentType;// 文件类型
	private String suffix;// 文件后缀名，不带.

	private String newFilename;// 保存后的文件名，带后缀
	private String savePath;// 文件保存的相对路径，包括文件名

	private String aaa;// 扩展字段
	private String bbb;// 扩展字段
	private String ccc;// 扩展字段

	public UploadSuccessResult() {
		super();
	}

	public UploadSuccessResult(String originalFilename, Long size, String contentType, String inputName, String suffix, String newFilename,
			String savePath) {
		super();
		this.originalFilename = originalFilename;
		this.size = size;
		this.contentType = contentType;
		this.inputName = inputName;
		this.suffix = suffix;
		this.newFilename = newFilename;
		this.savePath = savePath;
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

	public String getContentType() {
		return contentType;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getCcc() {
		return ccc;
	}

	public void setCcc(String ccc) {
		this.ccc = ccc;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getNewFilename() {
		return newFilename;
	}

	public void setNewFilename(String newFilename) {
		this.newFilename = newFilename;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getAaa() {
		return aaa;
	}

	public void setAaa(String aaa) {
		this.aaa = aaa;
	}

	public String getBbb() {
		return bbb;
	}

	public void setBbb(String bbb) {
		this.bbb = bbb;
	}

}
