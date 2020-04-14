package com.egova.cloud.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

/**
 * RemoteTokenServices包装，增加服务发现能力
 *
 * @author 奔波儿灞
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration.TokenInfoServicesConfiguration#remoteTokenServices()
 * @since 1.0
 */
@Configuration
public class RemoteTokenServicesAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteTokenServicesAutoConfiguration.class);

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
            LOG.debug("remoteTokenServices not definition");
        }
        if (tokenServices != null) {
            tokenServices.setRestTemplate(restTemplate);
        }
    }

}
