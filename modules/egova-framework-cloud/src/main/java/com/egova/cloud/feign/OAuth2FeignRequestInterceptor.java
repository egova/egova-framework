package com.egova.cloud.feign;

import com.egova.cloud.FeignToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.util.stream.Collectors;

/**
 * Feign OAuth2请求拦截器
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private static final String TOKEN_OBTAIN = "%24obtain";

    private final OAuth2RestTemplate oAuth2RestTemplate;

    public OAuth2FeignRequestInterceptor(OAuth2RestTemplate oAuth2RestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @Override
    public void apply(RequestTemplate template) {


        if (template.queries().containsKey(TOKEN_OBTAIN)) {
            String obtain = template.queries().get(TOKEN_OBTAIN).stream().collect(Collectors.toList()).get(0);


            if (StringUtils.equalsIgnoreCase(obtain, FeignToken.Obtain.parent.name())) {
                String token = RequestUtils.getToken();
                if (!StringUtils.isEmpty(token)) {
                    template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, RequestUtils.getToken()));
                }
            } else {
                String token = oAuth2RestTemplate.getAccessToken().getValue();
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
            }
        } else {
            String token = RequestUtils.getToken();
            if (token != null) {
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
            }
        }

    }

}
