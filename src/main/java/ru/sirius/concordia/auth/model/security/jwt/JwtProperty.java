package ru.sirius.concordia.auth.model.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("security.auth.user.jwt")
public class JwtProperty {

    private String secretKey;
}
