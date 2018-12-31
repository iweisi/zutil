package com.zcj.ext.oss.aliyun;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import com.aliyun.oss.OSSClient;

public class AliyunOssUtil {

	public static void uploadImgAliyun(String accessKeyId, String accessSecret, String endpoint, String bucket,
			String myPath, File file, String fileName) throws FileNotFoundException {
		OSSClient client = new OSSClient("http://" + endpoint, accessKeyId, accessSecret);
		client.putObject(bucket, myPath + fileName, file);
		client.shutdown();
	}

	public static void uploadImgAliyun(AliyunOssConfig config, String myPath, File file, String fileName)
			throws FileNotFoundException {
		OSSClient client = new OSSClient("http://" + config.getEndpoint(), config.getAccessKeyId(),
				config.getAccessSecret());
		client.putObject(config.getBucket(), myPath + fileName, file);
		client.shutdown();
	}

	public static void uploadImgAliyun(AliyunOssConfig config, String myPath, byte[] content, String fileName)
			throws FileNotFoundException {
		OSSClient client = new OSSClient("http://" + config.getEndpoint(), config.getAccessKeyId(),
				config.getAccessSecret());
		client.putObject(config.getBucket(), myPath + fileName, new ByteArrayInputStream(content));
		client.shutdown();
	}

}
