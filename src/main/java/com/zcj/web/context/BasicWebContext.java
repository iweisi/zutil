package com.zcj.web.context;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * WEB端(所有方法都不支持多线程)
 * 
 * @author ZCJ
 * @data 2013-8-13
 */
public class BasicWebContext {

	/** HttpSession--Attribute--KEY：登录用户信息，一般用于后台 */
	private static final String LOGIN_INFO = "login_info";

	/** HttpSession--Attribute--KEY：登录用户信息，一般用于前台 */
	private static final String LOGIN_INFO_WWW = "login_info_www";

	/** HttpSession--Attribute--KEY：当前站点，一般用于后台 */
	private static final String SITE_INFO = "site_info";

	/** HttpSession--Attribute--KEY：所有的查询条件(处理前) */
	private static final String QUERY = "query";

	/** HttpSession--Attribute--KEY：所有的查询条件(处理后) */
	private static final String QBUILDER = "qbuilder";

	public static void updateQuery(HttpServletRequest request, String[] keys, Object[] values) {
		Map<String, Object> query = new HashMap<String, Object>();
		Object o = request.getSession().getAttribute(BasicWebContext.QUERY);
		if (o != null) {
			query = (Map<String, Object>) o;
		}
		for (int i = 0; i < keys.length; i++) {
			query.put(keys[i], values[i]);
		}
		request.getSession().setAttribute(BasicWebContext.QUERY, query);
	}

	public static Object getQuery(HttpServletRequest request, String key) {
		Map<String, Object> query = new HashMap<String, Object>();
		Object o = request.getSession().getAttribute(BasicWebContext.QUERY);
		if (o != null) {
			query = (Map<String, Object>) o;
		}
		return query.get(key);
	}

	public static void updateQbuilder(HttpServletRequest request, String[] keys, Object[] values) {
		Map<String, Object> query = new HashMap<String, Object>();
		Object o = request.getSession().getAttribute(BasicWebContext.QBUILDER);
		if (o != null) {
			query = (Map<String, Object>) o;
		}
		for (int i = 0; i < keys.length; i++) {
			query.put(keys[i], values[i]);
		}
		request.getSession().setAttribute(BasicWebContext.QBUILDER, query);
	}

	/** 保存用户登录状态（后台） */
	public static void updateLoginUser(HttpServletRequest request, Object rightUser) {
		if (rightUser != null) {
			request.getSession().setAttribute(BasicWebContext.LOGIN_INFO, rightUser);
		}
	}

	/** 保存当前站点（后台） */
	public static void updateSite(HttpServletRequest request, String site) {
		if (site != null) {
			request.getSession().setAttribute(BasicWebContext.SITE_INFO, site);
		}
	}

	/** 获取当前站点（后台） */
	public static String getSite(HttpServletRequest request) {
		Object site = request.getSession().getAttribute(BasicWebContext.SITE_INFO);
		return Objects.toString(site, null);
	}

	/** 判断是否登陆，如果登陆则返回用户对象，如果未登陆则返回空（后台） */
	public static Object getLoginUser(HttpServletRequest request) {
		return request.getSession().getAttribute(BasicWebContext.LOGIN_INFO);
	}

	/** 移除用户登陆状态（后台） */
	public static void removeLoginUserAndSession(HttpServletRequest request) {
		request.getSession().removeAttribute(BasicWebContext.LOGIN_INFO);
		request.getSession().invalidate();
	}

	/** 保存用户登陆状态（前台） */
	public static void updateLoginUserWww(HttpServletRequest request, Object rightUser) {
		if (rightUser != null) {
			request.getSession().setAttribute(BasicWebContext.LOGIN_INFO_WWW, rightUser);
		}
	}

	/** 判断是否登陆，如果登陆则返回用户对象，如果未登陆则返回空（前台） */
	public static Object getLoginUserWww(HttpServletRequest request) {
		return request.getSession().getAttribute(BasicWebContext.LOGIN_INFO_WWW);
	}

	/** 移除用户登陆状态（前台） */
	public static void removeLoginUserAndSessionWww(HttpServletRequest request) {
		request.getSession().removeAttribute(BasicWebContext.LOGIN_INFO_WWW);
		request.getSession().invalidate();
	}

	/** 判断是否为AJAX请求 */
	public static boolean isWebAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equalsIgnoreCase(requestType);
	}

	/** 获取登陆用户的IP */
	public static String getRemortIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
