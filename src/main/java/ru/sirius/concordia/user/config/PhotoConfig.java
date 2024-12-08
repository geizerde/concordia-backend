package ru.sirius.concordia.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "photo.storage")
public class PhotoConfig {
    private String path;
}
