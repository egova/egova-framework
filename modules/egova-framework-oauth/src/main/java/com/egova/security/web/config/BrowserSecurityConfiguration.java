package com.egova.security.web.config;

import com.egova.security.core.configurer.HttpSecurityConfigurerManager;
import com.egova.security.core.properties.SecurityProperties;
import com.egova.security.web.configurer.FormSecurityConfigurer;
import com.egova.security.web.configurer.PermitSecurityConfigurer;
import com.egova.security.web.configurer.SessionSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Import({BrowserSecurityBeanConfig.class/*, BasicSecurityConfigurer.class*/, FormSecurityConfigurer.class, PermitSecurityConfigurer.class, SessionSecurityConfigurer.class})
public class BrowserSecurityConfiguration extends WebSecurityConfigurerAdapter
{

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public HttpSecurityConfigurerManager httpSecurityConfigurerManager(){
        return new HttpSecurityConfigurerManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // 禁用csrf，否则feign调用401
        http.csrf().disable();
        httpSecurityConfigurerManager().config(http);

        switch (securityProperties.getXFrameOptions()){
            case ALLOW_FROM:
                http.headers().frameOptions().disable();
                break;
            case SAMEORIGIN:
                http.headers().frameOptions().sameOrigin();
                break;
            case DENY:
                http.headers().frameOptions().deny();
                break;
        }


    }

}

