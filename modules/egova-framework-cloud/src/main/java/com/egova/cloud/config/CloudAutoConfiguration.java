package com.egova.cloud.config;

import com.egova.cloud.feign.EnableFeignClients;
import com.egova.cloud.feign.FeignAutoConfiguration;
import com.egova.cloud.hystrix.HystrixAutoConfiguration;
import com.egova.cloud.oauth2.CheckTokenEndpointAutoConfiguration;
import com.egova.cloud.oauth2.OAuth2ClientTokenConfiguration;
import com.egova.cloud.oauth2.RemoteTokenServicesAutoConfiguration;
import com.egova.cloud.web.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chendb
 * @description: 微服务自动配置
 * @date 2020-04-18 10:09:49
 */
//@EnableOAuth2Client
@EnableDiscoveryClient
@EnableFeignClients("com.egova")
@ImportAutoConfiguration({
        OAuth2ClientTokenConfiguration.class,
        FeignAutoConfiguration.class,
        HystrixAutoConfiguration.class,
        RemoteTokenServicesAutoConfiguration.class,
        CheckTokenEndpointAutoConfiguration.class,
        RestTemplateAutoConfiguration.class
})
public class CloudAutoConfiguration {


}
