package com.zcj.web.dto;

/**
 * 文件下载需要的数据
 * 
 * @author zouchongjin@sina.com
 * @data 2015年3月2日
 */
public class DownloadResult {

	private String fileName;// 文件命名，带后缀
	private String url;// 相对网站的路径，如"/download/test.xls"
	
	public DownloadResult() {
		super();
	}
	public DownloadResult(String fileName, String url) {
		super();
		this.fileName = fileName;
		this.url = url;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
