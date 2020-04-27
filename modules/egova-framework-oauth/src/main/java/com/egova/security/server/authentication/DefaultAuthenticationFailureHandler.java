package com.egova.security.server.authentication;

import com.egova.json.JsonMapping;
import com.egova.web.rest.ResponseResults;
import com.egova.security.core.LoginResponseType;
import com.egova.security.core.properties.BrowserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的认证失败结果处理器
 */
@EnableConfigurationProperties(BrowserProperties.class)
public class DefaultAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{

	@Autowired
	private BrowserProperties browserProperties;

	@Autowired
	JsonMapping jsonMapper;



	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
		logger.info("登录失败");
		if (LoginResponseType.json.equals(browserProperties.getSignInResponseType())) {
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			httpServletResponse.getWriter().write(jsonMapper.serialize(ResponseResults.error(e.getMessage())));
		} else {
			super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
		}
	}
}
