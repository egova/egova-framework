package com.egova.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * redis默认配置
 */
@Configuration
@EnableConfigurationProperties({DefaultRedisConfig.DefaultRedisProperties.class})
public class DefaultRedisConfig extends RedisConfig {

	@Autowired
	private DefaultRedisProperties properties;

	@Bean(value={"lettuceConnectionFactory","connectionFactory"})
	public LettuceConnectionFactory lettuceConnectionFactory(){
		return getRedisConnectionFactory();
	}


	@Override
	public LettuceConnectionFactory getRedisConnectionFactory()
	{

		return this.connectionFactory(properties);
	}

	@ConfigurationProperties(prefix = "spring.redis")
	public static class DefaultRedisProperties extends RedisProperties{

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
