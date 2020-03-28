package com.egova.security.core.provider;

import com.flagwind.application.Application;
import org.springframework.security.oauth2.provider.ClientDetails;

public class ClientDetailsExecutor
{
	private static Iterable<ClientDetailsProvider> getServices()
	{
		return Application.resolveAll(ClientDetailsProvider.class);
	}

	public static ClientDetails execute(String clientId)
	{

		for(ClientDetailsProvider provider : getServices())
		{
			ClientDetails clientDetails = provider.loadClientByClientId(clientId);
			if(clientDetails != null)
			{
				return clientDetails;
			}
		}
		return null;
	}
}
