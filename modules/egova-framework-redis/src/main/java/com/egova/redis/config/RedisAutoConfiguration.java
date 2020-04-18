package com.egova.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author chendb
 * @description: redis自动配置类
 * @date 2020-04-17 15:23:31
 */
@AutoConfigureBefore(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class RedisAutoConfiguration {


    @Primary
    @ConfigurationProperties(prefix = "spring.redis")
    public static class DefaultRedisProperties extends RedisProperties {

    }

    @Primary
    @Configuration
    @EnableConfigurationProperties({DefaultRedisProperties.class})
    public static class DefaultRedisConfig extends RedisConfig {


        private DefaultRedisProperties properties;

        public DefaultRedisConfig(DefaultRedisProperties properties) {
            this.properties = properties;
        }

        @Override
        public LettuceConnectionFactory getRedisConnectionFactory() {
            return this.connectionFactory(properties);
        }


        @Bean
        public LettuceConnectionFactory lettuceConnectionFactory() {
            return getRedisConnectionFactory();
        }

        @Bean
        public RedisTemplate redisTemplate() {
            RedisTemplate template = new RedisTemplate();
            template.setConnectionFactory(this.lettuceConnectionFactory());
            setSerializer(template);
            template.afterPropertiesSet();
            return template;
        }

        @Bean
        public StringRedisTemplate stringRedisTemplate() {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(this.lettuceConnectionFactory());
            template.afterPropertiesSet();
            return template;
        }

    }

}
