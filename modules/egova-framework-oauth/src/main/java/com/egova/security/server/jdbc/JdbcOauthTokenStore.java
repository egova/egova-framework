package com.egova.security.server.jdbc;

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * TokenStore 的jdbc实现
 * 
 * @author chendb
 * @date 2016年12月8日 下午5:40:11
 */
public class JdbcOauthTokenStore extends JdbcTokenStore {

    /**
     * 构造函数
     * 
     * @param dataSource 数据源
     */
    public JdbcOauthTokenStore(DataSource dataSource) {
        super(dataSource);
    }

}
