package com.egova.security.core.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 认证服务器配置
 */
@Data
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


}
