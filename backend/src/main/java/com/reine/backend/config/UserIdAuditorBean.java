package com.reine.backend.config;

import com.reine.backend.utils.AccountIdThreadLocal;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author reine
 */
@Configuration
public class UserIdAuditorBean implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Long id = AccountIdThreadLocal.get();
        AccountIdThreadLocal.remove();
        if (ctx == null) {
            return Optional.ofNullable(id);
        }
        if (ctx.getAuthentication() == null) {
            return Optional.ofNullable(id);
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return Optional.ofNullable(id);
        }
        Object principal = ctx.getAuthentication().getPrincipal();
        if (principal.getClass().isAssignableFrom(Long.class)) {
            return Optional.of((Long) principal);
        } else {
            return Optional.ofNullable(id);
        }
    }
}
