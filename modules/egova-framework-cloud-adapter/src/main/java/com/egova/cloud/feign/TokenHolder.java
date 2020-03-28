package com.egova.cloud.feign;

/**
 * Token上下文保存
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class TokenHolder {

    private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setup(String token) {
        THREAD_LOCAL.set(token);
    }

    public static String current() {
        return THREAD_LOCAL.get();
    }

    public static void cleanup() {
        THREAD_LOCAL.remove();
    }

}
