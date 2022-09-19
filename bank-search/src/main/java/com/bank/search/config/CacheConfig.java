package com.bank.search.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheConfig implements CachingConfigurer {
    public static final String CACHE_POLULATE = "CACHE_POPULATE";

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(CACHE_POLULATE);
        return cacheManager;
    }

    /**
     * 5분 주기로 캐시를 초기화 함 > 5분 뒤 재 캐싱
     */
    @Scheduled(fixedRate = 60*5*5000)
    @CacheEvict(allEntries = true, value = {CACHE_POLULATE})
    public void expireCahce() {
        log.info(CACHE_POLULATE + " is expired.");
    }
}
