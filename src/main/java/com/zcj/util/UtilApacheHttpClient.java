package com.zcj.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

//<dependency>
//	<groupId>org.apache.httpcomponents</groupId>
//	<artifactId>httpclient</artifactId>
//	<version>4.3.3</version>
//</dependency>
/**
 * HTTP请求，包括GET和POST
 * 
 * @author ZCJ
 * @data 2013-4-15
 */
@Deprecated
public class UtilApacheHttpClient {

	public static final String ERROR = "ERROR";

	private UtilApacheHttpClient() {

	}

	/**
	 * GET请求
	 * 
	 * @param url
	 * <br/>
	 *            http://www.baidu.com/s?wd=aaa
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String httpGet(String url) {
		String response = null;

		try {
			@SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpResponse.getEntity());
			} else {
				response = ERROR;
			}
		} catch (Exception e) {
			response = ERROR;
		}

		return response;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * <br/>
	 *            http://www.baidu.com/s
	 * @param params
	 * <br/>
	 *            List&lt;NameValuePair&gt; params = new
	 *            ArrayList&lt;NameValuePair&gt;(); <br/>
	 *            params.add(new BasicNameValuePair("wd", "aaa"));
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("resource")
	public static String httpPost(String url, List<NameValuePair> params) {
		String response = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				response = EntityUtils.toString(httpResponse.getEntity());
			} else {
				response = ERROR;
			}
		} catch (Exception e) {
			response = ERROR;
		}

		return response;
	}

}
