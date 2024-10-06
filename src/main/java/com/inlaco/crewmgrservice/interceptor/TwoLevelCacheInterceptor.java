// package com.foodey.server.interceptor;

// import org.springframework.cache.Cache;
// import org.springframework.cache.CacheManager;
// import org.springframework.cache.interceptor.CacheInterceptor;
// import org.springframework.data.redis.cache.RedisCache;

// public class TwoLevelCacheInterceptor extends CacheInterceptor {

//   private final CacheManager caffeineCacheManager;

//   public TwoLevelCacheInterceptor(CacheManager caffeineCacheManager) {
//     this.caffeineCacheManager = caffeineCacheManager;
//   }

//   @Override
//   protected Cache.ValueWrapper doGet(Cache cache, Object key) {
//     Cache.ValueWrapper existingCacheValue = super.doGet(cache, key);

//     if (existingCacheValue != null && cache.getClass() == RedisCache.class) {
//       Cache caffeineCache = caffeineCacheManager.getCache(cache.getName());
//       if (caffeineCache != null) {
//         caffeineCache.putIfAbsent(key, existingCacheValue.get());
//       }
//     }

//     return existingCacheValue;
//   }
// }
