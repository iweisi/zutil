package com.zcj.ext.fastdfs;

import java.util.HashMap;
import java.util.Map;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;

/**
 * FastDFS文件的信息，包括FileInfo和NameValuePair信息</br>
 * 	 依赖：fastdfs-client-java-20141207.jar</br>
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月15日
 */
public class FastdfsFileInfo extends FileInfo {

	private Map<String, String> param;

	public FastdfsFileInfo(FileInfo fInfo, NameValuePair nvps[]) {
		super(fInfo.getFileSize(), (int) (fInfo.getCreateTimestamp().getTime() / 1000), (int) fInfo.getCrc32(), fInfo.getSourceIpAddr());
		if (nvps != null && nvps.length > 0) {
			param = new HashMap<String, String>();
			for (NameValuePair n : nvps) {
				param.put(n.getName(), n.getValue());
			}
		}
	}

	private FastdfsFileInfo(long file_size, int create_timestamp, int crc32, String source_ip_addr) {
		super(file_size, create_timestamp, crc32, source_ip_addr);
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "FastdfsFileInfo [param=" + param + ", source_ip_addr=" + source_ip_addr + ", file_size=" + file_size
				+ ", create_timestamp=" + create_timestamp + ", crc32=" + crc32 + "]";
	}

}
