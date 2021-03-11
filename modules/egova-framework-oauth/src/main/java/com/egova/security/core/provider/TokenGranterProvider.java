package com.egova.security.core.provider;

import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;


/**
 * token附加信息提供器（注意：认证服务器使用）
 */
public interface TokenGranterProvider extends Ordered {

	String name();

	TokenGranter supply(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory);
}
