package com.egova.security.web.configurer;

import com.egova.security.core.configurer.HttpSecurityConfigurer;
import com.egova.security.core.properties.BrowserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 非认证资源配置
 */

public class SessionSecurityConfigurer implements HttpSecurityConfigurer
{

	@Autowired
	private BrowserProperties browserProperties;

	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;



	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		// 会话配置
		http.sessionManagement()
				.invalidSessionStrategy(invalidSessionStrategy)
				.maximumSessions(browserProperties.getSession().getMaximumSessions())
				.maxSessionsPreventsLogin(browserProperties.getSession().isMaxSessionsPreventsLogin())
				.expiredSessionStrategy(sessionInformationExpiredStrategy)
				.and()
				.and();


	}

}

