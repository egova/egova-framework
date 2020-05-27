package com.egova.security.web.configurer;

import com.egova.security.core.configurer.HttpSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * basic认证
 */
public class FormSecurityConfigurer implements HttpSecurityConfigurer
{

	/**
	 * 在微服务中，认证实现在security-server中，其他服务无实现类。
	 */
	@Autowired(required = false)
	private AuthenticationFailureHandler defaultAuthenticationFailureHandler;

	@Autowired(required = false)
	private AuthenticationSuccessHandler defaultAuthenticationSuccessHandler;


	@Override
	public void configure(HttpSecurity http) throws Exception
	{

		FormLoginConfigurer<HttpSecurity> configurer = http.formLogin()
				.loginPage("/authentication/required")
				.loginProcessingUrl("/authentication/form");

		if (defaultAuthenticationSuccessHandler != null) {
			configurer.successHandler(defaultAuthenticationSuccessHandler);
		}
		if (defaultAuthenticationFailureHandler != null) {
			configurer.failureHandler(defaultAuthenticationFailureHandler);
		}
	}


}
