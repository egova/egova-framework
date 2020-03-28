package com.egova.security.core.configurer;

import com.flagwind.application.Application;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;


public class HttpSecurityConfigurerManager
{

	private List<HttpSecurityConfigurer> getConfigures(){
		return Application.resolveAll(HttpSecurityConfigurer.class);
	}

	public void config(HttpSecurity http) throws Exception
	{

		for(HttpSecurityConfigurer configurer : getConfigures())
		{
			configurer.configure(http);
		}
	}
}
