package ru.sirius.concordia.auth.service;

import ru.sirius.concordia.auth.model.dto.entity.UserDTO;
import ru.sirius.concordia.auth.model.entity.Role;
import ru.sirius.concordia.auth.model.entity.User;
import ru.sirius.concordia.auth.model.repository.api.UserRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepositoryInterface userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(
                () -> new RuntimeException("User is not found")
        );
    }

    public User create(UserDTO userDTO, Role.Code roleCode) {
        User user = User.builder()
                .nickname(userDTO.getNickname())
                .password(
                        passwordEncoder.encode(userDTO.getPassword())
                )
                .role(
                        roleService.getByCode(roleCode)
                )
                .build();

        return userRepository.save(user);
    }

    public User create(UserDTO userDTO) {
        return create(userDTO, Role.Code.ROLE_USER);
    }
}

