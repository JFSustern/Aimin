package com.oimc.aimin.cache.formal;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.oimc.aimin.cache.formal.config.AiminCacheConfig;
import com.oimc.aimin.cache.formal.util.L2RedisCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Aimin缓存管理器实现类
 * 负责创建、管理和配置Aimin二级缓存实例
 * 实现Spring的CacheManager接口，支持集成到Spring缓存框架
 */
public class AiminCacheManager implements CacheManager {

    /**
     * 缓存实例映射表，用于存储已创建的缓存实例
     * key为缓存名称，value为缓存实例
     */
    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    /**
     * 允许创建的缓存名称集合
     * 当dynamic=false时，只有此集合中的缓存名称才允许被创建
     */
    private Set<String> cacheNames;
    
    /**
     * 是否允许动态创建缓存
     * true: 允许动态创建任意名称的缓存
     * false: 只允许创建cacheNames中指定的缓存
     */
    private boolean dynamic = true;
    
    /**
     * Redis缓存工具类
     */
    private L2RedisCache redisCache;
    
    /**
     * 二级缓存配置
     */
    private AiminCacheConfig l2CacheConfig;

    /**
     * 获取指定名称的缓存实例
     * 若缓存不存在，则根据配置决定是否创建
     *
     * @param name 缓存名称
     * @return 缓存实例，如果不允许创建则返回null
     */
    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            return cache;
        }
        if(!dynamic && !cacheNames.contains(name)) {
            return cache;
        }

        cache = new AiminCache(name, redisCache, caffeineCache(), l2CacheConfig);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        return oldCache == null ? cache : oldCache;
    }

    /**
     * 获取所有缓存名称
     * 当前实现返回空列表，实际缓存名称需查看cacheMap
     *
     * @return 缓存名称集合
     */
    @Override
    public Collection<String> getCacheNames() {
        return List.of();
    }

    /**
     * 构造函数
     *
     * @param l2CacheConfig 二级缓存配置
     * @param redisCache Redis缓存工具类
     */
    public AiminCacheManager(AiminCacheConfig l2CacheConfig, L2RedisCache redisCache) {
        super();
        this.l2CacheConfig = l2CacheConfig;
        this.redisCache = redisCache;
        this.dynamic = l2CacheConfig.isDynamic();
        this.cacheNames = l2CacheConfig.getCacheNames();
    }

    /**
     * 创建Caffeine缓存实例
     * 根据配置设置缓存参数，如初始容量、最大容量、过期时间等
     *
     * @return 配置好的Caffeine缓存实例
     */
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(l2CacheConfig.getCaffeine().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(l2CacheConfig.getCaffeine().getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if(l2CacheConfig.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(l2CacheConfig.getCaffeine().getExpireAfterWrite(), TimeUnit.SECONDS);
        }
        if(l2CacheConfig.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(l2CacheConfig.getCaffeine().getInitialCapacity());
        }
        if(l2CacheConfig.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(l2CacheConfig.getCaffeine().getMaximumSize());
        }
        if(l2CacheConfig.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(l2CacheConfig.getCaffeine().getRefreshAfterWrite(), TimeUnit.SECONDS);
        }
        return cacheBuilder.build();
    }

    /**
     * 清除指定缓存的本地缓存项
     * 用于缓存同步，当其他节点更新缓存时调用此方法清除本地缓存
     *
     * @param cacheName 缓存名称
     * @param key 缓存键，如果为null则清除所有
     */
    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }
        AiminCache redisCaffeineCache = (AiminCache) cache;
        redisCaffeineCache.clearLocal(key);
    }
}
