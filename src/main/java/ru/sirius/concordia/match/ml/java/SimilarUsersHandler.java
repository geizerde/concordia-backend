package ru.sirius.concordia.match.ml.java;

import lombok.AllArgsConstructor;
import ru.sirius.concordia.match.config.MlConfig;
import ru.sirius.concordia.user.model.Tag;
import ru.sirius.concordia.user.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@AllArgsConstructor
public class SimilarUsersHandler {
    private static final String PYTHON_EXECUTABLE = "C:\\Users\\geize\\Desktop\\test\\demo-ml\\src\\ml\\KNN\\.venv\\Scripts\\python.exe";

    private final MlConfig mlConfig;

    public Map<Integer, Double> findSimilarUsers(
            Long userId,
            Long countNeighbors,
            Double mutationChance
    ) {
        Map<Integer, Double> userSimilarityMap = new HashMap<>();

        ProcessBuilder processBuilder = new ProcessBuilder(
                PYTHON_EXECUTABLE,
                mlConfig.getPathToScript(),
                String.valueOf(userId),
                String.valueOf(countNeighbors),
                String.valueOf(mutationChance),
                mlConfig.getPathToUsersCsv()
        );

        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            readProcessOutput(process, userSimilarityMap);
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during Python script execution: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return userSimilarityMap;
    }

    private void readProcessOutput(Process process, Map<Integer, Double> userSimilarityMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, userSimilarityMap);
            }
        } catch (IOException e) {
            System.err.println("Error reading process output: " + e.getMessage());
        }
    }

    private void parseLine(String line, Map<Integer, Double> userSimilarityMap) {
        String[] parts = line.split(" ");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[0]);
                double similarity = Double.parseDouble(parts[1]);
                userSimilarityMap.put(id, similarity);
            } catch (NumberFormatException e) {
                System.err.println("Invalid line format: " + line);
            }
        }
    }

    public void generateCsv(List<User> users) {
        Path path = Paths.get(mlConfig.getPathToUsersCsv());

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Error deleting existing CSV file: " + e.getMessage());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,age,interests\n");

            for (User user : users) {
                StringJoiner interestsJoiner = new StringJoiner(", ");
                List<Tag> tags = user.getTags();

                if (tags != null && !tags.isEmpty()) {
                    tags.stream()
                            .map(Tag::getName)
                            .forEach(interestsJoiner::add);
                } else {
                    interestsJoiner.add("-");
                }

                String csvLine = String.format("%d,%d,\"%s\"",
                        user.getId(),
                        user.getAge(),
                        interestsJoiner
                );

                writer.write(csvLine + "\n");
            }

            System.out.println("CSV file generated successfully at: " + path);
        } catch (IOException e) {
            System.err.println("Error generating CSV file: " + e.getMessage());
        }
    }
}
