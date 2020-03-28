package com.egova.security.core.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 认证服务器配置
 */
@ConfigurationProperties(prefix = "egova.security.authentication")
public class AuthenticationProperties
{
	/**
	 * access token验证时长（秒）
	 */
	private int accessTokenValiditySeconds = 12*60*60;

	/**
	 * refresh token 验证时长（秒）
	 */
	private int refreshTokenValiditySeconds = 12*60*60;

	/**
	 * 是否支持 refresh token 功能
	 */
	private boolean supportRefreshToken = true;

	/**
	 * 是否可回收refresh token
	 */
	private boolean reuseRefreshToken = true;

	public boolean isSupportRefreshToken()
	{
		return supportRefreshToken;
	}

	public void setSupportRefreshToken(boolean supportRefreshToken)
	{
		this.supportRefreshToken = supportRefreshToken;
	}

	public boolean isReuseRefreshToken()
	{
		return reuseRefreshToken;
	}

	public void setReuseRefreshToken(boolean reuseRefreshToken)
	{
		this.reuseRefreshToken = reuseRefreshToken;
	}

	public int getAccessTokenValiditySeconds()
	{
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds)
	{
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public int getRefreshTokenValiditySeconds()
	{
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds)
	{
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
}
