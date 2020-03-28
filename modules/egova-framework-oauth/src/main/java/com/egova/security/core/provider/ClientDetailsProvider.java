package com.egova.security.core.provider;

import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * 资源信息提供器（注意：认证服务器使用）
 */
public interface ClientDetailsProvider
{
	ClientDetails loadClientByClientId(String clientId);
}
