package com.egova.cloud.oauth2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

/**
 * @author chendb
 * @description:
 * @date 2020-06-28 13:37:20
 */
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2ClientTokenConfiguration {

    @Bean("egovaOAuth2RestTemplate")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ClientProperties oAuth2ClientProperties) {

        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId(oAuth2ClientProperties.getId());
        details.setAccessTokenUri(oAuth2ClientProperties.getAccessTokenUrl());
        details.setClientId(oAuth2ClientProperties.getClientId());
        details.setClientSecret(oAuth2ClientProperties.getClientSecret());
        details.setAuthenticationScheme(AuthenticationScheme.valueOf(oAuth2ClientProperties.getClientAuthenticationScheme()));
        // 注意OAuth2ClientContext不能从spring上下文中注入进来，因为默认OAuth2ClientContext是@Scope为 "session" ,在子线程中会重新创建bean失败
        final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        return oAuth2RestTemplate;
    }
}