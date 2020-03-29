package com.egova.web.config;

import com.egova.json.databind.ObjectMappingCustomer;
import com.egova.web.converter.StringToBooleanConverter;
import com.egova.web.converter.StringToEnumConverterFactory;
import com.egova.web.converter.StringToTimestampConverter;
import com.egova.web.restful.DecoratingHandlerMapping;
import com.egova.web.restful.HandlerMethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;


@Configuration
public class MvcConfig extends WebMvcConfigurationSupport
{
	private static final Log LOG = LogFactory.getLog(MvcConfig.class);


	@Value("${root.dir:/egova-apps}")
	private String rootDir;

	@Value("${egova.web.static.file-dir:}")
	private String fileDir;

	@Value("${egova.web.static.file-site:files}")
	private String fileSite;

	@Autowired(required = false)
	private List<HandlerMethodInterceptor> handlerMethodInterceptors;



	@Override
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping()
	{
		return new DecoratingHandlerMapping(handlerMethodInterceptors);
	}

	@Override
	protected void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		ObjectMappingCustomer objectMappingCustomer = new ObjectMappingCustomer();
		if (converters.isEmpty()) {
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converters.add(converter);
		}
		for (HttpMessageConverter converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMappingCustomer);
			}
		}
		super.configureMessageConverters(converters);
	}

	@Override
	protected void configureViewResolvers(ViewResolverRegistry registry)
	{
		registry.viewResolver(new InternalResourceViewResolver());
		super.configureViewResolvers(registry);
	}

	@Override
	protected void addFormatters(FormatterRegistry registry)
	{
		registry.addConverterFactory(getEnumConverterFactory());
		registry.addConverter(new StringToBooleanConverter());
		registry.addConverter(new StringToTimestampConverter());
		super.addFormatters(registry);
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "WEB-INF/static/");

		if (!rootDir.startsWith("file")) {
			rootDir = "file:" + rootDir;
		}
		if (StringUtils.isNotBlank(rootDir)) {

			LOG.info("静态文件映射 /***  -->" + rootDir + "webApps/" + "  " + rootDir + "webApps/root/");
			registry.addResourceHandler("/**").addResourceLocations(rootDir + "webApps/", rootDir + "webApps/root/", "classpath:/static/", "WEB-INF/static/");
		}

		if (StringUtils.isNotBlank(rootDir)) {
			registry.addResourceHandler("/logs/**").addResourceLocations(rootDir + "logs/");
			LOG.info("静态文件映射 /logs/**  -->" + rootDir + "logs/");
		}


		if (StringUtils.isBlank(fileDir)) {
			fileDir = rootDir;
		} else {
			if (!fileDir.startsWith("file")) {
				fileDir = "file:" + fileDir;
			}
		}
		registry.addResourceHandler("/" + fileSite + "/**").addResourceLocations(fileDir + fileSite + "/");
		LOG.info("静态文件映射 /" + fileSite + "/**  -->" + fileDir + fileSite + "/");

		super.addResourceHandlers(registry);
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry)
	{
		assert this.getApplicationContext() != null;
		if(this.getApplicationContext().containsBean("loginLogInterceptor"))
		{
			HandlerInterceptorAdapter loginLogInterceptor = this.getApplicationContext().getBean("loginLogInterceptor", HandlerInterceptorAdapter.class);
			registry.addInterceptor(loginLogInterceptor);
		}
		super.addInterceptors(registry);
	}

	@Bean
	public StringToEnumConverterFactory getEnumConverterFactory()
	{
		return new StringToEnumConverterFactory();
	}

}
