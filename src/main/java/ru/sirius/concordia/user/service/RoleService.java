package ru.sirius.concordia.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.user.model.dto.RoleDTO;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.repository.RoleRepositoryInterface;

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
