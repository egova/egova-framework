package com.egova.security.core.authentication;

import com.egova.security.core.provider.UserDetailsExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultUserDetailsService implements UserDetailsService
{

	private Log logger = LogFactory.getLog(getClass());


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		return UserDetailsExecutor.execute(username);
//		logger.warn("请配置 UserDetailsService 接口的实现.");
//		throw new UsernameNotFoundException(username);
	}

}