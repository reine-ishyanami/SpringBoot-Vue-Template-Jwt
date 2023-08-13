package com.reine.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author reine
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    // 已登出的token
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    // 过滤器执行顺序
    public static final int ORDER_LIMIT = -101;
    public static final int ORDER_CORS = -102;

    // 邮箱验证参数
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    // 限流配置参数
    public static final String FLOW_LIMIT_COUNTER = "flow:counter:";

    public static final String FLOW_LIMIT_BLOCK = "flow:block:";

    // 消息队列配置参数
    public static final String REDIS_STREAM = "redis-stream";

    public static final String REDIS_STREAM_CONSUMER_GROUP = "mail-group";

    public static final String REDIS_STREAM_CONSUMER = "mail-consumer";
}
