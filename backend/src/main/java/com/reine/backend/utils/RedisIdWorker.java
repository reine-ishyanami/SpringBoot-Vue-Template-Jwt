package com.reine.backend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author reine
 */
@Component
@RequiredArgsConstructor
public class RedisIdWorker {

    private static final long BEGIN_TIMESTAMP = 1693056747L;

    private static final int COUNT_BITS = 32;

    private final StringRedisTemplate redisTemplate;

    public long nextId(String keyPrefix) {
        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2. 生成序列号
        // 2.1 获取当前日期
        String today = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = Optional.ofNullable(redisTemplate.opsForValue()
                .increment(Constant.REDIS_GLOBAL_ID_GENERATOR + keyPrefix + ":" + today)).orElse(0L);
        // 3. 拼接并返回
        return timestamp << COUNT_BITS | count;
    }

}
