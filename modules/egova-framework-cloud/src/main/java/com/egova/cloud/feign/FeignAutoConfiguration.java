package com.egova.cloud.feign;

import com.egova.json.JsonMapping;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Feign配置
 *
 * @author 奔波儿灞
 * @since 1.0
 */
//@Configuration
@ConditionalOnClass(Feign.class)
//@EnableFeignClients("com.egova")
@EnableConfigurationProperties({FeignClientProperties.class, FeignHttpClientProperties.class})
public class FeignAutoConfiguration {

    @Autowired(required = false)
    private List<FeignClientSpecification> configurations = new ArrayList<>();

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired
    private JsonMapping jsonMapping;

    @Bean
    public FeignContext defaultFeignContext() {
        FeignContext context = new FeignContext();
        context.setConfigurations(this.configurations);
        return context;
    }

    @Configuration
    @ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
    protected static class HystrixFeignTargeterConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public Targeter defaultFeignTargeter() {
            return new HystrixTargeter();
        }
    }

    @Configuration
    @ConditionalOnMissingClass("feign.hystrix.HystrixFeign")
    protected static class DefaultFeignTargeterConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public Targeter defaultFeignTargeter() {
            return new DefaultTargeter();
        }
    }

    /**
     * 注册OAuth2请求拦截器，将token传递到服务提供方
     *
     * @return OAuth2FeignRequestInterceptor
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }

    /**
     * 解码，默认基础上加入非OperateResult接收类型自动拆包
     *
     * @return feignDecoder
     */
    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new ResponseResultDecoder(new SpringDecoder(this.messageConverters), jsonMapping)));
    }

}
