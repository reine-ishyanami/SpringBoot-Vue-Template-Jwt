package com.reine.backend.utils;

import lombok.Getter;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author reine
 */
@Getter
public enum EmailType {

    REGISTER {
        @Override
        public SimpleMailMessage generateMailMessage(String username, String email) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("欢迎注册我们的网站");
            message.setText("您的邮件注册验证码为：" + this.getCode() + "，有效时间为3分钟，为了保障您的安全，请勿向他人泄露验证码信息");
            message.setTo(email);
            message.setFrom(username);
            return message;
        }
    }, RESET {
        @Override
        public SimpleMailMessage generateMailMessage(String username, String email) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("您的密码重置邮件");
            message.setText("您好，您正在进行重置密码操作，验证码：" + this.getCode() + "，有效时间为3分钟，如非本人操作，请忽略此邮件");
            message.setTo(email);
            message.setFrom(username);
            return message;
        }
    };
    private String code;

    public EmailType setCode(String code) {
        this.code = code;
        return this;
    }

    public abstract SimpleMailMessage generateMailMessage(String username, String email);

}
