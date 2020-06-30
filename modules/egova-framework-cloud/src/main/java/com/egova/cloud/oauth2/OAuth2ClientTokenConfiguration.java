package com.egova.cloud.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

/**
 * @author chendb
 * @description:
 * @date 2020-06-28 13:37:20
 */
//@EnableOAuth2Client


@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2ClientTokenConfiguration {

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

//    @Autowired
//    private OAuth2ClientContext oAuth2ClientContext;

//    @Autowired
//    private LoadBalancerInterceptor loadBalancerInterceptor;

//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Bean
//    public OAuth2RestTemplate clientCredentialsRestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails(),this.oAuth2ClientContext);
//    }

    @Bean("egovaClientCredentialsResourceDetails")
    public ClientCredentialsResourceDetails resourceDetails() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId(oAuth2ClientProperties.getId());
        details.setAccessTokenUri(oAuth2ClientProperties.getAccessTokenUrl());
        details.setClientId(oAuth2ClientProperties.getClientId());
        details.setClientSecret(oAuth2ClientProperties.getClientSecret());
        details.setAuthenticationScheme(AuthenticationScheme.valueOf(oAuth2ClientProperties.getClientAuthenticationScheme()));
        return details;
    }


    @Bean("egovaOAuth2RestTemplate")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ClientContext oAuth2ClientContext) {
//        AccessTokenRequest request = new DefaultAccessTokenRequest();
//        OAuth2ClientContext context = new DefaultOAuth2ClientContext(request);
        final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails(), oAuth2ClientContext);
//        oAuth2RestTemplate.getInterceptors().add(this.loadBalancerInterceptor);
        return oAuth2RestTemplate;
    }
}