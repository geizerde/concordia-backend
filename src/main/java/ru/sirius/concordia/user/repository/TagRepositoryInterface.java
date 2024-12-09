package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.Tag;

public interface TagRepositoryInterface extends JpaRepository<Tag, Long> {
    <T> T findByName(String name);
}

