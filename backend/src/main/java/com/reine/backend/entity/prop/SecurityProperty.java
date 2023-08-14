package com.reine.backend.entity.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author reine
 */
@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperty {

    private List<String> permitUrl;

}
