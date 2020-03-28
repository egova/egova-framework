package com.egova.cache;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;


public class RedisEhcacheCacheManager implements CacheManager
{
	private static final Log log = LogFactory.getLog(RedisEhcacheCacheManager.class);

	private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

	private RedisEhcacheProperties redisEhcacheProperties;

	private RedisTemplate<Object, Object> redisTemplate;

	private boolean dynamic;

	private Set<String> cacheNames;

	private org.ehcache.CacheManager ehCacheManager;
	private CacheConfiguration<Object, Object> configuration;

	private ReentrantLock lock = new ReentrantLock();

	public RedisEhcacheCacheManager(RedisEhcacheProperties redisEhcacheProperties,
									RedisTemplate<Object, Object> redisTemplate)
	{
		super();
		this.redisEhcacheProperties = redisEhcacheProperties;
		if(redisTemplate == null)
		{
			if(this.redisEhcacheProperties.getCacheType() != CacheType.ehcache)
			{
				log.warn("redis缓存构建失败，自动切换成ehcache缓存");
				this.redisEhcacheProperties.setCacheType(CacheType.ehcache);
			}
		}
		this.redisTemplate = redisTemplate;
		this.dynamic = redisEhcacheProperties.isDynamic();
		this.cacheNames = redisEhcacheProperties.getCacheNames();

		setAboutEhCache();

	}
	private void setAboutEhCache(){
		long ehcacheExpire = redisEhcacheProperties.getEhcache().getExpireAfterWrite();
		this.configuration =
				CacheConfigurationBuilder
						.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(redisEhcacheProperties.getEhcache().getMaxEntry()))
						.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcacheExpire)))
						.build();
		this.ehCacheManager = CacheManagerBuilder
				.newCacheManagerBuilder()
				.build();
		this.ehCacheManager.init();
	}

	@Override
	public Cache getCache(String name) {

		String[] arr =name.split("#");
		name = arr[0];

		String expireKey = "";
		if(arr.length>1){
			expireKey = arr[1];
		}
		Cache cache = cacheMap.get(name);
		if(cache != null) {
			return cache;
		}
		if(!dynamic && !cacheNames.contains(name)) {
			return cache;
		}

		cache = new RedisEhcacheCache(name,expireKey, redisTemplate, getEhcache(name), redisEhcacheProperties);

		Cache oldCache = cacheMap.putIfAbsent(name, cache);
		log.debug(String.format("create cache instance, the cache name is : %s", name));
		return oldCache == null ? cache : oldCache;
	}

	private org.ehcache.Cache<Object, Object> getEhcache(String name) {

		try {
			lock.lock();
			org.ehcache.Cache<Object, Object> res = ehCacheManager.getCache(name, Object.class, Object.class);
			if (res != null) {
				return res;
			}

			return ehCacheManager.createCache(name, configuration);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.cacheNames;
	}

	public void clearLocal(String cacheName, Object key, Integer sender) {
		Cache cache = cacheMap.get(cacheName);
		if(cache == null) {
			return ;
		}

		RedisEhcacheCache redisEhcacheCache = (RedisEhcacheCache) cache;
		//如果是发送者本身发送的消息，就不进行key的清除
		if(sender==null|| redisEhcacheCache.getLocalCache().hashCode() != sender)
		{
			redisEhcacheCache.clearLocal(key);
		}
	}
}
