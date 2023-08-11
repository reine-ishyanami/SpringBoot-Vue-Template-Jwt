package com.reine.backend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author reine
 */
@Component
@RequiredArgsConstructor
public class FlowUtils {

    private final StringRedisTemplate redisTemplate;

    public boolean limitOnceCheck(String key, int blockTime) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) return false;
        else redisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
        return true;
    }

}
