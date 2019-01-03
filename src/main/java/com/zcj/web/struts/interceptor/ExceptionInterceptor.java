package com.zcj.web.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.zcj.web.context.BasicWebContext;
import com.zcj.web.dto.AjaxResult;
import com.zcj.web.exception.BusinessException;
import com.zcj.web.struts.action.BasicAction;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionInterceptor extends AbstractInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String result = "";
        try {
            result = invocation.invoke();
        } catch (BusinessException ex) {
            logger.error(ex.getMessage(), ex);
            if (BasicWebContext.isWebAjaxRequest(ServletActionContext.getRequest())) {
                BasicAction action = (BasicAction) invocation.getAction();
                action.setJsonString(AjaxResult.initErrorSystemJson());
                return "jsonResult";
            } else {
                throw ex;
            }

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            if (BasicWebContext.isWebAjaxRequest(ServletActionContext.getRequest())) {
                BasicAction action = (BasicAction) invocation.getAction();
                action.setJsonString(AjaxResult.initErrorSystemJson());
                return "jsonResult";
            } else {
                throw new BusinessException("内部错误");
            }

        }
        return result;
    }
}