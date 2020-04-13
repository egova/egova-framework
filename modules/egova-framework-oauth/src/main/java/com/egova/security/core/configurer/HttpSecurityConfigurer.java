package com.egova.security.core.configurer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 抽象的Sercurity配置接口
 */
public interface HttpSecurityConfigurer
{
	void configure(HttpSecurity http) throws Exception;
}
