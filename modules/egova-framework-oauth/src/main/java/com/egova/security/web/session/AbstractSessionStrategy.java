package com.egova.security.web.session;


import com.egova.security.core.properties.BrowserProperties;
import com.egova.utils.ResponseResultUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 抽象的session失效处理器
 *
 * @author zhailiang
 *
 */
public class AbstractSessionStrategy {

	private final Log logger = LogFactory.getLog(getClass());
	/**
	 * 跳转的url
	 */
	private String destinationUrl;
	/**
	 * 系统配置信息
	 */
	private BrowserProperties browserProperties;
	/**
	 * 重定向策略
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	/**
	 * 跳转前是否创建新的session
	 */
	private boolean createNewSession = true;

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 */
	public AbstractSessionStrategy(BrowserProperties browserProperties) {
		String invalidSessionUrl = browserProperties.getSession().getSessionInvalidUrl();
		Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
		Assert.isTrue(StringUtils.endsWithIgnoreCase(invalidSessionUrl, ".html"), "url must end with '.html'");
		this.destinationUrl = invalidSessionUrl;
		this.browserProperties = browserProperties;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.web.session.InvalidSessionStrategy#
	 * onInvalidSessionDetected(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		logger.info("session失效");

		if (createNewSession) {
			request.getSession();
		}

		String sourceUrl = request.getRequestURI();
		String targetUrl;

		if (StringUtils.contains(sourceUrl, ".html")) {
			if(StringUtils.equals(sourceUrl, browserProperties.getSignInPage())
					|| StringUtils.equals(sourceUrl,  browserProperties.getSignOutUrl())){
				targetUrl = sourceUrl;
			}else{
				targetUrl = destinationUrl;
			}
			logger.info("跳转到:"+targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
		} else {
			Object result = buildResponseContent(request);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(result));
		}

	}

	/**
	 * @param request
	 * @return
	 */
	protected Object buildResponseContent(HttpServletRequest request) {
		String message = "session已失效";
		if (isConcurrency()) {
			message = message + "，有可能是并发登录导致的";
		}
		return  ResponseResultUtils.error(message);
	}

	/**
	 * session失效是否是并发导致的
	 *
	 * @return
	 */
	protected boolean isConcurrency() {
		return false;
	}

	/**
	 * Determines whether a new session should be created before redirecting (to
	 * avoid possible looping issues where the same session ID is sent with the
	 * redirected request). Alternatively, ensure that the configured URL does
	 * not pass through the {@code SessionManagementFilter}.
	 *
	 * @param createNewSession
	 *            defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

}