package com.reine.backend.enums;

import lombok.Getter;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author reine
 */
@Getter
public enum EmailEnum {

    REGISTER("register") {
        @Override
        public SimpleMailMessage generateMailMessage(String code, String username, String email) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("欢迎注册我们的网站");
            message.setText("您的邮件注册验证码为：" + code + "，有效时间为3分钟，为了保障您的安全，请勿向他人泄露验证码信息");
            message.setTo(email);
            message.setFrom(username);
            return message;
        }
    }, RESET("reset") {
        @Override
        public SimpleMailMessage generateMailMessage(String code, String username, String email) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("您的密码重置邮件");
            message.setText("您好，您正在进行重置密码操作，验证码：" + code + "，有效时间为3分钟，如非本人操作，请忽略此邮件");
            message.setTo(email);
            message.setFrom(username);
            return message;
        }
    };

    private final String type;

    EmailEnum(String type) {
        this.type = type;
    }

    public abstract SimpleMailMessage generateMailMessage(String code, String username, String email);

    private static final Map<String, EmailEnum> EMAIL_MAP =
            Arrays.stream(EmailEnum.values()).collect(Collectors.toMap(EmailEnum::getType, Function.identity()));

    public static EmailEnum of(String type) {
        return EMAIL_MAP.get(type);
    }
}
