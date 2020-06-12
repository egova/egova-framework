package com.egova.cloud.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

/**
 * 为 {@link CheckTokenEndpoint } 增加额外的oauth2用户信息传递
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Slf4j
public class CheckTokenEndpointAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        CheckTokenEndpoint checkTokenEndpoint = null;
        try {
            checkTokenEndpoint = applicationContext.getBean(CheckTokenEndpoint.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.debug("checkTokenEndpoint not definition");
        }
        if (checkTokenEndpoint != null) {
            // 提取oauth2用户信息
            DefaultAccessTokenConverter accessTokenConverter = new CustomAccessTokenConverter();
            checkTokenEndpoint.setAccessTokenConverter(accessTokenConverter);
        }
    }
}
