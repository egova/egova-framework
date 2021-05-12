package com.egova.cache;


import com.egova.web.rest.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Slf4j
public class RedisEhcacheCache extends AbstractValueAdaptingCache {

    private static final Pattern DURATION_PATTERN =
            Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?" +
                            "(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?",
                    Pattern.CASE_INSENSITIVE);
    private String name;

    private String expireKey;

    private RedisTemplate<Object, Object> redisTemplate;

    private org.ehcache.Cache<Object, Object> ehcacheCache;

    private String cachePrefix;

    private CacheType cacheType;

    private Duration defaultExpiration = Duration.ofSeconds(5 * 60);

    private Map<String, Duration> expires;

    private String topic = "cache:redis:ehcache:topic";

    private CacheKeys cacheKeys;

    public static class CacheKeys {
        private Set<Object> keys;
        private LocalDateTime expireTime;

        public CacheKeys(Set<Object> keys ) {
            this.keys = keys;
            expireTime = LocalDateTime.now().minusMinutes(5);
        }

        private boolean expired() {
            return LocalDateTime.now().isBefore(this.expireTime);
        }
    }

    protected RedisEhcacheCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public RedisEhcacheCache(String name, String expireKey, RedisTemplate<Object, Object> redisTemplate, Cache<Object, Object> ehcacheCache, RedisEhcacheProperties redisEhcacheProperties) {
        super(redisEhcacheProperties.isCacheNullValues());
        this.name = name;
        this.expireKey = expireKey;
        this.redisTemplate = redisTemplate;
        this.ehcacheCache = ehcacheCache;
        this.cacheType = redisEhcacheProperties.getCacheType();
        this.cachePrefix = redisEhcacheProperties.getCachePrefix();
        this.defaultExpiration = redisEhcacheProperties.getRedis().getDefaultExpiration();
        this.expires = redisEhcacheProperties.getRedis().getExpires();
        this.topic = redisEhcacheProperties.getRedis().getTopic();
    }

    public RedisEhcacheCache(String name, RedisTemplate<Object, Object> redisTemplate, Cache<Object, Object> ehcacheCache, RedisEhcacheProperties redisEhcacheProperties) {

        this(name, name, redisTemplate, ehcacheCache, redisEhcacheProperties);
    }

    public String getExpireKey() {
        if (StringUtils.isNotBlank(expireKey)) {
            return expireKey;
        }
        return this.name;
    }

    public void setExpireKey(String expireKey) {
        this.expireKey = expireKey;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (value != null) {
            return (T) value;
        }

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            value = lookup(key);
            if (value != null) {
                return (T) value;
            }
            value = valueLoader.call();
            Object storeValue = toStoreValue(valueLoader.call());
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            try {
                Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
                Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
                throw (RuntimeException) constructor.newInstance(key, valueLoader, e.getCause());
            } catch (Exception e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            lock.unlock();
        }
    }

    //从持久层读取value，然后存入缓存。允许value = null
    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }
        // 检测值是否有必要缓存
        if (!checkValue(value)) {
            return;
        }
        Duration expire = getExpire();

        // redis 缓存存储策略
        if (this.cacheType != CacheType.ehcache) {
            log.info("redis缓存，key:{},value:{}", key, value);

            try {
                Object cacheKey = getKey(key);
                if (expire.toMillis() > 0) {
                    redisTemplate.opsForValue().set(cacheKey, toStoreValue(value), expire.toMillis(), TimeUnit.MILLISECONDS);
                } else {
                    redisTemplate.opsForValue().set(cacheKey, toStoreValue(value), defaultExpiration.toMillis(), TimeUnit.MILLISECONDS);
                }
                addCacheKey(cacheKey);
            } catch (Exception ex) {
                if (this.cacheType == CacheType.redis) {
                    throw ex;
                }
            }

            // 通过redis推送消息，使其他服务的ehcache失效。
            // 原来的有个缺点：服务1给缓存put完KV后推送给redis的消息，服务1本身也会接收到该消息，
            // 然后会将刚刚put的KV删除。这里把ehcacheCache的hashcode传过去，避免这个问题。
            push(new CacheMessage(this.name, key, this.ehcacheCache.hashCode()));
            if (value == null) {
                return;
            }
        }

        if (this.cacheType != CacheType.redis) {
            log.info("ehcache缓层，key:{},value:{}", key, value);
            ehcacheCache.put(key, value);
        }
    }

    /**
     * key的生成 如：name:cachePrefix:key
     *
     * @param key
     * @return
     */
    private Object getKey(Object key) {
        return this.name.concat(":").concat(StringUtils.isEmpty(cachePrefix) ? key.toString() : cachePrefix.concat(":").concat(key.toString()));
    }

    private Duration getExpire() {

        Duration expire = defaultExpiration;
        Duration cacheNameExpire = expires.get(this.getExpireKey());
        if (DURATION_PATTERN.matcher(this.getExpireKey()).matches()) {
            return Duration.parse(this.getExpireKey());
        }
        return cacheNameExpire == null ? expire : cacheNameExpire;
    }


    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheKey = getKey(key);
        Object prevValue = null;

        // 检测值是否有必要缓存
        if (!checkValue(value)) {
            return toValueWrapper(null);
        }

        // 考虑使用分布式锁，或者将redis的setIfAbsent改为原子性操作
        synchronized (key) {
            boolean isAbsent = false;

            if (this.cacheType != CacheType.ehcache) {
                try {
                    prevValue = redisTemplate.opsForValue().get(cacheKey);

                    if (prevValue == null) {
                        Duration expire = getExpire();
                        log.info("插入redis库，key:{},value:{}", key, value);

                        if (expire.toMillis() > 0) {
                            redisTemplate.opsForValue().setIfAbsent(cacheKey, toStoreValue(value), expire.toMillis(), TimeUnit.MILLISECONDS);
                        } else {
                            redisTemplate.opsForValue().setIfAbsent(cacheKey, toStoreValue(value), defaultExpiration.toMillis(), TimeUnit.MILLISECONDS);
                        }
                        isAbsent = true;
                    }
                    this.addCacheKey(cacheKey);
                } catch (Exception ex) {
                    if (this.cacheType == CacheType.redis) {
                        throw ex;
                    } else {
                        log.warn("redis缓存操作异常", ex);
                    }
                }
            }

            if (this.cacheType != CacheType.redis) {
                prevValue = ehcacheCache.get(cacheKey);
                if (prevValue == null) {
                    log.info("插入ehcache库，key:{},value:{}", key, value);
                    ehcacheCache.putIfAbsent(key, toStoreValue(value));
                    isAbsent = true;
                }
            }
            if (isAbsent) {
                push(new CacheMessage(this.name, key, this.ehcacheCache.hashCode()));
            }
        }
        return toValueWrapper(prevValue);
    }

    @Override
    public void evict(Object key) {
        if (this.cacheType != CacheType.ehcache) {
            log.info("删除redis库，key:{}", key);
            try {
                Object cacheKey = getKey(key);
                // 先清除redis中缓存数据，然后清除ehcache中的缓存，避免短时间内如果先清除ehcache缓存后其他请求会再从redis里加载到ehcache中
                redisTemplate.delete(cacheKey);
                this.delCacheKey(key);
            } catch (Exception ex) {
                if (this.cacheType == CacheType.redis) {
                    throw ex;
                } else {
                    log.warn("redis缓存操作异常", ex);
                }
            }

            push(new CacheMessage(this.name, key, this.ehcacheCache.hashCode()));
        }
        if (this.cacheType != CacheType.redis) {
            log.info("删除ehcache库，key:{}", key);
            ehcacheCache.remove(key);
        }
    }

    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合条件的key
     *
     * @return
     */
    public Set<Object> cacheKeys() {
        if (this.cacheKeys == null || this.cacheKeys.expired()) {
            synchronized (this) {
                Set<Object> keys = new HashSet<>();
                this.scan(this.name.concat(":*"), item -> {
                    //符合条件的key
                    String key = new String(item, StandardCharsets.UTF_8);
                    keys.add(key);
                });
                this.cacheKeys = new CacheKeys(keys);
            }
        }
        return cacheKeys.keys;
    }

    public void addCacheKey(Object cacheKey) {
        this.cacheKeys().add(cacheKey);
    }

    public void delCacheKey(Object cacheKey) {
        this.cacheKeys().remove(cacheKey);
    }


    public void clearCacheKeys() {
        synchronized (this) {
            this.cacheKeys = null;
        }
    }

    @Override
    public void clear() {
        if (this.cacheType != CacheType.ehcache) {
            try {
                // 先清除redis中缓存数据，然后清除ehcache中的缓存，避免短时间内如果先清除ehcache缓存后其他请求会再从redis里加载到ehcache中
                Set<Object> keys = this.cacheKeys();
                if (keys != null && keys.size() > 0) {
                    for (Object key : keys) {
                        redisTemplate.delete(key);
                    }
                    this.clearCacheKeys();
                }

            } catch (Exception ex) {
                if (this.cacheType == CacheType.redis) {
                    throw ex;
                } else {
                    log.warn("redis缓存操作异常", ex);
                }
            }

            push(new CacheMessage(this.name, null, null));
        }
        if (this.cacheType != CacheType.redis) {
            ehcacheCache.clear();
        }
    }

    // 获根据key取缓存,如果返回null，则要读取持久层
    @Override
    protected Object lookup(Object key) {
        Object cacheKey = getKey(key);
        Object value = null;

        if (this.cacheType != CacheType.redis) {
            value = ehcacheCache.get(key);
            if (value != null) {
                return value;
            }
        }

        if (this.cacheType != CacheType.ehcache) {
            try {
                value = redisTemplate.opsForValue().get(cacheKey);
                if (value != null) {
                    this.addCacheKey(cacheKey);
                    if (this.cacheType != CacheType.redis) {
                        // 将二级缓存重新复制到一级缓存。原理是最近访问的key很可能再次被访问
                        ehcacheCache.put(key, value);
                    }
                    return value;
                }
            } catch (Exception ex) {
                if (this.cacheType == CacheType.redis) {
                    throw ex;
                } else {
                    log.warn("redis缓存操作异常", ex);
                }
            }
        }

        log.info("can not get data from cache , the key is : {}", cacheKey);
        return value;
    }


    /**
     * 缓存变更时，利用redis的消息订阅功能，通知其他节点清理本地缓存。
     *
     * @param message
     */
    private void push(CacheMessage message) {
        try {
            redisTemplate.convertAndSend(topic, message);
        } catch (Exception ex) {
            if (this.cacheType == CacheType.redis) {
                throw ex;
            } else {
                log.warn("redis缓存操作异常", ex);
            }
        }
    }

    /**
     * 清理本地缓存
     *
     * @param key
     */
    public void clearLocal(Object key) {
        log.debug("clear local cache, the key is : {}", key);
        if (key == null) {
            ehcacheCache.clear();
        } else {
            ehcacheCache.remove(key);
        }
    }

    public Cache<Object, Object> getLocalCache() {
        return ehcacheCache;
    }


    /**
     * 检测值是否有必要缓存
     *
     * @param value 检测对象
     * @return
     */
    public boolean checkValue(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof ResponseResult) {
            return !((ResponseResult) value).getHasError();
        }
        return true;
    }
}
