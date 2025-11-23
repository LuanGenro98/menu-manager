package br.com.luangenro.menu.manager.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.luangenro.menu.manager.domain.CacheName.*;
import static java.util.Objects.nonNull;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor(onConstructor_ = @__({@Autowired}))
public class CacheConfiguration implements CachingConfigurer {

    private static final String CACHE_TTL_PREFIX = "cache.ttl.";

    private final Environment environment;
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Override
    public CacheManager cacheManager() {
        final Map<String, RedisCacheConfiguration> cacheConfiguration = setupCacheConfiguration(
                GET_ITEM_BY_CATEGORY,
                GET_ITEM_BY_ID,
                GET_ALL_CATEGORIES,
                GET_CATEGORY_BY_ID
        );

        return RedisCacheManager.builder(redisConnectionFactory)
            .withInitialCacheConfigurations(cacheConfiguration)
            .build();
    }


    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            log.debug("Generating cache key for: target={}, method={}, params={}", target, method, params);

            if (nonNull(params) && params.length > 0)   {
                return Arrays.stream(params).map(String::valueOf).collect(Collectors.joining(","));
            }

            return target.getClass().getSimpleName().concat("#").concat(method.getName());
        };
    }

    private Map<String, RedisCacheConfiguration> setupCacheConfiguration(final String... cacheNames) {
        final Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();

        final ObjectMapper redisObjectMapper = new ObjectMapper();
        redisObjectMapper.findAndRegisterModules();

        redisObjectMapper.activateDefaultTyping(
                redisObjectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
        );

        Arrays.stream(cacheNames).forEach(cache -> cacheConfiguration.put(cache, createCacheConfiguration(cache, redisObjectMapper)));
        return cacheConfiguration;
    }


    private RedisCacheConfiguration createCacheConfiguration(final String ttlKey, final ObjectMapper redisObjectMapper) {
        final Long ttl = environment.getProperty(CACHE_TTL_PREFIX + ttlKey, Long.class);
        log.debug("Configuring cache for: key={}, ttl={}", ttlKey, ttl);

        final var keySerializer = fromSerializer(new StringRedisSerializer());
        final var valueSerializer = fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper));

        return defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(ttl))
                .disableCachingNullValues()
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer);
    }
}
