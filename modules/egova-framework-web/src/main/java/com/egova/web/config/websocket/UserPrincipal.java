package com.egova.web.config.websocket;

import java.security.Principal;

/**
 * 用户信息
 * @author chendb
 * @date 2017年3月8日 上午9:29:33
 */
public class UserPrincipal implements Principal{

	private String name;
	
	public UserPrincipal(String name){
		this.name=name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
