package com.egova.web.config.mvc;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

/**
 * 解决 spring mvc 参数绑定 "null" 字符串问题
 * {@link org.springframework.web.method.annotation.RequestParamMethodArgumentResolver }
 *
 * @author 奔波儿灞
 * @since 1.0
 */
//@Configuration
public class ParameterFilterConfiguration {

    private final static int ORDER = 100;

    @Bean
    public FilterRegistrationBean<ParameterFilter> parameterFilter() {
        FilterRegistrationBean<ParameterFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ParameterFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singleton("/*"));
        filterRegistrationBean.setOrder(ORDER);
        return filterRegistrationBean;
    }

}
