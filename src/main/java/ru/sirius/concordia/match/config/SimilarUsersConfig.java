package ru.sirius.concordia.match.config;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sirius.concordia.match.ml.SimilarUsersHandler;

@Configuration
@AllArgsConstructor
public class SimilarUsersConfig {
    private final MlConfig mlConfig;

    private final ModelMapper modelMapper;

    @Bean
    public SimilarUsersHandler getSimilarUsersHandler() {
        return new SimilarUsersHandler(
                mlConfig,
                modelMapper
        );
    }
}
