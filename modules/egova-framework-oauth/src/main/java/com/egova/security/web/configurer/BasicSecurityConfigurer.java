package com.egova.security.web.configurer;

import com.egova.security.core.configurer.HttpSecurityConfigurer;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * basic认证
 */
public class BasicSecurityConfigurer implements HttpSecurityConfigurer
{
	private static final String SWAGGER_URL = "/swagger-ui.html";

	@Override
	public void configure(HttpSecurity http) throws Exception
	{

		http.requestMatcher(new LuminaryRequestedMatcher()).httpBasic().and().authorizeRequests()
				// swagger页面需要添加登录校验
				.antMatchers(SWAGGER_URL).authenticated()
				// 监控节点需要添加登录校验
				.requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated().and()
				// 允许刷新服务
				.csrf();

	}

	private static class LuminaryRequestedMatcher implements RequestMatcher
	{
		@Override
        public boolean matches(HttpServletRequest request) {
			AntPathRequestMatcher swaggerRequestMatcher = new AntPathRequestMatcher(SWAGGER_URL);
			EndpointRequest.EndpointRequestMatcher endpointRequestMatcher = EndpointRequest.toAnyEndpoint();
			return swaggerRequestMatcher.matches(request) || endpointRequestMatcher.matches(request);
		}
	}


}
