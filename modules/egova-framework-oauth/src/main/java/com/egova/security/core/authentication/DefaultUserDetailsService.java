package com.egova.security.core.authentication;

import com.egova.security.core.provider.UserDetailsExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * 默认的UserDetailsService的实现，用于password认证时用户信息的查询
 */
public class DefaultUserDetailsService implements UserDetailsService
{
	private UserDetailsExecutor userDetailsExecutor;

	public DefaultUserDetailsService(UserDetailsExecutor UserDetailsExecutor) {
		this.userDetailsExecutor = UserDetailsExecutor;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		return Optional.ofNullable(userDetailsExecutor)
				.map(executor -> executor.execute(username))
				.orElse(null);
	}

}