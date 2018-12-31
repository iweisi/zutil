package com.zcj.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class UtilReflect {

    private static final Logger logger = LoggerFactory.getLogger(UtilReflect.class);

    private static List<Field> getFields(Object source) {
        List<Field> list = new ArrayList<Field>();
        Class<?> superClass = source.getClass();
        while (null != superClass) {
            list.addAll(Arrays.asList(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        return list;
    }

    /**
     * 复制source中非空的属性的值到target中 只是浅复制
     *
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyNotNull(Object source, Object target) {
        if (null == source || null == target) {
            throw new RuntimeException("属性复制错误：参数错误");
        }
        if (!source.getClass().equals(target.getClass())) {
            throw new RuntimeException("属性复制错误：类型不相同");
        }
        List<Field> fields = getFields(source);
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Method[] methods = source.getClass().getMethods();
        for (Method method : methods) {
            methodMap.put(method.getName().toLowerCase(), method);
        }
        try {
            for (Field field : fields) {
                String propertie = field.getName().toLowerCase();
                Method sourceMethod = null;
                if (field.getType().equals(boolean.class)) {
                    if (!methodMap.keySet().contains(propertie)) {
                        continue;
                    }
                    sourceMethod = methodMap.get(propertie);
                } else {
                    if (!methodMap.keySet().contains("get" + propertie)) {
                        continue;
                    }
                    sourceMethod = methodMap.get("get" + propertie);
                }
                Object sourceValue = sourceMethod.invoke(source);
                if (null == sourceValue) {
                    continue;
                }
                if (!methodMap.keySet().contains("set" + propertie)) {
                    continue;
                }
                Method targetMethod = methodMap.get("set" + propertie);
                targetMethod.invoke(target, new Object[]{sourceValue});
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：非法参数");
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：非法调用");
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：调用异常");
        }
    }

    /**
     * 复制source中非空的属性的值到target中 只是浅复制
     *
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyNotNullExclude(Object source, Object target, String[] excludeFields) {
        if (null == source || null == target) {
            throw new RuntimeException("属性复制错误：参数错误");
        }
        if (!source.getClass().equals(target.getClass())) {
            throw new RuntimeException("属性复制错误：类型不相同");
        }
        List<Field> fields = getFields(source);
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Method[] methods = source.getClass().getMethods();
        for (Method method : methods) {
            methodMap.put(method.getName().toLowerCase(), method);
        }
        try {
            for (Field field : fields) {
                String propertie = field.getName().toLowerCase();
                Method sourceMethod = null;
                if (field.getType().equals(boolean.class)) {
                    if (!methodMap.keySet().contains(propertie)) {
                        continue;
                    }
                    sourceMethod = methodMap.get(propertie);
                } else {
                    if (!methodMap.keySet().contains("get" + propertie)) {
                        continue;
                    }
                    sourceMethod = methodMap.get("get" + propertie);
                }
                Object sourceValue = sourceMethod.invoke(source);
                if (null == sourceValue && (excludeFields == null || !Arrays.asList(excludeFields).contains(propertie))) {
                    continue;
                }
                if (!methodMap.keySet().contains("set" + propertie)) {
                    continue;
                }
                Method targetMethod = methodMap.get("set" + propertie);
                targetMethod.invoke(target, new Object[]{sourceValue});
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：非法参数");
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：非法调用");
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("属性复制错误：调用异常");
        }
    }

}
