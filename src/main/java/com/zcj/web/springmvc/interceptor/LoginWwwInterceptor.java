package com.zcj.web.springmvc.interceptor;

import com.zcj.util.UtilString;
import com.zcj.web.context.BasicWebContext;
import com.zcj.web.dto.AjaxResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 登录拦截器（前台）
 *
 * @author zouchongjin@sina.com
 * @data 2016年5月17日
 */
public class LoginWwwInterceptor implements HandlerInterceptor {

    private String loginUrl;
    private List<String> excludeUrls;

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        if (exclude(arg0)) {
            return true;
        }

        if (BasicWebContext.getLoginUserWww(arg0) == null) {
            boolean ajax = BasicWebContext.isWebAjaxRequest(arg0);
            if (ajax) {
                arg1.getWriter().write(AjaxResult.initErrorLoginJson());
            } else {
                arg1.sendRedirect(initLoginUrl(arg0));
            }
            return false;
        }
        return true;
    }

    private boolean exclude(HttpServletRequest arg0) {
        if (excludeUrls != null) {
            for (String exc : excludeUrls) {
                if (Pattern.matches(arg0.getContextPath() + exc, arg0.getRequestURI())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String initLoginUrl(HttpServletRequest arg0) {
        StringBuilder sb = new StringBuilder();
        sb.append(arg0.getContextPath());
        if (UtilString.isNotBlank(loginUrl)) {
            sb.append(loginUrl);
        }
        return sb.toString();
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

}
