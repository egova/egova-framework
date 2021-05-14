package com.egova.security.core.provider;

import com.flagwind.application.Application;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class TokenExtractorExecutor {

	private static Iterable<TokenExtractorProvider> getServices() {
		return Application.resolveAll(TokenExtractorProvider.class);
	}

	public static Authentication execute(HttpServletRequest request) {

		for (TokenExtractorProvider ass : getServices()) {
			String token = ass.extractToken(request);
			if (StringUtils.isNotEmpty(token)) {
				return ass.extract(request);
			}
		}
		return null;
	}

}
