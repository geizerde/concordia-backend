package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.User;

import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
}
