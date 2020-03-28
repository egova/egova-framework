package com.egova.security.server.jdbc;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 因为该对象住TokenStore 而TokenStore是手动创建的，所以此处不能用注入
 * 
 * @author chendb
 * @date 2016年12月2日 上午12:20:03
 */
public class JdbcOauthTokenServices extends DefaultTokenServices {

    /**
     * 构造函数
     * 
     * @param tokenStore token存储类
     * @param clientDetailsService ClientDetails服务类
     */
    public JdbcOauthTokenServices(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        this.setAccessTokenValiditySeconds(3600 * 24);
        this.setClientDetailsService(clientDetailsService);
        this.setSupportRefreshToken(true);
        this.setTokenStore(tokenStore);
    }

}
