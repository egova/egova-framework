package com.egova.cloud.web;

import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * RestTemplate增加LoadBalancer能力
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Configuration
public class RestTemplateAutoConfiguration {

    private static final int CONNECT_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 5000;

    private final HttpClient client;

    public RestTemplateAutoConfiguration(HttpClient client) {
        this.client = client;
    }

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 设置UTF-8
        restTemplate.getMessageConverters().add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // 设置HttpClient
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        // If the endpoint returns a 400 response, this indicates that the token is invalid.
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode() != HttpStatus.BAD_REQUEST) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }

}
