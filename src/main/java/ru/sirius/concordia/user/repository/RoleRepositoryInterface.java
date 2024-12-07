package ru.sirius.concordia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.concordia.user.model.Role;

import java.util.Optional;

public interface RoleRepositoryInterface extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(Role.Code code);
}
