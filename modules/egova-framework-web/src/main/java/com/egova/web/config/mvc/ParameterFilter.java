package com.egova.web.config.mvc;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决 spring mvc 参数绑定 "null" 字符串问题
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/*")
public class ParameterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(new ParameterServletRequestWrapper(request), response);
    }

}
