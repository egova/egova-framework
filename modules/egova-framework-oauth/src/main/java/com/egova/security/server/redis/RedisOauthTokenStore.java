package com.egova.security.server.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 整个容器只能有一个TokenStore实现
 * 
 * @author chendb
 * @date 2016年12月2日 上午12:12:53
 */
public class RedisOauthTokenStore extends RedisTokenStore {

    /**
     * 构造函数
     * 
     * @param connectionFactory redis连接工厂
     */
    public RedisOauthTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

}
