package com.zcj.ext.oss.aliyun;

public class AliyunOssConfig {

	private String accessKeyId;
	private String accessSecret;
	private String endpoint;
	private String bucket;

	public AliyunOssConfig(String accessKeyId, String accessSecret, String endpoint, String bucket) {
		super();
		this.accessKeyId = accessKeyId;
		this.accessSecret = accessSecret;
		this.endpoint = endpoint;
		this.bucket = bucket;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

}
