package ru.sirius.concordia.auth.service;

import ru.sirius.concordia.auth.model.dto.entity.RoleDTO;
import ru.sirius.concordia.auth.model.entity.Role;
import ru.sirius.concordia.auth.model.repository.api.RoleRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepositoryInterface roleRepository;

    public Role create(RoleDTO roleDTO) {
        Role role = Role.builder()
                .name(roleDTO.getName())
                .code(roleDTO.getCode())
                .build();

        return roleRepository.save(role);
    }

    public Role getByCode(Role.Code code) {
        return roleRepository.findByCode(code).orElseThrow(
                () -> new RuntimeException("Role is not found")
        );
    }
}
