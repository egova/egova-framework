package com.egova.security.core;

import com.egova.security.core.provider.TokenExtractorExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chendb
 * @description:
 * @date 2021-05-14 09:55:26
 */
public class DefaultTokenExtractor extends BearerTokenExtractor {

    @Override
    public Authentication extract(HttpServletRequest request) {
        Authentication authentication = TokenExtractorExecutor.execute(request);
        if (authentication != null) {
            return authentication;
        }
        return super.extract(request);
    }
}
