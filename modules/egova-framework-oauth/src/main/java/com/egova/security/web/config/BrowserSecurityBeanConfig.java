package com.egova.security.web.config;

import com.egova.json.JsonMapping;
import com.egova.security.core.properties.BrowserProperties;
import com.egova.security.core.properties.SecurityProperties;
import com.egova.security.web.logout.DefaultLogoutSuccessHandler;
import com.egova.security.web.session.ExpiredSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@EnableConfigurationProperties({BrowserProperties.class, SecurityProperties.class})
public class BrowserSecurityBeanConfig {

    @Autowired
    private BrowserProperties browserProperties;

    @Autowired
    private JsonMapping jsonMapping;

    /**
     * session失效时的处理策略配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new com.egova.security.web.session.InvalidSessionStrategy(jsonMapping, browserProperties);
    }

    /**
     * 并发登录导致前一个session失效时的处理策略配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new ExpiredSessionStrategy(jsonMapping, browserProperties);
    }

    /**
     * 退出时的处理策略配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DefaultLogoutSuccessHandler(jsonMapping, browserProperties.getSignOutUrl());
    }


}
