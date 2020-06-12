package com.egova.cloud.feign;

import com.egova.json.databind.ObjectMappingCustomer;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

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
@EnableConfigurationProperties({FeignClientProperties.class, FeignHttpClientProperties.class})
public class FeignAutoConfiguration {

    @Autowired(required = false)
    private List<FeignClientSpecification> configurations = new ArrayList<>();


    @Autowired(
            required = false
    )
    private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();


    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Contract feignContract(ConversionService feignConversionService) {
        return new DefaultSpringMvcContract(this.parameterProcessors, feignConversionService);
    }

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
     * 解码，默认基础上加入非ResponseResult接收类型自动拆包
     *
     * @return feignDecoder
     */
    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new ResponseResultDecoder(new SpringDecoder(feignHttpMessageConverter()))));
    }

    /**
     * 编码，防止序列化时联想
     *
     * @return feignEncoder
     */
    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(feignHttpMessageConverter());
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        // 之所以不从容器中获取对象，是考虑到如果被额外定制过，将会出现冲突，因此内部创建
        ObjectMappingCustomer disableAssociativeObjectMapping = new ObjectMappingCustomer(false);
        List<HttpMessageConverter<?>> converters = new ArrayList<>(messageConverters.getObject().getConverters());
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter converter = converters.get(i);
            // 下面防止feign在编码时，进行联想
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, new MappingJackson2HttpMessageConverter(disableAssociativeObjectMapping));
            }
        }
        return () -> new HttpMessageConverters(converters);
    }

}
