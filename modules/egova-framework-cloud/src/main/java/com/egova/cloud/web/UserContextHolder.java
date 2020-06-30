package com.egova.cloud.web;

import com.egova.security.core.DefaultUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class UserContextHolder {

    private static final ThreadLocal<DefaultUserDetails> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setup(DefaultUserDetails userDetails) {
        THREAD_LOCAL.set(userDetails);
    }

    public static void setup() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return;
        }
        if (principal instanceof DefaultUserDetails) {
            DefaultUserDetails userDetails = (DefaultUserDetails) authentication.getPrincipal();
            THREAD_LOCAL.set(userDetails);
        }
    }

    public static DefaultUserDetails current() {
        return THREAD_LOCAL.get();
    }

    public static void cleanup() {
        THREAD_LOCAL.remove();
    }

}
