package com.reine.backend.service;

import com.reine.backend.entity.dto.Account;
import com.reine.backend.entity.vo.request.EmailRegisterVO;
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

    /**
     * 注册电子邮件验证码
     *
     * @param type  类型
     * @param email 电子邮件
     * @param ip    用户IP地址
     * @return {@link String}
     */
    String registerEmailVerifyCode(String type, String email, String ip);

    /**
     * 注册电子邮件帐户
     *
     * @param vo 表单信息
     * @return {@link String}
     */
    String registerEmailAccount(EmailRegisterVO vo);
}
