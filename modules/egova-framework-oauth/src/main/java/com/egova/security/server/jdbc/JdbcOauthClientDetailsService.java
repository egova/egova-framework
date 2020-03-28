package com.egova.security.server.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * jdbc oauth_client_details数据查询服务
 * 
 * @author chendb
 * @date 2016年12月8日 下午5:37:01
 */
@Component
public class JdbcOauthClientDetailsService extends JdbcClientDetailsService {

    private static final String SELECT_CLIENT_DETAILS_SQL = "select client_id, client_secret, "
            + "resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, "
            + "access_token_validity, refresh_token_validity, additional_information, autoapprove "
            + "from oauth_client_details where client_id = ? and archived = 0 ";

    /**
     * 构造函数
     * 
     * @param dataSource 数据源
     */
    @Autowired
    public JdbcOauthClientDetailsService(DataSource dataSource) {
        super(dataSource);
        setSelectClientDetailsSql(SELECT_CLIENT_DETAILS_SQL);
    }

}