package com.zcj.web.springmvc.interceptor;

import com.zcj.util.UtilEncryption;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ApiResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * API接口的签名验证拦截器
 *
 * @author zouchongjin@sina.com
 * @date 2019/1/2
 */
public class ApiSignInterceptor implements HandlerInterceptor {

    // 万能的API接口签名值
    private String skipSign = null;

    // 请求中保存签名内容的字段值
    private String signKey = "_sign";

    // 签名时的盐值
    private String secretKey = "ABCDEFG";

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
        String _sign = null;
        Map<String, Object> map = new TreeMap<>();
        Map<String, String[]> mapParam = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : mapParam.entrySet()) {
            if (!signKey.equals(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue()[0]);
            } else {
                _sign = entry.getValue()[0];
            }
        }
        if (UtilString.isNotBlank(skipSign) && skipSign.equals(_sign)) {
            return true;
        } else if (!initSignValue(map).equals(_sign)) {
            arg1.getWriter().write(ApiResult.initErrorSignJson());
            return false;
        }
        return true;
    }

    private String transMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("")
                    .append(null == entry.getValue() ? "" : entry.getValue().toString())
                    .append(iterator.hasNext() ? "" : "");
        }
        return sb.toString() + secretKey;
    }

    private String initSignValue(Map map) {
        return UtilEncryption.toMD5(UtilEncryption.toMD5(transMapToString(map)));
    }

    public void setSkipSign(String skipSign) {
        this.skipSign = skipSign;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
