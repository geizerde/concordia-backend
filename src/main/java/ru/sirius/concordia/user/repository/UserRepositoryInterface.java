package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findUsersByTagsId(Long tagId);
}
