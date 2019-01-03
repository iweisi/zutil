package com.zcj.web.springmvc.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zcj.util.UtilString;
import com.zcj.web.context.BasicWebContext;
import com.zcj.web.context.SystemContext;

public class SystemContextInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SystemContextInterceptor.class);

	/** 分页的有效值 */
	private static final String REGEX = "[0-9]+";

	private String offsetKey = "offset";// 分页偏移量的参数名
	private String pagesizeKey = "pagesize";// 每页数量的参数名
	private String pageKey;// 页码的参数名
	private int defaultPagesize = 15;// 默认每页的数量
	private String contentType = "text/html;charset=utf-8";

	private boolean iframeCrossDomain;// 是否支持iframe嵌入跨域

	private String siteKey = "site";// 全局请求有效的参数名
	private boolean siteBack;// 请求结束后是否把值传回视图
	private String defaultSite;// 全局参数的默认值

	private Map<String, InterceptorCallback> modelPut = new HashMap<String, InterceptorCallback>();// 请求结束后需要传回视图的内容

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// 最后执行，可用于释放资源；可以根据ex是否为null判断是否发生了异常，进行日志记录

		SystemContext.removeOffset();
		SystemContext.removePagesize();
		SystemContext.removeSite();
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// 生成视图之前执行；有机会修改ModelAndView

		if (siteBack && UtilString.isNotBlank(siteKey) && arg3 != null) {
			Map<String, Object> map = arg3.getModel();
			if (map != null) {
				map.put(siteKey, SystemContext.getSite());
			}
		}
		if (modelPut != null && !modelPut.isEmpty() && arg3 != null) {
			Map<String, Object> map = arg3.getModel();
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			for (Map.Entry<String, InterceptorCallback> entry : modelPut.entrySet()) {
				map.put(entry.getKey(), entry.getValue().value());
			}
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {

		logger.debug("请求地址：" + arg0.getRequestURI());

		// System.out.println(arg0.getContextPath()); // "/ssm"
		// System.out.println(arg0.getRequestURI()); // "/ssm/login.do"
		// System.out.println(arg0.getRequestURL()); // "http://.../ssm/login.do"
		// System.out.println(arg0.getSession().getServletContext().getRealPath("/"));
		// "E:\Kuaipan\Java_Developer\eclipseWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp8\wtpwebapps\spring_springmvc_mybatis\"
		// System.out.println(arg0.getServletPath()); // "/login.do"

		SystemContext.setOffset(getOffset(arg0));
		SystemContext.setPagesize(getPagesize(arg0));
		SystemContext.setSite(getSite(arg0));
		arg1.setContentType(contentType);

		if (iframeCrossDomain) {
			arg1.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		}

		return true;
	}

	// 优先获取URL上参数对应的值，然后获取session中的值，最后使用默认值
	private String getSite(HttpServletRequest request) {
		if (UtilString.isNotBlank(siteKey)) {
			String site = request.getParameter(siteKey);
			if (UtilString.isNotBlank(site)) {
				site = UtilString.keywordFilter(site);
				BasicWebContext.updateSite(request, site.trim());
				return site.trim();
			}
		}
		String site = BasicWebContext.getSite(request);
		site = UtilString.keywordFilter(site);
		if (site != null) {
			return site.trim();
		}
		if (UtilString.isNotBlank(defaultSite)) {
			return defaultSite.trim();
		}
		return null;
	}

	private int getOffset(HttpServletRequest request) {
		if (UtilString.isNotBlank(offsetKey)) {
			String[] offsetKeyArray = offsetKey.split(",");
			for (String offsetKey : offsetKeyArray) {
				String offset = request.getParameter(offsetKey);
				if (!UtilString.isBlank(offset) && offset.matches(REGEX)) {
					return Integer.parseInt(offset);
				}
			}
		}
		if (UtilString.isNotBlank(pageKey)) {
			Integer pagesize = getPagesize(request);
			String[] pageKeyArray = pageKey.split(",");
			for (String pageKey : pageKeyArray) {
				String page = request.getParameter(pageKey);
				if (UtilString.isNotBlank(page) && page.matches(REGEX)) {
					return toOffset(Integer.parseInt(page), pagesize);
				}
			}
		}
		return 0;
	}

	/** 页码转偏移量 */
	private Integer toOffset(Integer page, Integer pagesize) {
		if (page == null || page <= 0 || pagesize == null || pagesize <= 0) {
			return 0;
		}
		return (page - 1) * pagesize;
	}

	private int getPagesize(HttpServletRequest request) {
		if (UtilString.isNotBlank(pagesizeKey)) {
			String[] pagesizeKeyArray = pagesizeKey.split(",");
			for (String pagesizeKey : pagesizeKeyArray) {
				String pagesize = request.getParameter(pagesizeKey);
				if (!UtilString.isBlank(pagesize) && pagesize.matches(REGEX)) {
					return Integer.parseInt(pagesize);
				}
			}
		}
		return defaultPagesize;
	}

	public void modelPut(String key, InterceptorCallback callback) {
		if (UtilString.isNotBlank(key) && callback != null) {
			modelPut.put(key, callback);
		}
	}

	public String getOffsetKey() {
		return offsetKey;
	}

	public void setOffsetKey(String offsetKey) {
		this.offsetKey = offsetKey;
	}

	public String getPagesizeKey() {
		return pagesizeKey;
	}

	public void setPagesizeKey(String pagesizeKey) {
		this.pagesizeKey = pagesizeKey;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isIframeCrossDomain() {
		return iframeCrossDomain;
	}

	public void setIframeCrossDomain(boolean iframeCrossDomain) {
		this.iframeCrossDomain = iframeCrossDomain;
	}

	public int getDefaultPagesize() {
		return defaultPagesize;
	}

	public void setDefaultPagesize(int defaultPagesize) {
		this.defaultPagesize = defaultPagesize;
	}

	public boolean isSiteBack() {
		return siteBack;
	}

	public void setSiteBack(boolean siteBack) {
		this.siteBack = siteBack;
	}

	public String getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(String siteKey) {
		this.siteKey = siteKey;
	}

	public String getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(String defaultSite) {
		this.defaultSite = defaultSite;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

}
