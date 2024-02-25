package com.lucassilvs.clientoauthkeycloak.configuration;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.lucassilvs.clientoauthkeycloak.service.properties.TokenProperties;
import org.checkerframework.checker.index.qual.NonNegative;

import java.util.concurrent.TimeUnit;

public class CacheConfiguration {


//    @Bean
//    public CaffeineCache jwtTokenCache() {
//        return new CaffeineCache(
//                "jwtTokenCache",
//                Caffeine.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).recordStats().build());
//    }

}
