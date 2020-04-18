package com.egova.security.core.configurer;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象的HttpSecurityConfigurer的管理者
 */

public class HttpSecurityConfigurerManager
{

	private org.springframework.context.ApplicationContext context;

	public HttpSecurityConfigurerManager(org.springframework.context.ApplicationContext context) {
		this.context = context;
	}

	private List<HttpSecurityConfigurer> getConfigures() {
		Map<String, HttpSecurityConfigurer> map = context.getBeansOfType(HttpSecurityConfigurer.class);
		List<HttpSecurityConfigurer> list = new ArrayList<>(map.values());
		AnnotationAwareOrderComparator.sort(list);
		return list;
	}

	public void config(HttpSecurity http) throws Exception
	{

		for(HttpSecurityConfigurer configurer : getConfigures())
		{
			configurer.configure(http);
		}
	}
}
