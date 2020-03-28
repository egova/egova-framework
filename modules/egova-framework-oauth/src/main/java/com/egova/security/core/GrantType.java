package com.egova.security.core;


public enum  GrantType
{
	/**
	 * 授权码认证
	 */
	code("authorization_code"),
	/**
	 * 隐氏授权
	 */
	implicit("implicit"),
	/**
	 * 密码授权
	 */
	password("password"),
	/**
	 * 客户端授权
	 */
	client("client_credentials"),
	/**
	 * 刷新Token
	 */
	refreshToken("refresh_token")
	;

	private final String value;

	GrantType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
