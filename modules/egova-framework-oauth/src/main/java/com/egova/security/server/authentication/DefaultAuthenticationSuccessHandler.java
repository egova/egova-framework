package com.egova.security.server.authentication;


import com.egova.json.JsonMapping;
import com.egova.rest.ResponseResults;
import com.egova.security.core.LoginResponseType;
import com.egova.security.core.properties.BrowserProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的认证成功的结果处理类
 */
@EnableConfigurationProperties(BrowserProperties.class)
public class DefaultAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


	@Autowired
	private BrowserProperties browserProperties;

	@Autowired
	JsonMapping jsonMapper;

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		logger.info("登录成功");

		if (LoginResponseType.json.equals(browserProperties.getSignInResponseType())) {
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			String simpleName = authentication.getClass().getSimpleName();
			httpServletResponse.getWriter().write(jsonMapper.serialize(ResponseResults.success(simpleName)));

		} else {

			// egova.security.browser.singInSuccessUrl，总是跳到设置的地址上
			// 如果没设置，则尝试跳转到登录之前访问的地址上，如果登录前访问地址为空，则跳到网站根路径上

			if (StringUtils.isNotBlank(browserProperties.getSignInSuccessUrl())) {
				requestCache.removeRequest(httpServletRequest, httpServletResponse);
				setAlwaysUseDefaultTargetUrl(true);
				setDefaultTargetUrl(browserProperties.getSignInSuccessUrl());
			}
			super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
		}
	}
}
