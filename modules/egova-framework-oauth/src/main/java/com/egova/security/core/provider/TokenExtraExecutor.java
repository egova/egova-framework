package com.egova.security.core.provider;

import com.flagwind.application.Application;

import java.util.HashMap;
import java.util.Map;

public class TokenExtraExecutor
{
	private static Iterable<TokenExtraProvider> getServices()
	{
		return Application.resolveAll(TokenExtraProvider.class);
	}

	public static HashMap<String, Object> execute(String grantType,Map<String, String> parameters)
	{
		HashMap<String, Object> store = new HashMap<>();

		for(TokenExtraProvider ass : getServices())
		{
			if(ass.match(grantType, parameters))
			{
				ass.supply(grantType, parameters, store);
			}
		}

		return store;

	}
}
