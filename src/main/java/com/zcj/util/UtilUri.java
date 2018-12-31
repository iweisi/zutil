package com.zcj.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class UtilUri {

	/**
	 * 判断URL的域名和端口是否在指定集合中存在
	 * 
	 * @param url
	 * @param urls
	 * @return
	 */
	public static boolean operHostPortInclude(String url, Set<String> urls) {
		if (UtilString.isNotBlank(url) && urls != null && urls.size() > 0) {
			String u = getHostPort(url);
			if (UtilString.isNotBlank(u)) {
				for (String u2 : urls) {
					String uu = getHostPort(u2);
					if (UtilString.isNotBlank(uu) && uu.equals(u)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断两个URL是否为同域名和端口
	 * 
	 * @param url1
	 * @param url2
	 * @return
	 */
	public static boolean operHostPortSame(String url1, String url2) {
		String u1 = getHostPort(url1);
		String u2 = getHostPort(url2);
		if (UtilString.isNotBlank(u1) && UtilString.isNotBlank(u2)) {
			return u1.equals(u2);
		}
		return false;
	}

	/**
	 * 获取URL地址的域名和端口
	 * 
	 * @param url
	 *            例：http://www.baidu.com/?a=1
	 * @return 例：www.baidu.com:80、192.168.1.119:8080
	 */
	public static String getHostPort(String url) {
		if (UtilString.isNotBlank(url)) {
			try {
				URL u = new URL(url);
				return u.getHost() + ":" + (u.getPort() == -1 ? u.getDefaultPort() : u.getPort());
			} catch (MalformedURLException e) {
			}
		}
		return null;
	}

	/** 相对路径转成可访问的完整URL路径 */
	public static String urlFill(String url, String website) {
		if (UtilString.isBlank(url) || UtilString.isBlank(website)) {
			return url;
		}
		if (url.startsWith("http") || url.startsWith("www")) {
			return url;
		}
		return website + url;
	}

	/** 给URL添加参数 */
	public static String urlAddParam(String url, String key, String val) {
		if (UtilString.isBlank(url) || UtilString.isBlank(key) || UtilString.isBlank(val)) {
			return url;
		}
		String a = "";
		if (!url.contains("/")) {// 编码化URL处理 %3F->? %3D->=（编码后的URL不可能存在/）
			if (url.contains("%3F") && !url.contains("%3D")) {// http://zjw.cn?
				a = "";
			} else if (url.contains("%3F") && url.contains("%3D")) {// http://zjw.cn?a=b
				a = "%26";// &
			} else {
				a = "%3F";// ?
			}
		} else {// 非编码化url处理
			if (url.contains("?") && !url.contains("=")) {// http://zjw.cn?
				a = "";
			} else if (url.contains("?") && url.contains("=")) {// http://zjw.cn?a=b
				a = "&";
			} else {
				a = "?";
			}
		}
		return url += (a + key + "=" + val);
	}

}
