package com.egova.cloud.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

/**
 * RemoteTokenServices包装，增加服务发现能力
 *
 * @author 奔波儿灞
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration.TokenInfoServicesConfiguration#remoteTokenServices()
 * @since 1.0
 */
@Slf4j
public class RemoteTokenServicesAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private final RestTemplate restTemplate;

    public RemoteTokenServicesAutoConfiguration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        RemoteTokenServices tokenServices = null;
        // 修复认证服务本身无需注册的bug
        try {
            tokenServices = applicationContext.getBean(RemoteTokenServices.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.debug("remoteTokenServices not definition");
        }
        if (tokenServices != null) {
            // 增加微服务调用认证中心，是否支持服务发现。关闭后，需要手动配置认证中心IP地址。默认开启，可服务发现，负载均衡
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            Boolean discovery = env.getProperty("oauth2.client.discovery", Boolean.class, true);
            if (BooleanUtils.toBoolean(discovery)) {
                tokenServices.setRestTemplate(restTemplate);
            }
            // 提取oauth2用户信息
            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            accessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
            tokenServices.setAccessTokenConverter(accessTokenConverter);
        }
    }

}
