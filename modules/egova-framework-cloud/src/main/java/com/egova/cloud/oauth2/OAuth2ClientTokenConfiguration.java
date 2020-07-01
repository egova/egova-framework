package com.egova.cloud.oauth2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import java.util.Collections;

/**
 * @author chendb
 * @description:
 * @date 2020-06-28 13:37:20
 */
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2ClientTokenConfiguration {

    @Bean("egovaOAuth2RestTemplate")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ClientProperties oAuth2ClientProperties, LoadBalancerClient loadBalancer) {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId(oAuth2ClientProperties.getId());
        details.setAccessTokenUri(oAuth2ClientProperties.getAccessTokenUrl());
        details.setClientId(oAuth2ClientProperties.getClientId());
        details.setClientSecret(oAuth2ClientProperties.getClientSecret());
        details.setAuthenticationScheme(AuthenticationScheme.valueOf(oAuth2ClientProperties.getClientAuthenticationScheme()));
        // 注意OAuth2ClientContext不能从spring上下文中注入进来，因为默认OAuth2ClientContext是@Scope为 "session" ,在子线程中会重新创建bean失败
        OAuth2RestTemplate template = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        // 手动设置负载均衡
        ClientCredentialsAccessTokenProvider provider = new ClientCredentialsAccessTokenProvider();
        provider.setInterceptors(Collections.singletonList(new LoadBalancerInterceptor(loadBalancer)));
        template.setAccessTokenProvider(provider);
        return template;
    }

}