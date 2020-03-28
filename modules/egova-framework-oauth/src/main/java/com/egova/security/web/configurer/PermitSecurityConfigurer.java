package com.egova.security.web.configurer;

import com.egova.security.core.configurer.HttpSecurityConfigurer;
import com.egova.security.core.properties.BrowserProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 非认证资源配置
 */
public class PermitSecurityConfigurer implements HttpSecurityConfigurer
{
	@Autowired
	private BrowserProperties browserProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception
	{

		http.authorizeRequests().antMatchers(
				"/oauth2/*",
				"/security/*",
				"/authentication/*",
				browserProperties.getSignInPage(),
				browserProperties.getSignUpUrl(),
				browserProperties.getSession().getSessionInvalidUrl()).permitAll();

		if (StringUtils.isNotBlank(browserProperties.getSignOutUrl())) {
			http.authorizeRequests().antMatchers(browserProperties.getSignOutUrl()).permitAll();
		}

		http.authorizeRequests()
				.antMatchers(
						"/resources/**",
						"/assets/**",
						"/**.bundle.js",
						"/images/**",
						"/partials/**",
						"/styles/**",
						"/content/**",
//						"index.*",
//						"login.*",
						"/static/**",
						"/files/**",
						"/copyright*",
						"/api-docs/**",
						"/swagger-resources/**",
						"/v2/api-docs",
						"/webjars/**",
						"/free/**",
						"/security/check_token").permitAll();


	}

}

