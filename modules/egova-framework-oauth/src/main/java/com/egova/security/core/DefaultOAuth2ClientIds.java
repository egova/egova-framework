package com.egova.security.core;


/**
 * 默认的Client资源常量
 * @author chenabao
 */
public class DefaultOAuth2ClientIds
{
	/**
	 * 联合资源clientId
	 */
	public static final String UNITY = "unity-client";

	/**
	 * 手机资源clientId
	 */
	public static final String MOBILE = "mobile-client";

	public static boolean contains(String key)
	{
		return key.equals(UNITY) || key.equals(MOBILE);
	}

}
