package ru.sirius.concordia.user.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sirius.concordia.user.config.PhotoConfig;
import ru.sirius.concordia.user.model.Photo;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.repository.PhotoRepositoryInterface;
import ru.sirius.concordia.user.repository.UserRepositoryInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoConfig photoConfig;

    private PhotoRepositoryInterface photoRepository;

    private UserRepositoryInterface userRepository;

    public Photo uploadPhoto(
            Long userId,
            MultipartFile file
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String fileName = saveFile(userId, file);

        Photo photo = Photo.builder()
                .path(fileName)
                .user(user)
                .isAvatar(
                        user.getPhotos().isEmpty()
                )
                .build();

        return photoRepository.save(photo);
    }

    public Photo setAvatar(Long userId, Long photoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Photo photo = user.getPhotos().stream()
                .filter(p -> p.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Photo not found for this user"));

        user.getPhotos().forEach(p -> p.setIsAvatar(false));

        photo.setIsAvatar(true);

        return photoRepository.save(photo);
    }

    private String saveFile(Long userId, MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();


            ClassPathResource resource = new ClassPathResource("static/");

            String relativePathFromResourceFolder = Paths.get(
                    photoConfig.getPath(),
                    userId.toString(),
                    "photo"
            ).toString();

            Path userPhotoDir = Paths.get(
                    resource.getFile().getAbsolutePath(),
                    relativePathFromResourceFolder
            );

            File storageDir = userPhotoDir.toFile();

            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            File destinationFile = new File(storageDir, fileName);

            file.transferTo(destinationFile);

            return Paths.get(
                    relativePathFromResourceFolder,
                    destinationFile.getName()
            ).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}

