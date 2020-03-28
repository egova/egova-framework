package com.egova.security.core.authentication;

import com.egova.security.core.provider.ClientDetailsExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;


public class DefaultClientDetailsService implements ClientDetailsService
{
	private ClientDetailsService clientDetailsService;

	public DefaultClientDetailsService(ClientDetailsService clientDetailsService)
	{
		this.clientDetailsService = clientDetailsService;

	}


	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
	{
		if(StringUtils.isEmpty(clientId)){
			return null;
		}
		ClientDetails clientDetails = ClientDetailsExecutor.execute(clientId);
		if(clientDetails != null)
		{
			return clientDetails;
		}
		return clientDetailsService.loadClientByClientId(clientId);
	}
}
