package com.egova.cloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign OAuth2请求拦截器
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        String token = RequestUtils.getToken();
        if (token != null) {
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
        }
    }

}
