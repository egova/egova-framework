package com.egova.security.server.config;

import com.egova.security.core.authentication.DefaultClientDetailsService;
import com.egova.security.core.crypto.NoneOrBCryptPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 * @author chensoft
 */

public class AuthenticationBeanConfig
{

	/**
	 * 默认密码处理器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	public PasswordEncoder passwordEncoder()
	{
		return new NoneOrBCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint()
	{
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setTypeName("Basic");
		entryPoint.setRealmName("app/client");
		return entryPoint;
	}


	@Bean
	public DefaultClientDetailsService defaultClientDetailsService(ClientDetailsService clientDetailsService)
	{
		return new DefaultClientDetailsService(clientDetailsService);
	}


	/**
	 * redis ClientDetailsUserDetailsService 实现
	 *
	 * @return redis ClientDetailsUserDetailsService 实现
	 * @author chendb
	 * @date 2016年12月8日 下午5:48:12
	 */
	@Bean
	@ConditionalOnMissingBean(ClientDetailsUserDetailsService.class)
	public ClientDetailsUserDetailsService clientDetailsUserDetailsService(DefaultClientDetailsService clientDetailsService) {
		return new ClientDetailsUserDetailsService(clientDetailsService);
	}


	/**
	 * 访问控制决策器
	 * @return 访问控制决策器
	 * @author chendb
	 * @date 2016年12月8日 下午5:22:22
	 */
	@Bean
	public UnanimousBased accessDecisionManager()
	{
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
		decisionVoters.add(new ScopeVoter());
		decisionVoters.add(new RoleVoter());
		decisionVoters.add(new AuthenticatedVoter());
		return new UnanimousBased(decisionVoters);
	}


}
