package com.zcj.util;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.zcj.web.dto.ServiceResult;

/**
 * HTTP请求，包括GET和POST
 * 
 * @author ZCJ
 * @data 2013-4-15
 */
@Deprecated
public class UtilApacheHttpClient2 {

	private UtilApacheHttpClient2() {

	}

	/**
	 * 上传文件
	 * 
	 * @param httpUrl
	 *            接收文件的请求地址
	 * @param file
	 *            上传的文件
	 * @param type
	 *            接收后保存的文件夹名
	 * @return
	 */
	public static String httpPostFile(String httpUrl, File file, String type) {

		String result = null;

		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(httpUrl);

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", new FileBody(file));
			reqEntity.addPart("type", new StringBody(type));
			httppost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
				EntityUtils.consume(resEntity);
			}
		} catch (Exception e) {
			result = ServiceResult.initErrorJson("上传文件失败");
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}

		return result;
	}

}
