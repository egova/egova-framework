package com.egova.cloud.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chendb
 * @description:
 * @date 2020-06-28 13:35:11
 */
@Data
@ConfigurationProperties(prefix = "egova.oauth2.client")
public class OAuth2ClientProperties {
    private String id;
    private String accessTokenUrl;
    private String clientId;
    private String clientSecret;
    private String clientAuthenticationScheme;
}
