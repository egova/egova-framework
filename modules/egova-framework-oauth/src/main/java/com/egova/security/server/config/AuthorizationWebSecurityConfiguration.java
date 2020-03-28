package com.egova.security.server.config;


import com.egova.security.core.authentication.DefaultUserDetailsService;
import com.egova.security.core.properties.BrowserProperties;
import com.egova.security.web.config.BrowserSecurityConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurity放在Resource过滤后面，其中Resource只处理/unity/ /m/ oauth资源
 */
//@ConditionalOnClass(AuthorizationServerAutoConfiguration.class)
@Order(10)
@EnableConfigurationProperties(BrowserProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class AuthorizationWebSecurityConfiguration extends BrowserSecurityConfiguration
{

	private final BeanFactory beanFactory;

	@Autowired
	public AuthorizationWebSecurityConfiguration(BeanFactory beanFactory)
	{

		this.beanFactory = beanFactory;
	}

	/**
	 * 默认认证器
	 */
	@Bean
	public DefaultUserDetailsService defaultUserDetailsService()
	{
		return new DefaultUserDetailsService();
	}

	@Bean(name = "daoAuthenticationProvider")
	public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, DefaultUserDetailsService userDetailsService)
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}


	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth)
	{

		AuthenticationProvider daoAuthenticationProvider = beanFactory.getBean("daoAuthenticationProvider", AuthenticationProvider.class);
		auth.authenticationProvider(daoAuthenticationProvider);
	}


}

