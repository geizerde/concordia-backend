package ru.sirius.concordia.match.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "dating")
public class DatingConfig {
    private int matchCooldown;
}
