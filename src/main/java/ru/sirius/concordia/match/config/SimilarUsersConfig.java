package ru.sirius.concordia.match.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sirius.concordia.match.ml.java.SimilarUsersHandler;

@Configuration
@AllArgsConstructor
public class SimilarUsersConfig {
    private final MlConfig mlConfig;

    @Bean
    public SimilarUsersHandler getSimilarUsersHandler() {
        return new SimilarUsersHandler(mlConfig);
    }
}
