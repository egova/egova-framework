package com.egova.web.config.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 解决 spring mvc 参数绑定 "null" 字符串问题
 *
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class ParameterServletRequestWrapper extends HttpServletRequestWrapper {

    private static final String NULL = "null";

    ParameterServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        // "null" 字符串替换为 null
        if (NULL.equals(value)) {
            return null;
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            // "null" 字符串替换为 null
            if (NULL.equals(value)) {
                values[i] = null;
            }
        }
        return values;
    }

}
