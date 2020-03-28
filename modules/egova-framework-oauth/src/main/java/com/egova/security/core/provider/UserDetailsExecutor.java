package com.egova.security.core.provider;

import com.flagwind.application.Application;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsExecutor
{
	private static Iterable<UserDetailsProvider> getServices()
	{
		return Application.resolveAll(UserDetailsProvider.class);
	}

	public static UserDetails execute(String username)
	{

		for(UserDetailsProvider provider : getServices())
		{
			UserDetails clientDetails = provider.loadUserByUsername(username);
			if(clientDetails != null)
			{
				return clientDetails;
			}
		}
		return null;
	}
}
