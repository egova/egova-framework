package com.egova.security.core.provider;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chendb
 * @description:
 * @date 2021-05-14 09:57:44
 */
public interface TokenExtractorProvider {

    String extractToken(HttpServletRequest request);

    Authentication extract(HttpServletRequest request);

}
