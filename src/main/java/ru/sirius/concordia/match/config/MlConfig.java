package ru.sirius.concordia.match.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ml.knn")
public class MlConfig {
    private String pathToScript;

    private String pathToUsersCsv;

    private String ScheduleGenerateUserTagsCsv;
}
