package com.egova.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存消息监听
 */
@Slf4j
public class CacheMessageListener implements MessageListener {

    private RedisTemplate<Object, Object> redisTemplate;

    private RedisEhcacheCacheManager redisEhcacheCacheManager;


    public CacheMessageListener(RedisTemplate<Object, Object> redisTemplate, RedisEhcacheCacheManager redisEhcacheCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.redisEhcacheCacheManager = redisEhcacheCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage;
        try {
            cacheMessage = JSONObject.parseObject(message.getBody(), CacheMessage.class);
            log.debug(String.format("receive a redis topic message, clear local cache, the cacheName is %s, the key is %s", cacheMessage.getCacheName(), cacheMessage.getKey()));
            redisEhcacheCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey(), cacheMessage.getSender());
        } catch (Exception e) {
            log.warn("缓存自动清理失败", e);
        }

    }

}

