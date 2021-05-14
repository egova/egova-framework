package com.egova.security.resource.config;

import com.egova.security.core.DefaultTokenExtractor;
import com.egova.security.core.crypto.NoneOrBCryptPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

//@Configuration
public class ResourceBeanConfig
{
    @Bean
    public DefaultTokenExtractor defaultTokenExtractor(){
        return new DefaultTokenExtractor();
    }

    /**
     * 默认密码处理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder()
    {
        return new NoneOrBCryptPasswordEncoder();
    }

    /**
     * 访问拒绝处理器
     * @return 访问拒绝处理器
     * @author chendb
     * @date 2016年12月8日 下午5:23:07
     */
    @Bean
    public OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler()
    {
        return new OAuth2AccessDeniedHandler();
    }
}
