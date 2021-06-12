package com.egova.security.core.authentication;

import com.egova.exception.ExceptionUtils;
import com.egova.security.core.provider.ClientDetailsExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.Optional;


/**
 * 默认的ClientDetails的服务类，用于client认证详情信息查询
 */
public class DefaultClientDetailsService implements ClientDetailsService {
	private ClientDetailsService clientDetailsService;

	private SecurityBuilder<ClientDetailsService> securityBuilder;

	private ClientDetailsExecutor clientDetailsExecutor;

	public DefaultClientDetailsService(ClientDetailsExecutor clientDetailsExecutor) {
		this.clientDetailsExecutor = clientDetailsExecutor;

	}

	private SecurityBuilder<ClientDetailsService> getBuilder() {
		return securityBuilder;
	}

	public void setBuilder(SecurityBuilder<ClientDetailsService> securityBuilder) {
		this.securityBuilder = securityBuilder;
	}

	private ClientDetailsService getInnerService() {
		if (clientDetailsService != null) {
			return clientDetailsService;
		}
		if (getBuilder() == null) {
			return null;
		}
		try {
			clientDetailsService = getBuilder().build();
			return clientDetailsService;

		} catch (Exception ex) {
			throw ExceptionUtils.framework("InMemoryClientDetailsServiceBuilder生成服务异常");
		}
	}


	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		if (StringUtils.isEmpty(clientId)) {
			return null;
		}
		ClientDetails clientDetails = clientDetailsExecutor.execute(clientId);
		if (clientDetails != null) {
			return clientDetails;
		}

		return Optional.ofNullable(getInnerService()).map(s -> s.loadClientByClientId(clientId)).orElse(null);
	}

}
