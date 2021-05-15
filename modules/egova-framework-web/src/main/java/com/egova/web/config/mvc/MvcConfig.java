package com.egova.web.config.mvc;

import com.egova.web.config.StaticResourceProperties;
import com.egova.web.converter.StringToBooleanConverter;
import com.egova.web.converter.StringToEnumConverterFactory;
import com.egova.web.converter.StringToTimestampConverter;
import com.egova.web.rest.DecoratingHandlerMapping;
import com.egova.web.rest.HandlerMethodInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

//@Configuration
@Slf4j
@EnableConfigurationProperties(StaticResourceProperties.class)
public class MvcConfig extends WebMvcConfigurationSupport {

    @Autowired(required = false)
    private List<HandlerMethodInterceptor> handlerMethodInterceptors;

    @Autowired
    private ObjectMapper objectMapper;


    @Value("${root.dir:/egova-apps/}")
    private String rootDir;


    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new DecoratingHandlerMapping(handlerMethodInterceptors);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (converters.isEmpty()) {
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converters.add(converter);
        }
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        }
        super.configureMessageConverters(converters);
    }

    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver());
        super.configureViewResolvers(registry);
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(getEnumConverterFactory());
        registry.addConverter(new StringToBooleanConverter());
        registry.addConverter(new StringToTimestampConverter());
        super.addFormatters(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        String resourceDir = rootDir;

        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/WEB-INF/", "classpath:/WEB-INF/", "classpath:/static/");

        if (!resourceDir.startsWith("file:")) {
            resourceDir = "file:" + resourceDir;
        }
        if (!resourceDir.endsWith("/")) {
            resourceDir = resourceDir + "/";
        }
        if (StringUtils.isNotBlank(resourceDir)) {
            log.info("静态文件映射 /***  -->" + resourceDir + "webApps/" + "  " + resourceDir + "webApps/root/");

            registry.addResourceHandler("/**").addResourceLocations(resourceDir + "webApps/",
                    resourceDir + "webApps/root/", "classpath:/resources/WEB-INF/", "classpath:/WEB-INF/", "classpath:/static/");

            log.info("静态文件映射 /files/**  -->" + resourceDir + "files/");
            registry.addResourceHandler("/files/**").addResourceLocations(resourceDir + "files/");
        }
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        assert this.getApplicationContext() != null;
        if (this.getApplicationContext().containsBean("loginLogInterceptor")) {
            HandlerInterceptorAdapter loginLogInterceptor = this.getApplicationContext().getBean("loginLogInterceptor", HandlerInterceptorAdapter.class);
            registry.addInterceptor(loginLogInterceptor);
        }
        super.addInterceptors(registry);
    }

    @Bean
    public StringToEnumConverterFactory getEnumConverterFactory() {
        return new StringToEnumConverterFactory();
    }

}
