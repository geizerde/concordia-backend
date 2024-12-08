package ru.sirius.concordia.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.user.model.dto.UserDTO;
import ru.sirius.concordia.user.model.Role;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.repository.UserRepositoryInterface;
import ru.sirius.concordia.user.service.location.CityService;

@Service
@AllArgsConstructor
public class UserService {

    private final CityService cityService;

    private final UserRepositoryInterface userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User is not found")
        );
    }

    public User create(UserDTO userDTO, Role.Code roleCode) {
        User user = User.builder()
                .name(userDTO.getName())
                .isActive(userDTO.getIsActive())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .age(userDTO.getAge())
                .city(
                        cityService.findById(
                                userDTO.getCityId()
                        )
                )
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

