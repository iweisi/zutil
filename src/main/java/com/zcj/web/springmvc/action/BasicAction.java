package com.zcj.web.springmvc.action;

import com.zcj.util.UtilDate;
import com.zcj.util.UtilString;
import com.zcj.web.mybatis.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BasicAction<S extends BasicService> {

    @Autowired
    protected S basicService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(10000);
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    private class DateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Date date = UtilDate.format(text);
            setValue(date);
        }
    }

    protected Map<String, Object> initQbuilder(String key, Object value) {
        Map<String, Object> query = new HashMap<String, Object>();
        if (UtilString.isNotBlank(key) && value != null) {
            query.put(key, value);
        }
        return query;
    }

    protected Map<String, Object> initQbuilder(String[] keys, Object[] values) {
        Map<String, Object> query = new HashMap<String, Object>();
        if (keys != null && keys.length > 0 && values != null && values.length > 0 && keys.length == values.length) {
            for (int i = 0; i < keys.length; i++) {
                if (UtilString.isNotBlank(keys[i]) && values[i] != null) {
                    query.put(keys[i], values[i]);
                }
            }
        }
        return query;
    }

}