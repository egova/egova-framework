package com.egova.security.web.configurer;

import com.egova.security.core.configurer.HttpSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * basic认证
 */
public class FormSecurityConfigurer implements HttpSecurityConfigurer
{

	@Autowired
	private AuthenticationFailureHandler defaultAuthenticationFailureHandler;

	@Autowired
	private AuthenticationSuccessHandler defaultAuthenticationSuccessHandler;


	@Override
	public void configure(HttpSecurity http) throws Exception
	{

		http.formLogin()
				.loginPage("/authentication/require")
				.loginProcessingUrl("/authentication/form")
				.successHandler(defaultAuthenticationSuccessHandler)
				.failureHandler(defaultAuthenticationFailureHandler);

	}


}
