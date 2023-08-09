package com.reine.backend.config;

import com.reine.backend.entity.ApiResponse;
import com.reine.backend.entity.dto.Account;
import com.reine.backend.entity.vo.response.AuthorizeVO;
import com.reine.backend.filter.JwtAuthorizeFilter;
import com.reine.backend.utils.AccountThreadLocal;
import com.reine.backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author reine
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtUtils utils;

    private final JwtAuthorizeFilter jwtAuthorizeFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                ).logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                ).exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDenied)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)// 注册过滤器在认证器前
                .build();
    }

    private void onAccessDenied(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException e
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(ApiResponse.forbidden(e.getMessage()).asJsonString());
    }

    private void onUnauthorized(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(ApiResponse.unauthorized(e.getMessage()).asJsonString());
    }

    private void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)) writer.write(ApiResponse.success().asJsonString());
        else writer.write(ApiResponse.failure(400, "登出失败").asJsonString());
    }

    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();
        Account account = AccountThreadLocal.get();
        String token = utils.createJwt(user, account.getId(), account.getUsername());
        AuthorizeVO vo =account.asViewObject(AuthorizeVO.class, v -> {
            v.setToken(token);
            v.setExpire(utils.expireTime());
        });
        response.getWriter().write(ApiResponse.success(vo).asJsonString());
        AccountThreadLocal.remove();
    }

    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(ApiResponse.failure(401, exception.getMessage()).asJsonString());
    }
}
