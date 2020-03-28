package com.egova.security.core.properties;

import com.egova.security.core.LoginResponseType;
import com.egova.security.core.TokenStorageType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "egova.security.browser")
public class BrowserProperties
{


	private TokenStorageType tokenStorage = TokenStorageType.redis;
	/**
	 * session管理配置项
	 */
	private SessionProperties session = new SessionProperties();
	/**
	 * 登录页面，当引发登录行为的url以html结尾时，会跳到这里配置的url上
	 */
	private String signInPage = "/login.html";
	/**
	 * '记住我'功能的有效时间，默认1小时
	 */
	private int rememberMeSeconds = 3600;
	/**
	 * 退出成功时跳转的url，如果配置了，则跳到指定的url，如果没配置，则返回json数据。
	 */
	private String signOutUrl;
	/**
	 * 社交登录，如果需要用户注册，跳转的页面
	 */
	private String signUpUrl = "/logout.html";
	/**
	 * 登录响应的方式，默认是json
	 */
	private LoginResponseType signInResponseType = LoginResponseType.json;
	/**
	 * 登录成功后跳转的地址，如果设置了此属性，则登录成功后总是会跳到这个地址上。
	 *
	 * 只在signInResponseType为REDIRECT时生效
	 */
	private String signInSuccessUrl;

	public SessionProperties getSession()
	{
		return session;
	}

	public void setSession(SessionProperties session)
	{
		this.session = session;
	}

	public TokenStorageType getTokenStorage()
	{
		return tokenStorage;
	}

	public void setTokenStorage(TokenStorageType tokenStorage)
	{
		this.tokenStorage = tokenStorage;
	}

	public String getSignInPage()
	{
		return signInPage;
	}

	public void setSignInPage(String signInPage)
	{
		this.signInPage = signInPage;
	}

	public int getRememberMeSeconds()
	{
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds)
	{
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public String getSignOutUrl()
	{
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl)
	{
		this.signOutUrl = signOutUrl;
	}

	public String getSignUpUrl()
	{
		return signUpUrl;
	}

	public void setSignUpUrl(String signUpUrl)
	{
		this.signUpUrl = signUpUrl;
	}

	public LoginResponseType getSignInResponseType()
	{
		return signInResponseType;
	}

	public void setSignInResponseType(LoginResponseType signInResponseType)
	{
		this.signInResponseType = signInResponseType;
	}

	public String getSignInSuccessUrl()
	{
		return signInSuccessUrl;
	}

	public void setSignInSuccessUrl(String signInSuccessUrl)
	{
		this.signInSuccessUrl = signInSuccessUrl;
	}

	public static class SessionProperties {
		/**
		 * 同一个用户在系统中的最大session数，默认1
		 */
		private int maximumSessions = 1;
		/**
		 * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
		 */
		private boolean maxSessionsPreventsLogin;
		/**
		 * session失效时跳转的地址
		 */
		private String sessionInvalidUrl = "/invalid.html";

		public int getMaximumSessions() {
			return maximumSessions;
		}

		public void setMaximumSessions(int maximumSessions) {
			this.maximumSessions = maximumSessions;
		}

		public boolean isMaxSessionsPreventsLogin() {
			return maxSessionsPreventsLogin;
		}

		public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
			this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
		}

		public String getSessionInvalidUrl() {
			return sessionInvalidUrl;
		}

		public void setSessionInvalidUrl(String sessionInvalidUrl) {
			this.sessionInvalidUrl = sessionInvalidUrl;
		}
	}
}
