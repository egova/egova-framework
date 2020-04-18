package com.egova.cache.config;

import com.egova.cache.CacheKeyGenerator;
import com.egova.cache.CacheMessageListener;
import com.egova.cache.RedisEhcacheCacheManager;
import com.egova.cache.RedisEhcacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Objects;

/**
 * 二级缓层方案的自动配置类
 */
//@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisEhcacheProperties.class)
public class CacheAutoConfiguration {

    @Autowired
    private RedisEhcacheProperties redisEhcacheProperties;

    @Bean
    public RedisEhcacheCacheManager cacheManager(@Autowired(required = false) RedisTemplate<Object, Object> redisTemplate) {
        return new RedisEhcacheCacheManager(redisEhcacheProperties, redisTemplate);
    }

    @Bean
    @ConditionalOnBean(RedisEhcacheCacheManager.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate,
                                                                       RedisEhcacheCacheManager redisEhcacheCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        CacheMessageListener cacheMessageListener = new CacheMessageListener(redisTemplate, redisEhcacheCacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(redisEhcacheProperties.getRedis().getTopic()));
        return redisMessageListenerContainer;
    }


    @Configuration
    public static class CustomCachingConfig extends CachingConfigurerSupport {

        @Override
        public KeyGenerator keyGenerator() {
            return new CacheKeyGenerator();
        }

    }

    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new CacheKeyGenerator();
    }


}
