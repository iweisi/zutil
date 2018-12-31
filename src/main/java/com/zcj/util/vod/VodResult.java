package com.zcj.util.vod;

import com.zcj.util.vod.mediainfo.MediainfoBean;

/**
 * 视频转码相关的返回
 * 
 * @author zouchongjin@sina.com
 * @data 2014年9月22日
 */
public class VodResult {

	private String sourceFilePath;// 源文件路径
	private MediainfoBean sourceFileInfo;// 源文件信息
	
	private boolean h264;// 是否转码成功
	private String h264FilePath;// 转码后的文件路径
	
	private boolean stream;// 是否转流成功
	private String streamFilePath;// 流文件路径
	
	private boolean image;// 是否截图成功
	private String imageFilePath;// 截图文件
	
	public String getSourceFilePath() {
		return sourceFilePath;
	}
	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}
	public MediainfoBean getSourceFileInfo() {
		return sourceFileInfo;
	}
	public void setSourceFileInfo(MediainfoBean sourceFileInfo) {
		this.sourceFileInfo = sourceFileInfo;
	}
	public boolean isH264() {
		return h264;
	}
	public void setH264(boolean h264) {
		this.h264 = h264;
	}
	public String getH264FilePath() {
		return h264FilePath;
	}
	public void setH264FilePath(String h264FilePath) {
		this.h264FilePath = h264FilePath;
	}
	public boolean isStream() {
		return stream;
	}
	public void setStream(boolean stream) {
		this.stream = stream;
	}
	public String getStreamFilePath() {
		return streamFilePath;
	}
	public void setStreamFilePath(String streamFilePath) {
		this.streamFilePath = streamFilePath;
	}
	public boolean isImage() {
		return image;
	}
	public void setImage(boolean image) {
		this.image = image;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	
}
