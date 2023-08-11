package com.reine.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author reine
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    public static final int ORDER_CORS = -102;


    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    // 消息队列配置参数
    public static final String REDIS_STREAM = "redis-stream";

    public static final String REDIS_STREAM_CONSUMER_GROUP = "mail-group";

    public static final String REDIS_STREAM_CONSUMER = "mail-consumer";
}
