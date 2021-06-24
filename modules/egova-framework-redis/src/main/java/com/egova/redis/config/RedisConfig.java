package com.egova.redis.config;


import com.egova.redis.FastJson2JsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


public abstract class RedisConfig
{
    private ConcurrentHashMap<Integer,LettuceConnectionFactory> factoryCache = new ConcurrentHashMap<>();

    private GenericObjectPoolConfig genericObjectPoolConfig(RedisProperties properties) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        RedisProperties.Pool pool = properties.getLettuce().getPool();
        config.setMaxTotal(properties.getLettuce().getPool().getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        return config;
    }


    public RedisStandaloneConfiguration redisStandaloneConfiguration(RedisProperties properties)
    {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPassword(RedisPassword.of(properties.getPassword()));
        config.setPort(properties.getPort());
        config.setDatabase(properties.getDatabase());
        return config;
    }


    public abstract LettuceConnectionFactory getRedisConnectionFactory();

    public LettuceConnectionFactory connectionFactory(RedisProperties properties)
    {
        RedisProperties.Lettuce lettuce = properties.getLettuce();
        long timeout = properties.getTimeout()!=null? properties.getTimeout().toMillis():5000;
        long shutdownTimeout = lettuce.getShutdownTimeout()!=null?lettuce.getShutdownTimeout().toMillis():10000;

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig(properties))
                .shutdownTimeout(Duration.ofMillis(shutdownTimeout))
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration(properties), clientConfig);
    }


    public LettuceConnectionFactory getConnectionFactory(int dbIndex) {
        Function<Integer, LettuceConnectionFactory> factoryFunction = (i) -> {
            LettuceConnectionFactory factory = getRedisConnectionFactory();
            factory.setDatabase(i);
            factory.afterPropertiesSet();

            return factory;
        };
        return factoryCache.computeIfAbsent(dbIndex, factoryFunction);
    }

    public  RedisTemplate getTemplate(int dbIndex)
    {
        LettuceConnectionFactory factory = getConnectionFactory(dbIndex);
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    public StringRedisTemplate getStringTemplate(int dbIndex) {
        LettuceConnectionFactory factory = getConnectionFactory(dbIndex);
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }


    public void setSerializer(RedisTemplate template) {

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        template.setValueSerializer(serializer);


        template.setKeySerializer(new StringRedisSerializer());
    }
}
