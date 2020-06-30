package com.egova.cloud.feign;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class RequestUtils {

    private static final String TOKEN_NAME = "access_token";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private RequestUtils() {
        throw new IllegalStateException("Utils");
    }

    static String url() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            // 非web环境
            return "";
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return request != null ? request.getRequestURL().toString() : "";
    }

    public static String getToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            // 非web环境
            return TokenHolder.current();
        }
        HttpServletRequest request = requestAttributes.getRequest();
        // 从参数中获取
        String token = request.getParameter(TOKEN_NAME);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // 从请求头中获取
        String authorization = request.getHeader(TOKEN_HEADER);
        if (StringUtils.isNotBlank(authorization)) {
            if (StringUtils.startsWithIgnoreCase(authorization, BEARER_TOKEN_PREFIX)) {
                return authorization.substring(BEARER_TOKEN_PREFIX.length());
            }
        }
        // 从cookie中获取
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
