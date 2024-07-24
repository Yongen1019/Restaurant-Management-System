package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("start create redis template object");
        RedisTemplate redisTemplate = new RedisTemplate();
        // set redis connection factory object
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // set redis serializer (if no serializer inserted key will look like '\xac\xed\x00\x05t\x00\x04code', if has serializer inserted key will look like 'code')
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
