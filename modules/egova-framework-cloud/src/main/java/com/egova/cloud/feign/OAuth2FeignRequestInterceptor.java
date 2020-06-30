package com.egova.cloud.feign;

import com.egova.cloud.FeignToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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


        String url = RequestUtils.url();

        log.info("feign oauth client:{}", url);

        if (template.queries().containsKey(TOKEN_OBTAIN)) {
            String obtain = template.queries().get(TOKEN_OBTAIN).stream().collect(Collectors.toList()).get(0);
            if (StringUtils.equalsIgnoreCase(obtain, FeignToken.Obtain.local.name())) {
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, TokenHolder.current()));
                return;
            }

            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                // 非web环境
                return;
            }
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

//        if (token != null) {
//            if (!url.contains("/free/")) {
//                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
//            }
//        } else {
////            if (url.contains("/unity/")) {
////                template.header(AUTHORIZATION_HEADER,
////                        String.format("%s %s",
////                                BEARER_TOKEN_TYPE,
////                                oAuth2RestTemplate.getAccessToken().toString()));
//////                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
////            }
//        }
//        if (token != null && !url.contains("/free/")) {
//            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, token));
//        } else {
//            if (token == null && !url.contains("/free/")
////            log.info("feign oauth client1:{}", template.request().url());
////            log.info("feign oauth client2:{}", template.toString());
////            log.info("feign oauth client3:{}", template.url());
////            if (!template.request().url().contains("/free/")) {
////                // 作为个Client读Properties获取Token 用
////                log.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
////                template.header(AUTHORIZATION_HEADER,
////                        String.format("%s %s",
////                                BEARER_TOKEN_TYPE,
////                                oAuth2RestTemplate.getAccessToken().toString()));
////            }
//        }
    }

}
