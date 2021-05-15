package com.egova.security;


import com.egova.exception.ExceptionUtils;
import com.egova.security.core.DefaultUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author chendb
 * @description: 用户上下文
 * @date 2021-02-24 13:55:13
 */
public class UserContext {


    private UserContext() {
        throw new IllegalStateException("Utils");
    }

    /**
     * 获取当前登陆的用户名,未认证不会抛出异常
     *
     * @return 当前登陆的用户信息
     */
    public static String username() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
           //  throw ExceptionUtils.api("need authentication");
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取当前登陆的用户名
     *
     * @return 当前登陆的用户信息，未认证则抛出异常
     */
    public static String loginName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw ExceptionUtils.api("need authentication");
        }
        return authentication.getName();
    }


    /**
     * 当前登录用户租户信息
     *
     * @return
     */
    public static DefaultUserDetails details() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() instanceof DefaultUserDetails) {
            DefaultUserDetails defaultUserDetails = (DefaultUserDetails) authentication.getPrincipal();
            return defaultUserDetails;
        }
        return null;
    }


    /**
     * 当前登录用户租户信息
     *
     * @return
     */
    public static String tenantId() {
        DefaultUserDetails defaultUserDetails = details();
        return defaultUserDetails == null ? null : defaultUserDetails.getTenantId();
    }

    /**
     * 用户对应的人员主键标识
     * @return
     */
    public static String personId() {
        DefaultUserDetails defaultUserDetails = details();
        return defaultUserDetails == null ? null : defaultUserDetails.getPersonId();
    }

    /**
     * 用户主键标识
     * @return
     */
    public static String userId() {
        DefaultUserDetails defaultUserDetails = details();
        return defaultUserDetails == null ? null : defaultUserDetails.getId();
    }

}
