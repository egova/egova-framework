package com.egova.security.core.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientDetailsExecutor
{
	private ApplicationContext applicationContext;


	public ClientDetailsExecutor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private  Iterable<ClientDetailsProvider> getServices()
	{
		Map<String, ClientDetailsProvider> map = applicationContext.getBeansOfType(ClientDetailsProvider.class);
		List<ClientDetailsProvider> list = new ArrayList<>(map.values());
		AnnotationAwareOrderComparator.sort(list);
		return list;
	}

	public  ClientDetails execute(String clientId)
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
