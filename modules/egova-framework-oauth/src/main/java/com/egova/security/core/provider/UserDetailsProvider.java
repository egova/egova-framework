package com.egova.security.core.provider;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户信息提供器（注意：认证服务器使用）
 */
public interface UserDetailsProvider
{
	UserDetails loadUserByUsername(String username);
}
