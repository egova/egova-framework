package com.egova.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "cache.multi")
public class RedisEhcacheProperties {
    private Set<String> cacheNames = new HashSet<>();

    /**
     * 是否存储空值，默认true，防止缓存穿透
     */
    private boolean cacheNullValues = false;

    /**
     * 是否动态根据cacheName创建Cache的实现，默认true
     */
    private boolean dynamic = true;

    /**
     * 缓存key的前缀
     */
    private String cachePrefix;

    private CacheType cacheType = CacheType.all;

    private Redis redis = new Redis();

    private Ehcache ehcache = new Ehcache();


    @Data
    public class Redis {

        /**
         * 全局过期时间，单位毫秒，默认不过期
         */
        private Duration defaultExpiration = Duration.ofSeconds(0);

        /**
         * 每个cacheName的过期时间，单位毫秒，优先级比defaultExpiration高
         */
        private Map<String, Duration> expires = new HashMap<String, Duration>(){{
            put("minute10",Duration.ofMinutes(10));
            put("minute30",Duration.ofMinutes(30));
            put("hour1",Duration.ofHours(1));
            put("hour12",Duration.ofHours(12));
            put("hour24",Duration.ofHours(24));
        }};

        /**
         * 缓存更新时通知其他节点的topic名称
         */
        private String topic = "cache:redis:ehcache:topic";




    }


    @Data
    public class Ehcache {


        /**
         * 写入后过期时间，单位毫秒
         */
        private long expireAfterWrite=60000;


        /**
         * 每个ehcache最大缓存对象个数，超过此数量时按照失效策略（默认为LRU）
         */
        private long maxEntry = 500;

    }
}

