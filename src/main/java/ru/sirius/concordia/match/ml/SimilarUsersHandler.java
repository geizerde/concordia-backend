package ru.sirius.concordia.match.ml;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.sirius.concordia.match.config.MlConfig;
import ru.sirius.concordia.match.model.dto.UserMatchCoverageDTO;
import ru.sirius.concordia.user.model.Tag;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.model.dto.UserDTO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SimilarUsersHandler {
    private static final String PYTHON_EXECUTABLE = "C:\\Users\\geize\\Desktop\\test\\demo-ml\\src\\ml\\KNN\\.venv\\Scripts\\python.exe";

    private final MlConfig mlConfig;

    private final ModelMapper modelMapper;

    public List<UserMatchCoverageDTO> handle(
            User currentUser,
            List<User> users,
            Long countNeighbors,
            Double mutationChance
    ) {
        try {
            Path pathToUserFile = this.generateCsv(
                    currentUser,
                    users
            );

            Map<Integer, Double> similarUsers = this.findSimilarUsers(
                    currentUser.getId(),
                    countNeighbors,
                    mutationChance,
                    pathToUserFile
            );

            Files.deleteIfExists(pathToUserFile);

            return users.stream()
                    .map(user -> {
                        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                        Double coverage = similarUsers.getOrDefault(
                                user.getId().intValue(),
                                null
                        );

                        return new UserMatchCoverageDTO(userDTO, coverage);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Integer, Double> findSimilarUsers(
            Long userId,
            Long countNeighbors,
            Double mutationChance,
            Path pathToUserFile
    ) {
        Map<Integer, Double> userSimilarityMap = new HashMap<>();

        ProcessBuilder processBuilder = new ProcessBuilder(
                PYTHON_EXECUTABLE,
                mlConfig.getPathToScript(),
                String.valueOf(userId),
                String.valueOf(countNeighbors),
                String.valueOf(mutationChance),
                pathToUserFile.toAbsolutePath().toString()
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

    public Path generateCsv(
            User currentUser,
            List<User> users
    ) {
        String fileName = currentUser.getId() + ".csv";
        Path path = Paths.get(mlConfig.getPathToUsersCsv(), fileName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Error deleting existing CSV file: " + e.getMessage());
        }

        List<User> userListCopy = new ArrayList<>(users);

        if (!userListCopy.contains(currentUser)) {
            userListCopy.addFirst(currentUser);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,age,interests\n");

            for (User user : userListCopy) {
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
            return path;
        } catch (IOException e) {
            System.err.println("Error generating CSV file: " + e.getMessage());
            return null;
        }
    }
}
