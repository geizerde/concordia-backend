package ru.sirius.concordia.match.config.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sirius.concordia.match.ml.SimilarUsersHandler;
import ru.sirius.concordia.user.service.UserService;

@Component
@AllArgsConstructor
public class GenerateUserTagsCsvTask {
    private final SimilarUsersHandler similarUsersHandler;

    private final UserService userService;

    @Scheduled(cron = "#{@mlConfig.scheduleGenerateUserTagsCsv}")
    public void scheduleGenerateCsv() {
        similarUsersHandler.generateCsv(
                userService.getAllUsers()
        );
    }
}

