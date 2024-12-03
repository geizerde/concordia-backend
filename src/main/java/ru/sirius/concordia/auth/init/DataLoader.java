package ru.sirius.concordia.auth.init;

import ru.sirius.concordia.auth.model.dto.entity.RoleDTO;
import ru.sirius.concordia.auth.model.dto.entity.UserDTO;
import ru.sirius.concordia.auth.model.entity.Role;
import ru.sirius.concordia.auth.service.RoleService;
import ru.sirius.concordia.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RoleService roleService;

    private final UserService userService;

    private final List<RoleDTO> defaultRoles = List.of(
            new RoleDTO(null, "User Role", Role.Code.ROLE_USER),
            new RoleDTO(null, "Admin Role", Role.Code.ROLE_ADMIN)
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createDefaultRoles();
        createAdminUser();
    }

    private void createDefaultRoles() {
        for (var role : defaultRoles) {
            roleService.create(role);
        }
    }

    private void createAdminUser() {
        userService.create(
                UserDTO.builder()
                        .nickname("Admin")
                        .password("123")
                        .build(),
                Role.Code.ROLE_ADMIN
        );
    }
}
