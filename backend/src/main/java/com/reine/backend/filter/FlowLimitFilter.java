package com.reine.backend.filter;

import com.reine.backend.entity.RestBean;
import com.reine.backend.utils.Constant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 限流过虑器
 *
 * @author reine
 */
@Component
@Order(Constant.ORDER_LIMIT)
@RequiredArgsConstructor
@Slf4j
public class FlowLimitFilter extends HttpFilter {

    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        if (tryCount(ip, uri)) chain.doFilter(request, response);
        else this.writeBlockMessage(response);
    }

    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden("操作频繁，请稍后再试").asJsonString());
    }

    private boolean tryCount(String ip, String uri) {
        synchronized (ip.intern()) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(Constant.FLOW_LIMIT_BLOCK + ip))) return false;
            return this.limitPeriodCheck(ip, uri);
        }
    }

    /**
     * 限制检查
     *
     * @param ip 知识产权
     * @return boolean
     */
    private boolean limitPeriodCheck(String ip, String uri) {
        if (uri.startsWith("/webjars/")) return true;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(Constant.FLOW_LIMIT_COUNTER + ip))) {
            Long inc = Optional.ofNullable(redisTemplate.opsForValue().increment(Constant.FLOW_LIMIT_COUNTER + ip)).orElse(0L);
            if (inc > 10) {
                redisTemplate.opsForValue().set(Constant.FLOW_LIMIT_BLOCK + ip, "", 30, TimeUnit.SECONDS);
                return false;
            }
        }
        redisTemplate.opsForValue().set(Constant.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS);
        return true;
    }
}
