package com.egova.security.server.redis;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.util.Collection;

/**
 * Redis版本的TokenStoreUserApprovalHandler实现（是种服务组合）
 * 
 * @author chendb
 * @date 2016年12月8日 下午5:54:35
 */
public class RedisTokenStoreUserApprovalHandler extends ApprovalStoreUserApprovalHandler {

    private ClientDetailsService clientDetailsService;

    /**
     * 构造函数
     * @param tokenApprovalStore ApprovalStorep接口实现（这里可输入redis版本实现）
     * @param clientDetailsService ClientDetailsService接口实现（这里可输入redis版本实现）
     */
    public RedisTokenStoreUserApprovalHandler(ApprovalStore tokenApprovalStore,
            ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
        this.setApprovalStore(tokenApprovalStore);
        this.setClientDetailsService(clientDetailsService);
        this.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        this.setUseApprovalStore(true);

    }

    private boolean useApprovalStore = true;

    public void setUseApprovalStore(boolean useApprovalStore) {
        this.useApprovalStore = useApprovalStore;
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
            Authentication userAuthentication) {

        boolean approved = false;

        if (useApprovalStore) {
            authorizationRequest = super.checkForPreApproval(authorizationRequest, userAuthentication);
            approved = authorizationRequest.isApproved();
        }
        else {
            if (clientDetailsService != null) {
                Collection<String> requestedScopes = authorizationRequest.getScope();
                try {
                    ClientDetails client = clientDetailsService
                            .loadClientByClientId(authorizationRequest.getClientId());
                    for (String scope : requestedScopes) {
                        if (client.isAutoApprove(scope) || client.isAutoApprove("all")) {
                            approved = true;
                            break;
                        }
                    }
                }
                catch (ClientRegistrationException e) {
                }
            }
        }
        authorizationRequest.setApproved(approved);

        return authorizationRequest;

    }

    @Override
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        if (super.isApproved(authorizationRequest, userAuthentication)) {
            return true;
        }
        return userAuthentication.isAuthenticated();

    }

}
