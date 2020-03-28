package com.egova.security.core;

public class DefaultOAuth2ClientIds
{
	public static final String UNITY = "unity-client";

	public static final String MOBILE = "mobile-client";

	public static boolean contains(String key)
	{
		return key.equals(UNITY) || key.equals(MOBILE);
	}

}
