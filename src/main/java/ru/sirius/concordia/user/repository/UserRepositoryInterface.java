package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sirius.concordia.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findUsersByTagsId(Long tagId);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    Optional<String> findEmailById(Long id);
}
