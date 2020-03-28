package com.egova.security.core.provider;

import org.springframework.core.Ordered;

import java.util.HashMap;
import java.util.Map;


/**
 * token附加信息提供器（注意：认证服务器使用）
 */
public interface TokenExtraProvider extends Ordered
{
	boolean match(String grantType, Map<String, String> parameters);

	void supply(String grantType, Map<String, String> parameters, HashMap<String, Object> context);
}
