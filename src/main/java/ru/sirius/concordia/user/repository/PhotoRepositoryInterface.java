package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.Photo;

public interface PhotoRepositoryInterface extends JpaRepository<Photo, Long> {
    <T> T findById(Long id, Class<T> type);
}
