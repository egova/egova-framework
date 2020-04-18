package com.egova.security.utils;

import com.egova.security.core.properties.SecurityProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author chendb
 * @description: 安全配置工具类
 * @date 2020-04-17 23:36:34
 */
public class SecurityConfigurationUtils {

    public static void configure(HttpSecurity http, SecurityProperties securityProperties) throws Exception {
        switch (securityProperties.getXFrameOptions()){
            case ALLOW_FROM:
                http.headers().frameOptions().disable();
                break;
            case SAMEORIGIN:
                http.headers().frameOptions().sameOrigin();
                break;
            case DENY:
                http.headers().frameOptions().deny();
                break;
        }
    }
}
