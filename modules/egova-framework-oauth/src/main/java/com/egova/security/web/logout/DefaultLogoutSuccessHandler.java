package com.egova.security.web.logout;

import com.egova.json.JsonMapping;
import com.egova.rest.ResponseResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
	private Log log = LogFactory.getLog(getClass());

	public DefaultLogoutSuccessHandler(JsonMapping jsonMapping, String signOutSuccessUrl) {
		this.signOutSuccessUrl = signOutSuccessUrl;
		this.jsonMapping = jsonMapping;
	}

	private String signOutSuccessUrl;

	private JsonMapping jsonMapping;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.web.authentication.logout.
	 * LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		log.info("退出成功");

		if (StringUtils.isBlank(signOutSuccessUrl)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jsonMapping.serialize(ResponseResults.success("退出成功")));
		} else {
			response.sendRedirect(signOutSuccessUrl);
		}

	}

}