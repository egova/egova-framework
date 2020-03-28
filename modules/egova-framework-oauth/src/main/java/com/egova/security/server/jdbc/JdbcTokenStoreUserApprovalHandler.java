package com.egova.security.server.jdbc;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 这个需要手动创建不能注入，而对象容器只能存在一种实现
 * 
 * @author hbche
 * @date 2016年12月1日 下午11:55:08
 */
public class JdbcTokenStoreUserApprovalHandler extends TokenStoreUserApprovalHandler {

    /**
     * 构造函数
     * 
     * @param tokenStore 对应token存储实现
     * @param clientDetailsService 对应clientDetails服务实现
     */
    public JdbcTokenStoreUserApprovalHandler(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        this.setTokenStore(tokenStore);
        this.setClientDetailsService(clientDetailsService);
        this.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
    }


    @Override
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        if (super.isApproved(authorizationRequest, userAuthentication)) {
            return true;
        }
        return userAuthentication.isAuthenticated();

    }

}
