package com.reine.backend.listener;

import com.reine.backend.utils.EmailType;
import com.reine.backend.utils.RedisStreamUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author reine
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailQueueListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final RedisStreamUtils streamUtils;

    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stream = message.getStream();
        RecordId id = message.getId();
        Map<String, String> data = message.getValue();
        String email = data.get("email");
        String code = data.get("code");
        String type = data.get("type");
        log.info("email:{}, code:{}, type: {}", email, code, type);
        SimpleMailMessage mailMessage = EmailType.valueOf(type.toUpperCase()).setCode(code).generateMailMessage(username, email);
        streamUtils.delete(stream, id.getValue());
        if (mailMessage == null) return;
        try {
            sender.send(mailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
