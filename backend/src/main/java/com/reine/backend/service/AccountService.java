package com.reine.backend.service;

import com.reine.backend.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author reine
 */
public interface AccountService extends UserDetailsService {

    /**
     * 查找账户按名称或电子邮件
     *
     * @param text 文本
     * @return {@link Account}
     */

    Account findAccountByNameOrEmail(String text);
}
