package ru.sirius.concordia.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sirius.concordia.user.model.Tag;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.repository.TagRepositoryInterface;
import ru.sirius.concordia.user.repository.UserRepositoryInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepositoryInterface tagRepository;
    private final UserRepositoryInterface userRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(String name) {
        return tagRepository.save(
                Tag.builder()
                        .name(name)
                        .build()
        );
    }

    @Transactional
    public User addTagsToUser(Long userId, List<Long> tagIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);

        user.getTags().clear();
        user.getTags().addAll(tags);

        return userRepository.save(user);
    }

    public List<User> getUsersByTagId(Long tagId) {
        return userRepository.findUsersByTagsId(tagId);
    }
}
