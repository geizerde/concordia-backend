package ru.sirius.concordia.auth.model.repository.api;

import ru.sirius.concordia.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
}
