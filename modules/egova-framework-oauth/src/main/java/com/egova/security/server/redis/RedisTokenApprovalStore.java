package com.egova.security.server.redis;

import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Redis版本Token批准存储实现接口
 * 
 * @author chendb
 * @date 2016年12月8日 下午5:53:56
 */
public class RedisTokenApprovalStore extends TokenApprovalStore {

    /**
     * 构造函数
     * 
     * @param tokenStore TokenStore实现接口（这是应该传入redis版本实现）
     */
    public RedisTokenApprovalStore(TokenStore tokenStore) {
        this.setTokenStore(tokenStore);
    }

}