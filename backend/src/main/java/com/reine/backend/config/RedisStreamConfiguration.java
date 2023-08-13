package com.reine.backend.config;

import com.reine.backend.listener.MailQueueListener;
import com.reine.backend.utils.Constant;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.ErrorHandler;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis stream消息队列配置类
 *
 * @author reine
 */
@Configuration
@RequiredArgsConstructor
public class RedisStreamConfiguration {

    private final RedisConnectionFactory factory;

    private final MailQueueListener listener;

    private final StringRedisTemplate template;

    @PostConstruct
    public void initConsumerGroup() {
        try {
            template.opsForStream().createGroup(Constant.REDIS_STREAM, Constant.REDIS_STREAM_CONSUMER_GROUP);
        } catch (RedisSystemException ignore) {
            //避免组已存在异常 BUSYGROUP Consumer Group name already exists
        }
    }

    @PreDestroy
    public void destroy(){

    }


    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> container() {
        AtomicInteger index = new AtomicInteger(1);
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("async-stream-consumer-" + index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 一次最多获取多少条消息
                        .batchSize(1)
                        // 运行 Stream 的 poll task
                        .executor(executor)
                        // Stream 中没有消息时，阻塞多长时间，需要比 `spring.redis.timeout` 的时间小
                        .pollTimeout(Duration.ofSeconds(3))
                        // 获取消息的过程或获取到消息给具体的消息者处理的过程中，发生了异常的处理
                        .errorHandler(new StreamErrorHandler())
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(factory, options);

        // 消费组，自动ack
        container.receiveAutoAck(
                Consumer.from(
                        Constant.REDIS_STREAM_CONSUMER_GROUP,
                        Constant.REDIS_STREAM_CONSUMER
                ),
                StreamOffset.create(Constant.REDIS_STREAM, ReadOffset.lastConsumed()),
                listener
        );
        return container;
    }


    @Slf4j
    private static class StreamErrorHandler implements ErrorHandler {
        @Override
        public void handleError(Throwable t) {
            log.error("redis stream exception: {}", t.getMessage());
        }
    }
}
