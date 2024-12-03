package ru.sirius.concordia.auth.model.repository.api;

import ru.sirius.concordia.auth.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepositoryInterface extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(Role.Code code);
}
