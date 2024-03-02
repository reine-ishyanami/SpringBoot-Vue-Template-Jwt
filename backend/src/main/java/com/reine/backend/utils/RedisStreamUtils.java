package com.reine.backend.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 使用redis的stream数据类型实现消息队列
 *
 * @author reine
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisStreamUtils {

    private final StringRedisTemplate redisTemplate;

    /**
     * 创建消费组
     *
     * @param key   键
     * @param group 组名称
     * @return {@link String}
     */
    public void createGroup(String key, String group) {
        String res = redisTemplate.opsForStream().createGroup(key, group);
        if (res.equals("OK")) log.info("Consumption group successfully created");
    }

    /**
     * 删除消费组
     *
     * @param key   键
     * @param group 组名称
     * @return {@link String}
     */
    public void destroyGroup(String key, String group) {
        boolean equals = Boolean.TRUE.equals(redisTemplate.opsForStream().destroyGroup(key, group));
        if (equals) log.info("Consumer group successfully deleted");
    }

    /**
     * 添加map消息
     *
     * @param key   键
     * @param value 值
     * @return {@link String}
     */
    public String addMap(String key, Map<String, String> value) {
        return Objects.requireNonNull(redisTemplate.opsForStream().add(key, value)).getValue();
    }

    /**
     * 添加record消息
     *
     * @param record 消息
     * @return {@link String}
     */
    public String addRecord(Record<String, Object> record) {
        return Objects.requireNonNull(redisTemplate.opsForStream().add(record)).getValue();
    }

    /**
     * 确认消费
     *
     * @param key       键
     * @param group     组
     * @param recordIds 消息id
     * @return {@link Long}
     */
    public Long ack(String key, String group, String... recordIds) {
        return redisTemplate.opsForStream().acknowledge(key, group, recordIds);
    }

    /**
     * 删除消息，当一个节点的所有消息都被删除，那么该节点会自动销毁
     *
     * @param key       键
     * @param recordIds 消息id
     * @return {@link Long}
     */
    public Long delete(String key, String... recordIds) {
        return redisTemplate.opsForStream().delete(key, recordIds);
    }
}
