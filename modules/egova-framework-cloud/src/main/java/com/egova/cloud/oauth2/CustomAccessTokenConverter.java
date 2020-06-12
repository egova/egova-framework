package com.egova.cloud.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 增加用户信息，供微服务直接获取
 * {
 *     "active": true,
 *     "exp": 1591624888,
 *     "user_name": "admin",
 *     "user_details": {},
 *     "authorities": [
 *         "Server"
 *     ],
 *     "client_id": "unity-client",
 *     "scope": [
 *         "read",
 *         "write"
 *     ]
 * }
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    static final String USER_DETAILS = "user_details";

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> response = new HashMap<>(super.convertAccessToken(token, authentication));
        response.put(USER_DETAILS, authentication.getPrincipal());
        return response;
    }

}
