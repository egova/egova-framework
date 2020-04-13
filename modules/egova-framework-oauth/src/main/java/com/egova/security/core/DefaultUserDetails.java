package com.egova.security.core;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 默认的用户详情
 */
public class DefaultUserDetails extends org.springframework.security.core.userdetails.User
{
	private String id;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public DefaultUserDetails(String userId, String username, String password, Collection<? extends GrantedAuthority> authorities)
	{
		super(username, password, authorities);
		this.id = userId;
	}

	public DefaultUserDetails(String userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
	{
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = userId;
	}
}
